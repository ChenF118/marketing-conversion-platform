<template>
  <div id="MyQrcode">
    <el-dialog title="查看微信二维码" :visible.sync="outerVisible" width="30%">
      <div class="content">
        <div>
          <el-upload
            class="avatar-uploader"
            action="#"
            :http-request="httpRequest"
            :show-file-list="false"
          >
            <img :src="src.sendimg" alt="" v-if="src.sendimg" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <div class="button">
          <el-button
            type="primary"
            size="small"
            @click="saveimg"
            style="margin-right: 10px"
          >
            保存
          </el-button>

          <el-popconfirm title="确定删除这条吗？" @confirm="deleteimg">
            <el-button type="primary" slot="reference" size="small"
              >删除</el-button
            >
          </el-popconfirm>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { updateQRApi, deleteQRApi } from "@/api/wxmanage";
import { uploadImageApi } from "@/api/upload";
export default {
  name: "MyQrcode",
  props: ["outershow", "image"],
  created() {},
  data() {
    return {
      outerVisible: false,
      src: {},
      flag: false,
    };
  },
  methods: {
    deleteimg() {
      if (this.src.sendimg) {
        this.DeleteQr();
      } else {
        this.$message("图片为空无需删除！");
      }
    },
    saveimg() {
      if (this.flag) {
        this.UpdateQr();
      } else {
        this.$message("请选择图片后再保存！");
      }
    },

    // 上传到服务端，保存返回的 URL（不再把 base64 塞进数据库）
    getFile(file) {
      const form = new FormData();
      form.append("file", file);
      uploadImageApi(form).then((res) => {
        if (res.data.code === 200) {
          this.src.sendimg = res.data.data.url;
        } else {
          this.$message.error(res.data.message);
        }
      });
    },
    beforeAvatarUpload(file) {
      const isImage = ["image/jpeg", "image/png", "image/jpg"].indexOf(
        file.type
      );
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (isImage === -1) {
        return this.$message.error("上传图片只能是 JPG或PNG 格式!");
      }
      if (!isLt2M) {
        return this.$message.error("上传图片大小不能超过 2MB!");
      }
      this.getFile(file);
    },
    httpRequest(data) {
      this.flag = true;
      this.beforeAvatarUpload(data.file);
    },

    // Api
    async UpdateQr() {
      /* const data = await updateQRApi(this.src);
      console.log(data); */
      const { data: res } = await updateQRApi(this.src);
      this.flag = false;
      if (res.code == 200) {
        this.$emit("refresh");
        this.$message({
          message: "修改成功",
          type: "success",
        });
        this.outerVisible = false;
      } else {
        return this.$message.error("未知错误");
      }
    },
    async DeleteQr() {
      const { data: res } = await deleteQRApi(this.src.id);
      if (res.code == 200) {
        this.$message({
          message: "图片已删除",
          type: "success",
        });
        this.src.sendimg = "";
        this.$emit("refresh");
      } else {
        return this.$message.error("未知错误");
      }
    },
  },
  watch: {
    outershow(newval) {
      if (newval == true) {
        this.outerVisible = newval;
        this.src = this.image;
      }
    },
    outerVisible(newval) {
      if (newval == false) {
        this.$emit("closer");
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#MyQrcode {
  :deep(.el-dialog__body) {
    height: 30vh !important;
  }

  .content {
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-around;
  }
}
</style>

<template>
  <div id="Addvnumber">
    <Dialog :formtitle="dialogtitle">
      <template
        ><i class="el-icon-circle-plus-outline" ref="showdialog"></i
        >{{ dialogtitle }}</template
      >
      <template slot="currentForm">
        <div class="half">
          <div class="form_content">
            <el-form
              ref="form"
              :model="form"
              :rules="addrules"
              label-width="100px"
              size="small"
            >
              <el-form-item label="微信号" prop="wxh">
                <el-input
                  v-model="form.wxh"
                  placeholder=""
                  :disabled="isonlyread"
                ></el-input>
              </el-form-item>

              <el-form-item label="微信名称">
                <el-input v-model="form.wxname" placeholder=""></el-input>
              </el-form-item>

              <el-form-item label="添加二维码">
                <el-upload
                  class="avatar-uploader"
                  action="#"
                  :http-request="httpRequest"
                  :show-file-list="false"
                >
                  <img v-if="form.erweima" :src="form.erweima" class="avatar" />
                  <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
              </el-form-item>

              <el-form-item label="微信号性别">
                <el-select
                  clearable
                  v-model="form.sex"
                  placeholder="不使用可不填写"
                >
                  <el-option label="男" value="男"></el-option>
                  <el-option label="女" value="女"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="选择分组" prop="wxgroup">
                <el-select
                  clearable
                  v-model="form.wxgroup"
                  placeholder="请选择"
                >
                  <el-option
                    v-for="item in selectlist"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="在线状态">
                <el-radio-group v-model="form.online">
                  <el-radio label="1">上线</el-radio>
                  <el-radio label="0">下线</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="自动上线下">
                <el-radio-group v-model="form.auto" class="radioflex">
                  <el-radio label="1">关闭</el-radio>
                  <el-radio label="2">按复制次数控制</el-radio>
                  <el-radio label="3">按时间段控制</el-radio>
                  <el-radio label="4">按上报到粉数</el-radio>
                  <el-radio label="5">按二维码长按识别数</el-radio>
                  <el-radio label="6">按数据上报成功</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="选择权重">
                <el-select v-model="form.level" placeholder="不使用可不填写">
                  <el-option label="1" value="1"></el-option>
                  <el-option label="2" value="2"></el-option>
                  <el-option label="3" value="3"></el-option>
                  <el-option label="4" value="4"></el-option>
                  <el-option label="5" value="5"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="备注">
                <el-input
                  v-model="form.remarks"
                  placeholder="简单的页面备注，便于查看统计"
                ></el-input>
              </el-form-item>

              <el-form-item>
                <el-button type="primary" @click="closedialog">取 消</el-button>
                <el-button type="primary" @click="onSubmit('form')"
                  >提 交</el-button
                >
              </el-form-item>
            </el-form>
          </div>

          <div class="tell_content">
            <p>( 建议从微信复制到此位置，避免手动输入错误 不用区分大小写)</p>
            <p>(可以给微信号选择一个分组，便于按分组查看分析数据)</p>
            <p></p>
          </div>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script>
import Dialog from "@/components/Dialog.vue";
// 验证规则
import Bus from "@/utils/event";
import store from "@/store/index";
export default {
  name: "Addvnumber",
  props: ["dialogtitle"],
  created() {
    this.selectlist = store.state.wxgrouplist;
  },
  data() {
    return {
      form: {
        id: 0,
        wxh: "",
        wxname: "",
        erweima: "",
        sex: "",
        wxgroup: "",
        online: "",
        auto: "1",
        level: 5,
        remarks: "",
      },
      addrules: {
        wxh: [{ required: true, message: "请输入微信号", trigger: "blur" }],
        wxgroup: [{ required: true, message: "请选择分组", trigger: "blur" }],
      },
      selectlist: [],
      isonlyread: false,
    };
  },
  components: {
    Dialog,
  },
  methods: {
    // 表单验证
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$emit("sendform", this.form);
          this.$refs.form.resetFields();
        } else {
          this.$message.error("请检查表单");
          return false;
        }
      });
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
    },

    // 头像
    // 文件格式转换
    getFile(file2) {
      this.getBase64(file2).then((res) => {
        this.form.erweima = res;
      });
    },
    beforeAvatarUpload(file) {
      const isJPG =
        ["image/jpeg", "image/PNG", "image/jpg"].indexOf(file.type) == -1;
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (isJPG) {
        return this.$message.error("上传图片只能是 JPG或PNG 格式!");
      }
      if (!isLt2M) {
        return this.$message.error("上传图片大小不能超过 2MB!");
      }
      this.getFile(file);
    },
    getBase64(file1) {
      return new Promise((resolve, reject) => {
        let reader = new FileReader();
        let fileResult = "";
        reader.readAsDataURL(file1); //开始转
        reader.onload = function () {
          fileResult = reader.result;
        }; //转 失败
        reader.onerror = function (error) {
          reject(error);
        }; //转 结束 resolve 出去
        reader.onloadend = function () {
          resolve(fileResult);
        };
      });
    },
    httpRequest(data) {
      this.beforeAvatarUpload(data.file);
    },

    // 打开dialog
    opendialog(forms) {
      this.form.id = forms.id;
      this.form.wxh = forms.wxh;
      this.form.wxname = forms.wxname;
      this.form.erweima = forms.erweima;
      this.form.sex = forms.sex;
      this.form.wxgroup = forms.Wxgroup.id;
      this.form.level = forms.level;
      this.form.remarks = forms.remarks;

      this.isonlyread = true;
      this.$refs.showdialog.click();
    },
  },
};
</script>

<style lang="scss" scoped>
#Addvnumber {
  margin-right: 10px;

  .half {
    display: flex;
  }
  .form_content {
    width: 50%;
  }
  .tell_content {
    flex: 1;
    font-size: 12px;
    padding: 20px 0;

    p {
      margin-bottom: 50px;
    }
  }

  .radioflex {
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;

    .el-radio {
      margin-bottom: 10px;
    }
  }
}
</style>

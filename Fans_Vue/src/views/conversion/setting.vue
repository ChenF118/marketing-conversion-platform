<template>
  <div id="My-Setting">
    <Dialog formtitle="设置" :openfromfather="isopen" @alreadyclose="closer">
      <template>设置按钮</template>
      <template slot="currentForm">
        <div class="content">
          转化时长限制
          <el-divider></el-divider>
          <p style="color: #fe0000">
            设置最小转化时长！访客从开始访问到产生转化的时长,小于该值将不统计！<br />(可以避免部分网络爬虫造成的虚假转化数据)
          </p>
          <div class="flex">
            <div class="form">
              <el-form
                :model="form"
                size="small"
                :rules="rules"
                ref="form"
                label-width="150px"
              >
                <el-form-item label="统计url:">
                  <el-input v-model="url" disabled></el-input>
                </el-form-item>
                <el-form-item label="最小转化时长(秒):" prop="second">
                  <el-input v-model.trim.number="form.second"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">提交</el-button>
                </el-form-item>
              </el-form>
            </div>
            <div class="formright">
              <p>默认0无限制</p>
            </div>
          </div>
        </div>
      </template>
    </Dialog>
  </div>
</template>

<script>
import Bus from "@/utils/event";
import Dialog from "@/components/Dialog.vue";
import { cvssecondApi } from "@/api/conversion";
export default {
  name: "My-Setting",
  created() {
    Bus.$on("tosetsecond", (val) => {
      this.isopen = true;
      this.url = val.landings;
      this.form.id = val.id;
      this.form.second = val.second;
      this.init = val.second;
    });
  },
  props: ["isopenin"],
  data() {
    return {
      isopen: false,
      input: "",
      url: "",
      form: {
        id: "",
        second: 0,
      },
      init: 0,
      rules: {
        second: [
          {
            required: true,
            message: "转化时间必填才可提交",
            trigger: "blur",
          },
          {
            type: "number",
            message: "必须填入数字",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    onSubmit() {
      if (this.init == this.form.second) {
        return false;
      } else {
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.setmin();
            this.$emit("closer");
            Bus.$emit("closedialog");
          } else {
            return false;
          }
        });
      }
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
    },
    closer() {
      this.isopen = false;
      this.$emit("closer");
    },

    // Api
    async setmin() {
      const { data: res } = await cvssecondApi(this.form);
      if (res.code == 200) {
        this.$message({
          message: "修改成功",
          type: "success",
        });
      } else {
        return this.$message.error("修改失败！");
      }
    },
  },
  components: {
    Dialog,
  },
  watch: {
    isopenin(newval) {
      this.isopen = newval;
    },
  },
};
</script>

<style lang="scss" scoped>
.content {
  height: 434px;

  .flex {
    width: 100%;
    display: flex;
    margin-top: 40px;

    .form {
      width: 60%;
      margin-right: 20px;
    }

    .formright {
      p {
        margin-top: 55px;
      }
    }
  }
}
</style>

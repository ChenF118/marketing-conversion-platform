<template>
  <div id="Landwithgroup">
    <my-dialog :formtitle="dialogtitle">
      <template
        ><i class="el-icon-circle-plus-outline" ref="showdialog"></i
        >{{ dialogtitle }}</template
      >
      <template slot="currentForm">
        <div class="half">
          <div class="form_content">
            <el-form ref="form" :model="form" label-width="130px" size="small">
              <el-form-item label="落地页微信号来源">
                <el-radio-group v-model="form.from">
                  <el-radio :label="1">分组控制</el-radio>
                  <el-radio :label="2">手动指定</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="微信号来源分组" v-if="flag">
                <el-select
                  clearable
                  v-model="form.group"
                  placeholder="选择微信号分组"
                >
                  <el-option
                    v-for="item in selectlist"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="微信号展示方式" v-if="flag">
                <el-radio-group v-model="form.show1" class="radioflex">
                  <el-radio label="按权重随机">按权重随机</el-radio>
                  <el-radio label="按最少复制数">按最少复制数</el-radio>
                  <el-radio label="按最少到粉数">按最少到粉数</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="添加微信号" v-else>
                <el-input
                  v-model="form.weixin"
                  placeholder="输入微信号"
                ></el-input>
              </el-form-item>

              <el-form-item label="展示方式" v-if="!flag">
                <el-radio-group v-model="form.show2">
                  <el-radio label="随机显示">随机显示</el-radio>
                  <el-radio label="轮播显示">轮播显示</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="移动点击复制弹窗">
                <el-select v-model="form.mclick" placeholder="">
                  <el-option label="关闭" value="关闭"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="移动长按复制弹窗">
                <el-select v-model="form.mtouch" placeholder="">
                  <el-option label="关闭" value="关闭"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="PC点击复制弹窗">
                <el-select v-model="form.pclick" placeholder="">
                  <el-option label="关闭" value="关闭"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="PC选择复制弹窗">
                <el-select v-model="form.pselect" placeholder="">
                  <el-option label="关闭" value="关闭"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="二维码设置">
                <el-radio-group v-model="form.erweima">
                  <el-radio :label="0">关闭</el-radio>
                  <el-radio :label="1">固定显示</el-radio>
                  <el-radio :label="2">轮播显示</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="同访客微信号固定">
                <el-radio-group v-model="form.same">
                  <el-radio label="关闭">关闭</el-radio>
                  <el-radio label="开启">开启</el-radio>
                </el-radio-group>
              </el-form-item>

              <el-form-item label="分享微信号固定">
                <el-radio-group v-model="form.share">
                  <el-radio label="关闭">关闭</el-radio>
                  <el-radio label="开启">开启</el-radio>
                </el-radio-group>
              </el-form-item>

              <!-- <el-form-item label="微信号样式">
                移动&nbsp;
                <el-select
                  v-model="form.mstyle"
                  label="移动"
                  style="margin-bottom: 10px"
                >
                  <el-option
                    label="样式一（默认）"
                    :value="1"
                  ></el-option> </el-select
                ><br />
                PC&nbsp;&nbsp;
                <el-select v-model="form.pstyle" label="PC">
                  <el-option label="样式一（默认）" :value="1"></el-option>
                </el-select>
              </el-form-item>

              <el-form-item label="字体">
                <el-select v-model="form.font">
                  <el-option :label="'样式一（默认）'" :value="1"></el-option>
                </el-select>
              </el-form-item> -->

              <el-form-item>
                <el-button type="primary" @click="closedialog">取 消</el-button>
                <el-button type="primary" @click="onSubmit">提 交</el-button>
              </el-form-item>
            </el-form>
          </div>
          <div class="tell_content">
            <p>
              ( 从选择的分组中获取微信号，并根据微信号上下线规则自动控制上下线 )
            </p>
            <p>(在微信号分组管理中添加)</p>
            <p>(从分组上线微信号中按设置权重随机显示)</p>
          </div>
        </div>
      </template>
    </my-dialog>
  </div>
</template>

<script>
import Bus from "@/utils/event";
import store from "@/store/index";
import { changelotApi } from "@/api/landing";
export default {
  name: "Landwithgroup",
  props: ["dialogtitle"],
  created() {
    this.selectlist = store.state.wxgrouplist;
  },
  data() {
    return {
      form: {
        id: 0,
        from: 1,
        group: "",
        show1: "按权重随机",
        weixin: "",
        show2: "随机显示",
        mclick: "关闭",
        mtouch: "关闭",
        pclick: "关闭",
        pselect: "关闭",
        erweima: 0,
        same: "",
        share: 0,
        /* mstyle: 1,
        pstyle: 1,
        font: 1, */
      },
      arrs: [],
      selectlist: [],
      // 分组或自定义微信
      flag: true,
    };
  },
  methods: {
    onSubmit() {
      // this.ChangeWeixin();
      this.changelot();
    },
    // 取消按钮
    closedialog() {
      Bus.$emit("closedialog");
    },

    // 打开dialog
    opendialog(forms) {
      this.arrs = [forms.id];
      this.form.id = forms.id;
      this.form.from = forms.from;
      this.form.group = forms.gid;
      this.form.show1 = forms.show;
      this.form.show2 = forms.show;
      this.form.mclick = forms.mclick;
      this.form.mtouch = forms.mtouch;
      this.form.pclick = forms.pclick;
      this.form.pselect = forms.pselect;
      this.form.erweima = forms.erweima;
      this.form.same = forms.same;
      this.form.share = forms.share;

      this.$refs.showdialog.click();
    },

    // 批量设置的打开按钮
    OpenFromEditMany(val) {
      this.$refs.showdialog.click();
      this.arrs = val;
    },

    // Api
    /*   async ChangeWeixin() {
      const { data: res } = await changeweixinApi(this.form);
      if (res.code == 200) {
        this.$message({
          message: "修改成功！",
          type: "success",
        });
        Bus.$emit("closedialog");
        this.$emit("refresh");
      } else {
        return this.$message.error("出错了");
      }
    }, */

    async changelot() {
      const { data: res } = await changelotApi(this.arrs, this.form);
      if (res.code == 200) {
        this.$message({
          message: "修改成功！",
          type: "success",
        });
        Bus.$emit("closedialog");
        this.$emit("refresh");
      } else {
        return this.$message.error("出错了");
      }
    },
  },
  watch: {
    "form.from"(newval) {
      if (newval == "2") {
        this.flag = false;
      } else {
        this.flag = true;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
#Landwithgroup {
  .half {
    display: flex;
  }
  .form_content {
    width: 50%;

    .code {
      height: 40px;
      border-radius: 4px;
      border: 1px solid #dcdfe6;
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
  .tell_content {
    flex: 1;
    font-size: 12px;
    // padding: 10px 0;

    p {
      margin-bottom: 18px;
      line-height: 32px;
    }
  }
}
</style>

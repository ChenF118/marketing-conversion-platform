<template>
  <div id="Home">
    <el-row>
      <el-col :span="24">
        <div class="main-container">
          <!-- 顶部欢迎 -->
          <div class="welcome">
            <div class="bg-light">
              <span
                >欢迎登录：<i style="color: #ff0000; font-style: normal">{{
                  userinfo.username
                }}</i></span
              >
            </div>
          </div>
          <!-- 数据统计 -->
          <div class="statistic">
            <div class="common-title">昨日数据统计</div>
            <Statistic :statistic="statistics"></Statistic>
          </div>
          <!-- 最近数据 -->
          <div class="lately">
            <div class="common-title">最近7天数据</div>
            <div>
              <Linechart :pv="pvs" :uv="uvs" :count="counts"></Linechart>
            </div>
          </div>
          <!-- 常用功能 -->
          <div class="common">
            <div class="common-title">常用功能</div>
            <Conmmon></Conmmon>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import store from "@/store/index";
import Statistic from "@/components/home/Statistic.vue";
import Linechart from "@/views/charts/line.vue";
// 常用功能
import Conmmon from "@/components/Common.vue";
import { monitorhomeApi } from "@/api/conversion";
export default {
  name: "Home",
  created() {
    this.userinfo = store.state.userinfo;
    this.monitorcount();
  },
  data() {
    return {
      userinfo: null,
      // 昨日数据
      statistics: {
        pv: 0,
        uv: 0,
        ip: 0,
        count: 0,
        open: 0,
        cvs: 0,
      },
      pvs: [],
      uvs: [],
      counts: [],
    };
  },
  provide() {
    return {
      pv: this.pvs,
      uv: this.uvs,
      count: this.counts,
    };
  },
  methods: {
    // Api
    async monitorcount() {
      const { data: res } = await monitorhomeApi();
      this.statistics.pv = res.data[6][1].pv;
      this.statistics.uv = res.data[6][1].uv;
      this.statistics.ip = res.data[6][1].uv;
      this.statistics.count = res.data[6][1].count;
      this.statistics.open = res.data[6][1].count;
      this.statistics.cvs = res.data[6][1].count;

      res.data.forEach((item) => {
        this.pvs.push(item[1].pv);
        this.uvs.push(item[1].uv);
        this.counts.push(item[1].count);
      });
    },
  },
  components: {
    Statistic,
    Linechart,
    Conmmon,
  },
};
</script>

<style lang="scss" scoped>
#Home {
  padding: 10px;

  .common-title {
    font-size: 18px;
    line-height: 45px;
    text-align: left;
    border-bottom: 1px solid #f6f6f6;
    margin-bottom: 10px;
    font-weight: 700;
  }

  .main-container {
    background-color: #fff;
    padding: 10px 20px;

    .welcome {
      margin-bottom: 10px;
      .bg-light {
        background: #fafafa;
        line-height: 60px;
      }
    }

    /*  .statistic {
    }

    .lately {
    } */
    .common {
      margin-top: 10px;
    }
  }
}
</style>

<template>
  <div
    :id="id"
    :class="className"
    :style="{ height: height, width: width }"
  ></div>
</template>

<script>
import echarts from "echarts";
import resize from "./mixins/resize";
export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart",
    },
    id: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "200px",
    },
    height: {
      type: String,
      default: "200px",
    },
  },
  created() {
    this.getX();
  },
  data() {
    return {
      chart: null,
      Xline: [],
    };
  },
  inject: {
    pvs: {
      from: "pv",
      default: [0, 0, 0, 0, 0, 0, 0],
    },
    uvs: {
      from: "uv",
      default: [0, 0, 0, 0, 0, 0, 0],
    },
    counts: {
      from: "count",
      default: [0, 0, 0, 0, 0, 0, 0],
    },
  },
  mounted() {
    setTimeout(() => {
      this.initChart();
    }, 1000);
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    getX() {
      let days = [];
      var date = new Date();
      for (let i = 0; i <= 6; i++) {
        //今天加上前6天
        let dateItem = new Date(date.getTime() - (i + 1) * 60 * 60 * 24 * 1000); //使用当天时间戳减去以前的时间毫秒（小时*分*秒*毫秒）
        let m = dateItem.getMonth() + 1; //获取月份js月份从0开始，需要+1
        let d = dateItem.getDate(); //获取日期
        let valueItem = `${m}月${d}日`; //组合
        days.push(valueItem); //添加至数组
      }
      this.Xline = days.reverse();
    },
    initChart() {
      this.chart = echarts.init(document.getElementById(this.id));
      this.chart.setOption({
        backgroundColor: "#394056",
        title: {
          top: 20,
          text: "折线图堆叠",
          textStyle: {
            fontWeight: "normal",
            fontSize: 16,
            color: "#F1F1F3",
          },
          left: "1%",
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            lineStyle: {
              color: "#57617B",
            },
          },
        },
        // 折线图顶部线条名称
        legend: {
          top: 20,
          icon: "rect",
          itemWidth: 14,
          itemHeight: 5,
          itemGap: 13,
          data: ["访问pv", "访问uv", "复制数量"],
          right: "4%",
          textStyle: {
            fontSize: 12,
            color: "#F1F1F3",
          },
        },
        // 内边距
        grid: {
          top: 100,
          left: "3%",
          right: "3%",
          bottom: "2%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            boundaryGap: false,
            axisLine: {
              lineStyle: {
                color: "#57617B",
              },
            },
            data: this.Xline,
          },
        ],
        yAxis: [
          {
            type: "value",
            name: "",
            axisTick: {
              show: false,
            },
            axisLine: {
              lineStyle: {
                color: "#57617B",
              },
            },
            axisLabel: {
              margin: 10,
              textStyle: {
                fontSize: 14,
              },
            },
            splitLine: {
              lineStyle: {
                color: "#57617B",
              },
            },
          },
        ],
        series: [
          {
            name: "访问pv",
            type: "line",
            smooth: true,
            symbol: "circle",
            symbolSize: 5,
            showSymbol: false,
            lineStyle: {
              normal: {
                width: 1,
              },
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: "rgba(137, 189, 27, 0.3)",
                    },
                    {
                      offset: 0.8,
                      color: "rgba(137, 189, 27, 0)",
                    },
                  ],
                  false
                ),
                shadowColor: "rgba(0, 0, 0, 0.1)",
                shadowBlur: 10,
              },
            },
            itemStyle: {
              normal: {
                color: "rgb(137,189,27)",
                borderColor: "rgba(137,189,2,0.27)",
                borderWidth: 12,
              },
            },
            data: this.pvs,
          },
          {
            name: "访问uv",
            type: "line",
            smooth: true,
            symbol: "circle",
            symbolSize: 5,
            showSymbol: false,
            lineStyle: {
              normal: {
                width: 1,
              },
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: "rgba(0, 136, 212, 0.3)",
                    },
                    {
                      offset: 0.8,
                      color: "rgba(0, 136, 212, 0)",
                    },
                  ],
                  false
                ),
                shadowColor: "rgba(0, 0, 0, 0.1)",
                shadowBlur: 10,
              },
            },
            itemStyle: {
              normal: {
                color: "rgb(0,136,212)",
                borderColor: "rgba(0,136,212,0.2)",
                borderWidth: 12,
              },
            },
            data: this.uvs,
          },
          {
            name: "复制数量",
            type: "line",
            smooth: true,
            symbol: "circle",
            symbolSize: 5,
            showSymbol: false,
            lineStyle: {
              normal: {
                width: 1,
              },
            },
            areaStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: "rgba(219, 50, 51, 0.3)",
                    },
                    {
                      offset: 0.8,
                      color: "rgba(219, 50, 51, 0)",
                    },
                  ],
                  false
                ),
                shadowColor: "rgba(0, 0, 0, 0.1)",
                shadowBlur: 10,
              },
            },
            itemStyle: {
              normal: {
                color: "rgb(219,50,51)",
                borderColor: "rgba(219,50,51,0.2)",
                borderWidth: 12,
              },
            },
            data: this.counts,
          },
        ],
      });
    },
  },
};
</script>

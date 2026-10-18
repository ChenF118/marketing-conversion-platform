<template>
  <div id="Common">
    <div class="step">
      <el-steps direction="vertical" :active="4" finish-status="success">
        <el-step title="步骤 1"></el-step>
        <el-step title="步骤 2"></el-step>
        <el-step title="步骤 3"></el-step>
      </el-steps>
    </div>
    <div class="code_block">
      <div class="block_item">
        <h2><span>* </span>转化类型按钮id<i>（添加在具体按钮标签上）</i></h2>

        <div class="code">
          <pre>
            1.点击按钮 class="clicktrigger" 2.长按按钮 class="touchtrigger"
          </pre>
        </div>
        <h6>添加方法：请将此代码标签内部！</h6>
      </div>

      <div class="block_item">
        <h2>
          <span>* </span>转化类型埋点代码<i
            >（必须添加在落地页具体按钮标签上）</i
          >
        </h2>

        <div class="code">
          <pre>
            &lt;span type_index="1"&gt;默认微信号&lt;/span&gt;
          </pre>
        </div>
        <h6>添加方法：添加type_index属性</h6>
      </div>

      <div class="block_item">
        <h2><span>* </span>转化目标埋点代码<i>（按需添加，名称自定义）</i></h2>

        <div class="code">
          <pre>
            &lt;span target_element="顶部点击按钮"&gt;默认微信号&lt;/span&gt;
          </pre>
        </div>
        <h6>添加方法：添加targetelement属性</h6>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Common",
  created() {},
  data() {
    return {};
  },
  components: {},
};
</script>

<style lang="scss" scoped>
#Common {
  padding: 10px;
  display: flex;
  justify-content: flex-start;

  .step {
    background-color: #fff;
    padding: 10px;
  }
  .code_block {
    flex: 1;
    background-color: #fff;
    padding: 10px;

    .block_item {
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      padding: 20px 10px;
      margin-bottom: 20px;

      h2 {
        font-size: 18px;
        text-align: left;
        line-height: 1;
        padding: 15px 10px;
        border-bottom: 1px solid #ccc;
        span {
          color: #fe0000;
        }
        i {
          color: #01aaed;
          font-style: normal;
          font-weight: 400;
        }
      }

      .code {
        background-color: #fafafa;
        height: 50px;
        margin: 10px 0;

        pre {
          height: 100%;
          line-height: 50px;
          width: 100%;
          text-align: left;
        }
      }

      h6 {
        font-size: 12px;
        text-align: left;
        padding-left: 10px;
      }
    }
  }
}
</style>

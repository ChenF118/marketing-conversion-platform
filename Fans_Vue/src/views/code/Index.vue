<template>
  <div id="CodeIndex">
    <div class="step">
      <el-steps direction="vertical" :active="4" finish-status="success">
        <el-step title="步骤 1"></el-step>
        <el-step title="步骤 2"></el-step>
        <el-step title="步骤 3"></el-step>
        <el-step title="步骤 4"></el-step>
      </el-steps>
    </div>
    <div class="code_block">
      <div class="block_item">
        <h2>
          <span>* </span>基础js代码<i
            >（必须添加在落地页上！支持https，安装后可使用除微信在线管理功能外所有功能）</i
          >
        </h2>

        <div class="code">
          <pre>
           1.引入jquery 2. &lt;script type="text/javascript" src="monitor.js"&gt;&lt;/script&gt;
          </pre>
        </div>
        <h6>添加方法：请将此代码放在落地页头部的head标签前面！</h6>
      </div>

      <div class="block_item">
        <h2>
          <span>* </span>微信在线管理代码<i
            >（按需添加，添加后可使用微信号在线管理,点击复制，打开微信等功能！）</i
          >
        </h2>

        <div class="code">
          <pre>
            微信号代码： &lt;span class="inner_text"&gt;默认微信号&lt;/span&gt;
          </pre>
        </div>
        <h6>
          添加方法：将微信号代码替换落地页上的微信号，并且需要替换页面上所有的微信号！
        </h6>
      </div>

      <div class="block_item">
        <h2>
          <span>* </span>微信名称管理代码<i
            >（按需添加，添加后可显示微信名称）</i
          >
        </h2>

        <div class="code">
          <pre>
            微信号名称代码： &lt;span class="inner_name"&gt;默认微信号&lt;/span&gt;
          </pre>
        </div>
        <h6>
          添加方法：将微信名称代码替换落地页上的微信名称，并且需要替换页面上所有的微信名称！
        </h6>
      </div>

      <div class="block_item">
        <h2>
          <span>* </span>二维码代码<i
            >（按需添加，若需要统计二维码，使用二维码轮播等，请正确替换二维码标签！）</i
          >
        </h2>

        <div class="code">
          <pre>
            二维码标签： &lt; img class="atr_img" src="默认二维码地址"/&gt;
          </pre>
        </div>
        <h6>添加方法：替换页面上显示二维码的img标签即可</h6>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CodeIndex",
  created() {},
  data() {
    return {};
  },
  components: {},
};
</script>

<style lang="scss" scoped>
#CodeIndex {
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

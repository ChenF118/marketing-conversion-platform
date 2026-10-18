<template>
  <div id="LoginContainer">
    <div class="formcontent">
      <h2>
        <slot></slot>
      </h2>

      <slot name="form"></slot>
    </div>
  </div>
</template>

<script>
// 验证规则
export default {
  name: "LoginContainer",
};
</script>

<style lang="scss" scoped>
#LoginContainer {
  height: 100vh;
  background: url(../../assets/images/bj.jpg) no-repeat center;
  background-size: 100% 100vh;

  .formcontent {
    background-color: #ffffff;
    width: 460px;
    border-radius: 5px;
    box-shadow: 0 0 20px rgba($color: #3f3f3f, $alpha: 1);
    padding: 30px 0;
    position: fixed;
    top: 20%;
    left: calc(50% - 230px);

    h2 {
      color: #76b9f7;
    }
  }
}
</style>

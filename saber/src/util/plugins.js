let plugins = {
  install(Vue) {
    Vue.prototype.showLoading = function(text = "请求中", target = "body") {
      return this.$loading({
        target: target,
        lock: true,
        text: text,
        spinner: "el-icon-loading"
      });
    };

    Vue.prototype.walert = function(message, title = "提示") {
      return this.$alert(message, title);
    };

    Vue.prototype.wconfirm = function(
      message,
      title = "提示",
      type = "warning"
    ) {
      return this.$confirm(message, title, {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: type,
        dangerouslyUseHTMLString: true,
        closeOnClickModal: false
      });
    };

    // Vue.filter('dateFormate', function (value) {
    //     return value.substring(0, 4) + "-" + value.substring(4, 6) + "-" + value.substring(6, 8)+" " + value.substring(8, 10) +":" +value.substring(10, 12)+":"+ value.substring(12,14)
    // })
  }
};

export default plugins;

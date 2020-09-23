<template>
  <basic-container>
    <avue-crud
      :option="option"
      :table-loading="loading"
      :data="data"
      :page="page"
      :permission="permissionList"
      :before-open="beforeOpen"
      :upload-before="uploadBefore"
      :upload-delete="uploadDelete"
      v-model="form"
      ref="crud"
      @row-update="rowUpdate"
      @row-save="rowSave"
      @row-del="rowDel"
      @search-change="searchChange"
      @search-reset="searchReset"
      @selection-change="selectionChange"
      @current-change="currentChange"
      @size-change="sizeChange"
      @refresh-change="refreshChange"
      @sort-change="sortChange"
      @on-load="onLoad"
    >
      <template slot="phone" slot-scope="scope">
        <span style="color: #F56C6C;font-size:14px;font-weight:bold">{{scope.row.phone}}</span>
      </template>

      <template slot="status" slot-scope="{row}">
        <el-tag :type="statusType[row.status-1]">{{row.$status}}</el-tag>
      </template>

      <template slot="menuLeft">
        <el-button
          type="danger"
          size="small"
          icon="el-icon-delete"
          plain
          v-if="permission.customer_delete"
          @click="handleDelete"
        >删 除</el-button>
        <el-button
          type="primary"
          size="small"
          icon="el-icon-download"
          v-if="permission.customer_export"
          @click="customerExport()"
        >导 出</el-button>
      </template>
    </avue-crud>
  </basic-container>
</template>

<script>
import {
  getList,
  getDetail,
  add,
  update,
  remove,
  exportCustomer,
  removeCustomerImg,
  saveCustomerImg,
} from "@/api/customer/customer";
import { setDialogBodyHeight } from "@/util/util";
import { mapGetters } from "vuex";
import {
  uploadBeforeCallback,
  uploadAfterCallback,
  getFormdataByFile,
  camecaseToLine,
  exportExcel,
} from "@/util/util";

export default {
  data() {
    let checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error("年龄不能为空"));
      }
      value = parseInt(value);
      if (!Number.isInteger(value)) {
        callback(new Error("请输入数字值"));
      } else {
        if (value < 16) {
          callback(new Error("必须年满16岁"));
        } else if (value > 100) {
          callback(new Error("年龄不能大于100"));
        } else {
          callback();
        }
      }
    };

    let validPhone = (rule, value, callback) => {
      if (!value) {
        callback(new Error("请输入电话号码"));
      } else if (!/^1[3|4|5|7|8][0-9]\d{8}$/.test(value)) {
        callback(new Error("请输入正确的11位手机号码"));
      } else {
        callback();
      }
    };

    return {
      id: "",
      files: [],
      form: {},
      opeType: "",
      query: {},
      loading: true,
      page: {
        pageSize: 20,
        currentPage: 1,
        total: 0,
      },
      statusType: ["danger", ""],
      selectionList: [],
      sortParams: {},
      option: {
        menuWidth: 200,
        dialogHeight: 400,
        height: "auto",
        calcHeight: 30,
        dialogHeight: 600,
        dailogHeight: 800,
        tip: false,
        searchShow: true,
        searchMenuSpan: 6,
        border: true,
        index: true,
        viewBtn: true,
        selection: true,
        dialogClickModal: false,
        column: [
          {
            label: "手机",
            prop: "phone",
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            search: true, //是否搜索
            slot: true,
            value: "",
            width: 120,
            rules: [{ required: true, trigger: "blur", validator: validPhone }],
          },
          {
            label: "性别",
            prop: "sex",
            value: "男",
            width: 70,
            dicData: [
              {
                label: "男",
                value: "男",
              },
              {
                label: "女",
                value: "女",
              },
            ],
            type: "radio",
            sortable: "custom",
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            search: true, //是否搜索
            rules: [
              {
                required: true,
                message: "请输入性别",
                trigger: "blur",
              },
            ],
          },

          {
            label: "年龄",
            prop: "age",
            value: "",
            width: 70,
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            search: false, //是否搜索
            sortable: "custom",
            rules: [{ validator: checkAge, trigger: "blur", required: true }],
          },
          {
            label: "生日",
            prop: "birthday",
            type: "date",

            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: true, //列表隐藏
            search: false, //是否搜索
            format: "yyyy-MM-dd",
            valueFormat: "yyyy-MM-dd",
            sortable: "custom",
            rules: [
              {
                required: false,
                message: "请输入生日",
                trigger: "blur",
              },
            ],
          },

          {
            label: "创建时间",
            prop: "createTime",
            width: 140,
            format: "yyyy-MM-dd hh:mm:ss",
            valueFormat: "yyyy-MM-dd hh:mm:ss",
            editDisplay: false, //编辑是否展示
            addDisplay: false, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            search: false, //是否搜索
            sortable: "custom",
          },
          {
            label: "创建人",
            prop: "createUserAccount",
            editDisplay: false, //编辑是否展示
            addDisplay: false, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            search: false, //是否搜索
            width: 100,
          },
          {
            label: "导出时间",
            prop: "exportTime",
            editDisplay: false, //编辑是否展示
            addDisplay: false, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            sortable: "custom",
            search: false, //是否搜索
          },
          {
            label: "导出次数",
            prop: "exportNum",
            editDisplay: false, //编辑是否展示
            addDisplay: false, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            search: false, //是否搜索
            sortable: "custom",
          },
          {
            label: "状态",
            prop: "status",
            sortable: "custom",
            dicData: [
              {
                label: "未导出",
                value: 1,
                color: "red",
              },
              {
                label: "已导出",
                value: 2,
                color: "red",
              },
            ],
            slot: true,
            type: "radio",
            editDisplay: false, //编辑是否展示
            addDisplay: false, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            search: true, //是否搜索
          },
          {
            label: "姓名",
            prop: "name",
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: true, //列表隐藏
            search: false, //是否搜索
            sortable: "custom",
            rules: [
              {
                required: false,
                message: "请输入姓名",
                trigger: "blur",
              },
            ],
          },
          {
            label: "邮箱",
            prop: "email",
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: true, //列表隐藏
            search: false, //是否搜索
            rules: [
              {
                required: false,
                message: "请输入邮箱",
                trigger: "blur",
              },
            ],
          },
          {
            label: "地址",
            prop: "address",
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: true, //列表隐藏
            search: false, //是否搜索
            rules: [
              {
                required: false,
                message: "请输入地址",
                trigger: "blur",
              },
            ],
          },
          {
            label: "收入",
            prop: "income",
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: true, //列表隐藏
            search: false, //是否搜索
            sortable: "custom",
            rules: [
              {
                required: false,
                message: "请输入收入",
                trigger: "blur",
              },
            ],
          },
          {
            label: "备注",
            prop: "remark",
            type: "textarea",
            span: 24,
            minRows: 4,
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: true, //列表隐藏
            search: false, //是否搜索
            rules: [
              {
                required: false,
                message: "请输入备注",
                trigger: "blur",
              },
            ],
          },
          {
            label: "用户图片",
            prop: "customerImgs",
            type: "upload",
            dataType: "array",
            listType: "picture-img",
            span: 24,
            limit: 200,
            hide: true,
            listType: "picture-card",
            headers: { "Content-Type": "multipart/form-data" },
            tip: "只能上传jpg/png文件，且不超过500kb",
            // action: "/api/blade-customer/customer/saveCustomerImg",
          },
        ],
      },
      data: [],
    };
  },
  computed: {
    ...mapGetters(["permission"]),
    permissionList() {
      return {
        addBtn: this.vaildData(this.permission.customer_add, false),
        viewBtn: this.vaildData(this.permission.customer_view, false),
        delBtn: this.vaildData(this.permission.customer_delete, false),
        editBtn: this.vaildData(this.permission.customer_edit, false),
      };
    },
    ids() {
      let ids = [];
      this.selectionList.forEach((ele) => {
        ids.push(ele.id);
      });
      return ids.join(",");
    },
  },
  methods: {
    //删除图片
    uploadDelete(column, file) {
      if (this.opeType == "edit") {
        this.wconfirm("删除后不可恢复，确认要删除图片吗？").then(() => {
          let imgName = "upload" + file.url.split("upload")[1];
          if (this.opeType == "edit") {
            removeCustomerImg(imgName).then((res) => {
              let customerImgsArr = this.form.customerImgs;
              customerImgsArr.forEach((item, index) => {
                if (item == file.url) {
                  customerImgsArr.splice(index, 1);
                }
              });
              this.$message({
                type: "success",
                message: "删除成功",
              });
              return true;
            });
          }
        });
      }
      return false;
    },
    customerExport() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      let loading = this.showLoading("正在导出");
      exportCustomer(this.ids)
        .then((res) => {
          loading.close();
          exportExcel(res);
        })
        .catch(() => {
          loading.close();
        });
    },
    //排序
    sortChange(column) {
      let name = camecaseToLine(column.prop);
      let prop = column.order === "descending" ? "descs" : "ascs";
      let params = {
        [prop]: name,
      };
      this.sortParams = params;
      this.onLoad(this.page);
    },

    uploadBefore(file, done, loading, column) {
      if (this.opeType == "add") {
        uploadBeforeCallback(
          file,
          loading,
          column,
          ["jpeg", "png", "jpg"],
          this
        );
      } else {
        uploadBeforeCallback(
          file,
          loading,
          column,
          ["jpeg", "png", "jpg"],
          this,
          saveImagesCallback,
          this
        );
        function saveImagesCallback(file, that) {
          let formdata = new FormData();
          formdata.append("file", file);
          formdata.append("customerId", that.id);
          saveCustomerImg(formdata).then((res) => {
            that.$message({
              type: "success",
              message: "上传成功",
            });
          });
        }
      }
    },
    getFormdata(row) {
      let formdata = getFormdataByFile(row, this, "customerImgs");
      return formdata;
    },
    rowSave(row, done, loading) {
      add(this.getFormdata(row)).then(
        () => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!",
          });
          done();
        },
        (error) => {
          loading();
          window.console.log(error);
        }
      );
    },
    rowUpdate(row, index, done, loading) {
      row.customerImgs = [];
      update(row).then(
        () => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!",
          });
          done();
        },
        (error) => {
          loading();
          console.log(error);
        }
      );
    },

    rowDel(row) {
      this.$confirm("确定将选择数据删除?", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return remove(row.id);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!",
          });
        });
    },
    handleDelete() {
      if (this.selectionList.length === 0) {
        this.$message.warning("请选择至少一条数据");
        return;
      }
      this.$confirm("确定将选择数据删除?", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          return remove(this.ids);
        })
        .then(() => {
          this.onLoad(this.page);
          this.$message({
            type: "success",
            message: "操作成功!",
          });
          this.$refs.crud.toggleSelection();
        });
    },
    beforeOpen(done, type) {
      this.opeType = type;
      if (["edit", "view"].includes(type)) {
        getDetail(this.form.id).then((res) => {
          this.id = this.form.id;
          this.form = res.data.data;
          let customerImgsArr = res.data.data.customerImgs;
          let arr = [];
          customerImgsArr.forEach((item) => {
            arr.push(item.imgLink);
          });
          this.form.customerImgs = arr;
        });
      }
      done();
      setDialogBodyHeight(this);
    },
    searchReset() {
      this.query = {};
      this.onLoad(this.page);
    },
    searchChange(params, done) {
      this.query = params;
      this.page.currentPage = 1;
      this.onLoad(this.page, params);
      done();
    },
    selectionChange(list) {
      this.selectionList = list;
    },
    selectionClear() {
      this.selectionList = [];
      this.$refs.crud.toggleSelection();
    },
    currentChange(currentPage) {
      this.page.currentPage = currentPage;
    },
    sizeChange(pageSize) {
      this.page.pageSize = pageSize;
    },
    refreshChange() {
      this.onLoad(this.page, this.query);
    },
    onLoad(page, params = {}) {
      let defaultSort = {};
      if (this.validatenull(this.sortParams)) {
        defaultSort = { descs: "create_time" };
      }
      this.loading = true;
      getList(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.query, defaultSort, this.sortParams)
      ).then((res) => {
        const data = res.data.data;
        this.page.total = data.total;
        this.data = data.records;
        this.loading = false;
        this.selectionClear();
      });
    },
  },
};
</script>

<style>
</style>

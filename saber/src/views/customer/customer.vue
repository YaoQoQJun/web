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
} from "@/api/customer/customer";
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
    return {
      files: [],
      form: {},
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
            value: "15213089751",
            rules: [
              {
                required: true,
                message: "请输入手机",
                trigger: "blur",
              },
            ],
          },
          {
            label: "性别",
            prop: "sex",
            value: "男",
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
            value: "89",
            editDisplay: true, //编辑是否展示
            addDisplay: true, //新增是否展示
            viewDisplay: true, //查看是否展示
            hide: false, //列表隐藏
            search: false, //是否搜索
            sortable: "custom",
            rules: [
              {
                required: true,
                message: "请输入年龄",
                trigger: "blur",
              },
            ],
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
    customerExport() {
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

    //删除文件的回调
    uploadAfter(column) {
      uploadAfterCallback(column, this);
    },
    uploadBefore(file, done, loading, column) {
      uploadBeforeCallback(file, loading, column, ["jpeg", "png", "jpg"], this);
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
      if (["edit", "view"].includes(type)) {
        getDetail(this.form.id).then((res) => {
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

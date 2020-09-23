<template>
  <basic-container>
    <div class="ctitle">
      <span>按年份输出(最近3年总数据)</span>
    </div>
    <avue-crud :data="yearCount" :option="yearOption" :table-loading="loading" @on-load="onLoad"></avue-crud>
    <div class="ctitle">
      <span>按月份输出(最近12个月总数据)</span>
    </div>
    <avue-crud :data="monthCount" :option="monthOption" :table-loading="loading"></avue-crud>
  </basic-container>
</template>

<script>
import { allCount } from "@/api/customer/customer";

export default {
  name: "allCount",
  data() {
    return {
      yearCount: [],
      monthCount: [],
      loading: false,
      yearOption: {
        columnBtn: false,
        searchBtn: false,
        addBtn: false,
        menu: false,
        refreshBtn: false,
        column: [
          { label: "年份", prop: "name" },
          { label: "男", prop: "boy", sortable: true },
          { label: "女", prop: "girl", sortable: true },
          { label: "总计", prop: "all", sortable: true },
        ],
      },
      monthOption: {
        columnBtn: false,
        searchBtn: false,
        refreshBtn: false,
        addBtn: false,
        menu: false,
        column: [
          { label: "年份", prop: "name" },
          { label: "男", prop: "boy", sortable: true },
          { label: "女", prop: "girl", sortable: true },
          { label: "总计", prop: "all", sortable: true },
        ],
      },
    };
  },
  methods: {
    onLoad(page, params = {}) {
      this.loading = true;
      allCount().then((res) => {
        let data = res.data.data;
        this.yearCount.push(
          data.jnCount,
          data.snCount,
          data.ssnCount,
          data.zjCount
        );
        this.monthCount = data.monthCounts;
        this.loading = false;
      });
    },
  },
};
</script>

<style>
</style>

<template>
  <basic-container class="personCount">
    <div class="ctitle">
      <span>根据日期查询编辑业绩</span>
    </div>
    <avue-crud
      :data="personCount"
      :option="personOption"
      :table-loading="loading"
      @on-load="onLoad"
      @search-change="searchChange"
      @search-reset="searchReset"
    ></avue-crud>
  </basic-container>
</template>

<script>
import { personalCount } from "@/api/customer/customer";
import { getDate } from "@/util/util";
let date = getDate();

let startTime = `${date.year}-${date.newMonth}-01 00:00:00`;
let endTime = `${date.year}-${date.newMonth}-${date.newDay} 23:59:59`;

export default {
  name: "allCount",
  data() {
    return {
      query: { dateTimeRange: [startTime, endTime] },
      loading: false,
      personCount: [],
      personOption: {
        menu: false,
        refreshBtn: false,
        addBtn: false,
        columnBtn: false,
        menu: false,
        column: [
          { label: "编辑", prop: "name" },
          { label: "男", prop: "boy", sortable: true },
          { label: "女", prop: "girl", sortable: true },
          { label: "总计", prop: "all", sortable: true },
          {
            label: "起始时间",
            prop: "dateTimeRange",
            hide: true,
            search: true,
            searchSpan: 10,
            type: "datetimerange",
            searchRange: true,
            searchClearable: false,
            valueFormat: "yyyy-MM-dd HH:mm:ss",
            searchValue: [startTime, endTime],
          },
        ],
      },
    };
  },
  methods: {
    personalCount() {
      personalCount().then((res) => {
        this.personCount = res.data.data;
      });
    },
    searchReset() {
      this.query = { dateTimeRange: ["", ""] };
      this.onLoad(this.page);
    },
    searchChange(params, done) {
      this.query = params;
      this.onLoad(this.page, params);
      done();
    },
    onLoad(page, params = {}) {
      this.loading = true;
      let dateTimeRange = this.query.dateTimeRange;
      let obj = {
        startDate: dateTimeRange[0],
        endDate: dateTimeRange[1],
      };
      personalCount(Object.assign(params, obj)).then((res) => {
        this.personCount = res.data.data;
        this.loading = false;
      });
    },
  },
};
</script>

<style>
.personCount .avue-crud__right {
  display: none;
}
.personCount .avue-crud__menu {
  display: none;
}
.personCount .avue-form__menu {
  width: 25%;
}
</style>

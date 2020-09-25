<template>
  <div>
    <Form :inline="true" :label-width="80">
      <Form-item label="状态">
        <Select v-model="mapType" placeholder="请选择">
          <Option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </Select>
      </Form-item>
      <Form-item label="日期">
        <Date-picker
          v-model="planStartTime"
          value-format="yyyy-MM-dd"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions2"
        />
      </Form-item>
      <Form-item label="咨询人电话">
        <Input v-model="buyerMobile" placeholder="请输入电话" clearable></Input>
      </Form-item>
      <FormItem>
        <Button type="primary" @click="onSearch">搜索</Button>
      </FormItem>
      <Table
        :columns="columns1"
        ref="singleTable"
        :data="tableData"
        highlight-current-row
        style="width: 100%"
      >
        <template slot-scope="{ row, index }" slot="status">
          <span>{{ formatMapStatus(row.status) }}</span>
        </template>
        <template slot-scope="{ row, index }" slot="action">
          <Button type="primary" size="small" style="margin-right: 5px" @click="onEdit(index)">编辑</Button>
          <!-- <Button type="error" size="small" style="margin-right: 5px" @click="onMakeOrder(index)">推单</Button> -->
        </template>
      </Table>
      <Page
        :current="currentPage"
        :total="totalPage"
        @on-change="onPageChange"
        show-elevator
        size="small"
        show-total
      ></Page>
    </Form>
    <Modal
      v-model="popShow"
      title="修改搜索记录"
      :visible="true"
      :close-on-click-modal="false"
      width="30%"
      :closable="false"
      :mask-closable="false"
      @close="onClose(false)"
    >
      <Form :label-width="70" ref="formFields" :model="form" :rules="rulesRight">
        <Form-item label="咨询人" prop>
          <Input v-model="form.buyerName" disabled clearable />
        </Form-item>
        <Form-item label="咨询电话" prop>
          <Input v-model="form.buyerMobile" disabled clearable />
        </Form-item>
        <Form-item label="状态" prop>
          <Select v-model="form.status" placeholder="请选择">
            <Option
              v-for="item in optionsStatus"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="备注" prop="note">
          <Input v-model="form.note" placeholder="请输入备注" clearable />
        </Form-item>
      </Form>
      <div slot="footer" class="dialog-footer">
        <Button type="primary" @click="onSave(true)">保 存</Button>
        <Button @click="onClose(false)">取 消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import { query, update } from "@/api/search";

export default {
  name: "PagePermission",
  data() {
    return {
      rulesRight: {
        note: [{ required: true, message: "请输入", trigger: "blur" }],
      },
      columns1: [
        { title: "查询商标", key: "searchWord" },
        { title: "咨询人", key: "buyerName" },
        { title: "咨询人手机", key: "buyerMobile" },
        { title: "状态", key: "status", slot: "status", width: 100 },
        // {title: '预付款', key: 'prePay', width: 80},
        { title: "成功率%", key: "success", width: 100 },
        { title: "注册号", key: "registNo" },
        { title: "备注", key: "note" },
        { title: "创建时间", key: "createTime", width: 150 },
        { title: "更新时间", key: "updateTime", width: 150 },
        { title: "操作", slot: "action", width: 150, align: "center" },
      ],
      mapType: 0,
      options: [
        {
          value: 0,
          label: "全部",
        },
        {
          value: 1,
          label: "发起搜索",
        },
        {
          value: 2,
          label: "完成搜索",
        },
      ],
      optionsStatus: [
        {
          value: 1,
          label: "发起搜索",
        },
        {
          value: 2,
          label: "完成搜索",
        },
      ],
      openType: "add",
      form: {
        id: "",
        buyerMobile: "",
        buyerName: "",
        note: "",
        status: "",
      },
      planStartTime: [],
      buyerMobile: "",
      pickerOptions2: {
        shortcuts: [
          {
            text: "最近一周",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近一个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: "最近三个月",
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      mobile: "",
      tableData: [],
      currentRow: null,
      currentPage: 1,
      popShow: false,
      popNewOrderShow: false,
      currentIndex: null,
      pageSize: 10,
      totalPage: 1,
    };
  },
  computed: {
    formQuery() {
      return {
        pageIndex: this.currentPage,
        pageSize: this.pageSize,
        mobile: this.mobile,
        buyerMobile: this.buyerMobile,
        status: this.mapType,
        dateRange: this.planStartTime,
      };
    },
    formQueryUpdate() {
      return {
        id: this.form.id,
        note: this.form.note,
        status: this.form.status,
      };
    },
  },
  created() {
    this.reqList();
  },
  methods: {
    formClear() {
      return {
        id: "",
        buyerMobile: "",
        buyerName: "",
        note: "",
        status: "",
      };
    },
    onMakeOrder(index) {
      const item = this.tableData[index];
      if (item.buyerMobile !== null && item.buyerMobile !== "") {
        this.$router
          .push({
            name: "MakeOrder",
            query: {
              buyerId: item.id,
              buyerName: item.buyerName,
              buyerMobile: item.buyerMobile,
            },
          })
          .catch((err) => {
            err;
          });
      } else {
        this.$Notice.error({
          title: "咨询人手机不能为空",
        });
      }
    },
    formatMapStatus(row) {
      return row === 1 ? "发起搜索" : "回复搜索";
    },
    onSave(confirm) {
      this.$refs.formFields.validate((valid) => {
        if (valid) {
          if (this.openType === "edit") {
            this.reqEdit();
          } else {
          }
        } else {
          return false;
        }
      });
    },
    reqEdit() {
      update(this.formQueryUpdate).then((res) => {
        if (res.status === true) {
          this.onClose();
          this.reqList();
        } else {
          this.$Notice.error({
            title: "客户端请求错误",
          });
        }
      });
    },
    onEdit(index) {
      const item = this.tableData[index];
      this.openType = "edit";
      this.form.id = item.id;
      this.form.status = item.status;
      this.form.note = item.note;
      this.form.buyerName = item.buyerName;
      this.form.buyerMobile = item.buyerMobile;

      this.popShow = true;
    },
    onNewOrder(index) {
      const item = this.tableData[index];
      this.openType = "edit";
      this.form.id = item.id;
      this.form.openid = item.openid;
      this.form.mobile = item.mobile;
      this.form.buyerName = item.buyerName;
      this.form.buyerMobile = item.buyerMobile;
      this.form.idCard = item.idCard;

      this.currentIndex = index;
      this.popNewOrderShow = true;
    },
    onPageChange(page) {
      this.currentPage = page;
      this.reqList();
    },
    onSearch() {
      this.currentPage = 1
      this.reqList();
    },
    onClose(confirm) {
      this.popShow = false;
      // this.popNewOrderShow = false
      // if (confirm) this.reqFun(data);
      this.form = this.formClear;
    },
    reqList() {
      query(this.formQuery).then((res) => {
        console.log(res);
        if (res.status === true) {
          const rdata = res.data;
          this.tableData = rdata.list;
          this.totalPage = rdata.count;
        } else {
          this.$Notice.error({
            title: "客户端请求错误",
          });
        }
      });
    },
  },
};
</script>

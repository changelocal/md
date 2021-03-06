<template>
  <div>
    <Form :inline="true" :label-width="60">
      <Form-item label="状态">
        <Select v-model="status" placeholder="请选择">
          <Option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </Select>
      </Form-item>
      <Form-item label="类型">
        <Select v-model="kind" placeholder="请选择">
          <Option v-for="item in kinds" :key="item.value" :label="item.label" :value="item.value" />
        </Select>
      </Form-item>
      <Form-item label="日期">
        <DatePicker
          v-model="planStartTime"
          format="yyyy-MM-dd"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions2"
        />
      </Form-item>
      <Form-item label="订单号">
        <Input v-model="orderNo" placeholder="请输入订单号" clearable></Input>
      </Form-item>
      <Form-item label="买家手机">
        <Input v-model="userMobile" placeholder="请输入手机号" clearable></Input>
      </Form-item>
      <Form-item label>
        <Button type="primary" @click="onSearch">搜索</Button>
      </Form-item>
      <Table
        :columns="columns1"
        ref="singleTable"
        :data="tableData"
        highlight-current-row
        style="width: 100%"
      >
        <template slot-scope="{ row, index }" slot="status">
          <span>{{ formatMapStatus(row.orderStatus) }}</span>
        </template>
        <template slot-scope="{ row, index }" slot="type">
          <span>{{ formatMapType(row.OrderType) }}</span>
        </template>
        <template slot-scope="{ row, index }" slot="action">
          <Button type="primary" size="small" style="margin-right: 5px" @click="onEdit(index)">编辑</Button>
          <Button type="primary" size="small" style="margin-right: 5px" @click="onImage(index)">查看资料</Button>
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
      title="修改订单"
      :visible="true"
      :close-on-click-modal="false"
      width="30%"
      :closable="false"
      :mask-closable="false"
      @close="onClose(false)"
    >
      <Form :label-width="70" ref="formFields" :model="form" :rules="rulesRight">
        <Form-item label="订单号" prop>
          <Input v-model="form.orderNo" disabled placeholder clearable />
        </Form-item>
        <Form-item label="类型" prop>
          <Input v-model="form.orderTypeName" disabled placeholder clearable />
        </Form-item>
        <Form-item label="状态" prop="status">
          <Select v-model="form.status" placeholder="请选择">
            <Option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="预付款" prop="prePay">
          <InputNumber
            v-model="form.prePay"
            :min="0"
            :max="99999"
            controls-position="right"
            placeholder="请输入预付款"
            clearable
          />
        </Form-item>
        <Form-item label="剩余款" prop="restPay">
          <InputNumber
            v-model="form.restPay"
            :min="0"
            :max="99999"
            controls-position="right"
            placeholder="请输入剩余款"
            clearable
          />
        </Form-item>
        <Form-item label="共付款" prop="totalPay">
          <InputNumber
            v-model="form.totalPay"
            :min="0"
            :max="99999"
            controls-position="right"
            placeholder="请输入共付款"
            clearable
          />
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
import { query, update } from "@/api/order";

export default {
  name: "PagePermission",

  data() {
    return {
      rulesRight: {
        prePay: [{ required: true, message: "请输入" }],
        restPay: [{ required: true, message: "请输入" }],
        totalPay: [{ required: true, message: "请输入" }],
        status: [{ required: true, message: "请输入" }],
      },
      columns1: [
        { title: "订单号", key: "orderNo", width: 130 },
        { title: "状态", key: "statusName" , width: 100},
        { title: "订单类型", key: "orderTypeName" , width: 120},
        { title: "产品名称", key: "productName" },
        // { title: "商标类型", key: "categoryName" },
        { title: "预付款", key: "prePay", width: 80 },
        { title: "剩余付款", key: "restPay", width: 90 },
        { title: "共付款", key: "totalPay", width: 80 },
        { title: "买家手机", key: "userMobile" , width: 120 },
        { title: "销售", key: "opUserName" },
        { title: "创建时间", key: "createTime", width: 150 },
        { title: "操作", slot: "action", width: 170, align: "center" },
      ],
      openType: "add",
      orderNo: "",
      userMobile: "",
      form: {
        no: "",
        status: "",
        type: "",
        prePay: 0,
        restPay: 0,
        totalPay: 0,
        buyer: "",
        orderTypeName: "",
        productNo: "",
      },
      options: [
        {
          value: 0,
          label: "全部",
        },
        {
          value: 1,
          label: "待支付定金",
        },
        {
          value: 2,
          label: "待提交资料",
        },
        {
          value: 3,
          label: "委托受理",
        },
        {
          value: 4,
          label: "待支付尾款",
        },
        {
          value: 5,
          label: "已完成",
        },
        {
          value: 9,
          label: "已作废",
        },
      ],
      kinds: [
        {
          value: 0,
          label: "全部",
        },
        {
          value: 1,
          label: "商标注册",
        },
        {
          value: 2,
          label: "商标维权",
        },
        {
          value: 3,
          label: "商标信息变更",
        },
      ],
      planStartTime: [],
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
      status: 0,
      kind: 0,
      name: "",
      tableData: [],
      currentRow: null,
      currentPage: 1,
      popShow: false,
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
        orderStatus: this.status,
        orderType: this.kind,
        orderNo: this.orderNo,
        userMobile: this.userMobile,
        dateRange: this.planStartTime,
      };
    },
    formQueryUpdate() {
      return {
        id: this.form.id,
        status: this.form.status,
        prePay: this.form.prePay,
        restPay: this.form.restPay,
        totalPay: this.form.totalPay,
        productNo: this.form.productNo,
      };
    },
  },
  onAdd() {
    this.openType = "add";
    this.popShow = true;
  },
  created() {
    this.reqList();
  },
  methods: {
    onSave(confirm) {
      this.$refs.formFields.validate((valid) => {
        if (valid) {
          if (this.openType === "edit") {
            this.reqEdit();
          }
        } else {
          return false;
        }
      });

    },
    reqEdit() {
      update(this.formQueryUpdate).then((res) => {
        if (res.status === true) {
          this.close();
          this.reqList();
        } else {
          this.$Notice.error({
            title: "客户端请求错误",
          });
        }
      });
    },
    close() {
      this.popShow = false;
      this.form = this.formClear();
    },
    onImage(index) {
      const item = this.tableData[index];
      this.$router
        .push({
          name: "Image",
          query: {
            orderId: item.id,
          },
        })
        .catch((err) => {
          err;
        });
    },
    formatMapStatus(row) {
      if (row === 1) {
        return "待支付定金";
      } else if (row === 2) {
        return "待提交资料";
      } else if (row === 3) {
        return "委托受理";
      } else if (row === 4) {
        return "待支付尾款";
      } else if (row === 5) {
        return "已完成";
      } else if (row === 9) {
        return "已作废";
      }
    },
    formatMapType(row) {
      return row === 1 ? "商标注册" : row === 2 ? "商标维权" : "商标信息变更";
    },
    onEdit(index) {
      const item = this.tableData[index];
      this.openType = "edit";
      this.form.id = item.id;
      this.form.orderNo = item.orderNo;
      this.form.status = item.status;
      this.form.prePay = item.prePay;
      this.form.restPay = item.restPay;
      this.form.totalPay = item.totalPay;
      this.form.orderTypeName = item.orderTypeName;
      this.form.productNo = item.productNo;

      this.popShow = true;
    },
    onClose(confirm) {
      this.popShow = false;
      this.form = this.formClear();
      this.reqList();
    },
    onPageChange(page) {
      this.currentPage = page;
      this.reqList();
    },
    onSearch() {
      this.currentPage = 1
      this.reqList();
    },
    reqList() {
      query(this.formQuery).then((res) => {
        console.log(res);
        if (res.status === true) {
          const rdata = res.data;
          this.tableData = rdata.list;
          this.totalPage = rdata.total;
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

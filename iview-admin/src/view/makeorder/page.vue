<template>
  <div>
    <Form :inline="true" :label-width="60">
      <row>
        <Form-item label="商标分类">
          <Select v-model="brandClass" placeholder="请选择">
            <Option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="字符长度">
          <Select v-model="charLength" placeholder="请选择">
            <Option
              v-for="item in optionsChar"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="商标类型">
          <Select v-model="brandType" placeholder="请选择">
            <Option
              v-for="item in optionsType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="出售价格">
          <Select v-model="brandPrice" placeholder="请选择">
            <Option
              v-for="item in optionsPrice"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="商标名字">
          <Input v-model="name" placeholder="请输入商标名字" clearable></Input>
        </Form-item>
        <Form-item label="商标编号">
          <Input v-model="brandId" placeholder="请输入商标编号" clearable></Input>
        </Form-item>
        <Form-item label>
          <Button type="primary" @click="onSearch">搜索</Button>
        </Form-item>
      </row>
      <row>
        <Form-item label="买家名字">
          <Input v-model="rBuyerName" disabled placeholder clearable></Input>
        </Form-item>
        <Form-item label="买家手机">
          <Input v-model="rBuyerMobile" disabled placeholder clearable></Input>
        </Form-item>
        <Form-item label>
          <Button type="primary" @click="onEdit()">生成订单</Button>
        </Form-item>
      </row>
      <Table
        @on-selection-change="onSelectChange"
        :columns="columns1"
        ref="singleTable"
        :data="tableData"
        highlight-current-row
        style="width: 100%"
      >
        <template slot-scope="{ row, index }" slot="isQuality">
          <span>{{ formatMapisQuality(row.isQuality) }}</span>
        </template>
        <template slot-scope="{ row, index }" slot="isEnable">
          <span>{{ formatMapisEnable(row.isEnable) }}</span>
        </template>
        <template slot-scope="{ row, index }" slot="price">
          <span>{{ formatMapPrice(row) }}</span>
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
      title="推单"
      :visible="true"
      :close-on-click-modal="false"
      width="30%"
      :closable="false"
      :mask-closable="false"
      @close="onClose(false)"
    >
      <Form :label-width="80" ref="formFields" :model="form" :rules="rulesRight">
        <Form-item label="买家名字" prop>
          <Input v-model="form.buyerName" disabled placeholder clearable />
        </Form-item>
        <Form-item label="买家手机" prop>
          <Input v-model="form.buyerMobile" disabled placeholder clearable />
        </Form-item>
        <Form-item label="商标名称" prop>
          <Input v-model="form.brandName" disabled placeholder clearable />
        </Form-item>
        <Form-item label="商标类型" prop>
          <Input v-model="form.brandType" disabled placeholder clearable />
        </Form-item>
        <Form-item label="定金" prop="prePay">
          <InputNumber
            :max="99999"
            :min="1"
            :step="1"
            v-model="form.prePay"
            placeholder="请输入"
            clearable
          />
        </Form-item>
        <Form-item label="剩余支付" prop="restPay">
          <InputNumber
            :max="99999"
            :min="1"
            :step="1"
            v-model="form.restPay"
            placeholder="请输入"
            clearable
          />
        </Form-item>
        <Form-item label="总价" prop="totalPay">
          <InputNumber
            :max="99999"
            :min="1"
            :step="1"
            v-model="form.totalPay"
            placeholder="请输入"
            clearable
          />
        </Form-item>
      </Form>
      <div slot="footer" class="dialog-footer">
        <Button type="primary" @click="onSave(true)">立即推单</Button>
        <Button @click="onClose(false)">取 消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import { query } from "@/api/brand";
import { push } from "@/api/order";
export default {
  name: "PagePermission",
  data() {
    return {
      rulesRight: {
        prePay: [{ required: true, message: "请输入" }],
        restPay: [{ required: true, message: "请输入" }],
        totalPay: [{ required: true, message: "请输入" }],
      },
      columns1: [
        { type: "selection", width: 60, align: "center" },
        { title: "编号", key: "brandId" },
        {
          title: "图片",
          key: "imageUrl",
          columns: {
            width: "50px",
          },
          render: (h, params) => {
            return h("div", [
              h("img", {
                attrs: {
                  src: params.row.imageUrl,
                },
                style: {
                  width: "100px",
                  height: "100px",
                },
              }),
            ]);
          },
        },
        { title: "类型", key: "categoryName" },
        { title: "名称", key: "brandName" },
        { title: "注册号", key: "mobile" },
        { title: "分类", key: "brandType" },
        { title: "最低价格", key: "price" },
        { title: "友商价格", key: "priceHigh" },
        // {title: '热门', key: 'isQuality', slot: 'isQuality'},
        // {title: '上架', key: 'isEnable', slot: 'isEnable'},
        // {title: '操作', slot: 'action', width: 150, align: 'center'}
      ],
      brandClass: 0,
      brandPrice: 1,
      charLength: 0,
      brandType: 0,
      brandId:"",
      openType: "add",
      form: {
        buyerId: "",
        buyerName: "",
        buyerMobile: "",
        brandName: "",
        brandType: "",
        brandTypeId: "",
        img: "",
        prePay: 0,
        restPay: 0,
        totalPay: 0,
        brandIds: [],
      },
      options: [
        { value: 0, label: "不限" },
        { label: "化学原料", value: 1 },
        { label: "颜料油漆", value: 2 },
        { label: "日化用品", value: 3 },
        { label: "燃料油脂", value: 4 },
        { label: "医药	", value: 5 },
        { label: "金属材料", value: 6 },
        { label: "机械设备", value: 7 },
        { label: "手工器械", value: 8 },
        { label: "科学仪器", value: 9 },
        { label: "医疗器械", value: 10 },
        { label: "灯具空调", value: 11 },
        { label: "运输工具", value: 12 },
        { label: "军火烟火", value: 13 },
        { label: "珠宝钟表", value: 14 },
        { label: "乐器	", value: 15 },
        { label: "办公用品", value: 16 },
        { label: "橡胶制品", value: 17 },
        { label: "皮革皮具", value: 18 },
        { label: "建筑材料", value: 19 },
        { label: "家具	", value: 20 },
        { label: "厨房洁具", value: 21 },
        { label: "绳网袋篷", value: 22 },
        { label: "纱线丝	", value: 23 },
        { label: "布料床单", value: 24 },
        { label: "服装鞋帽", value: 25 },
        { label: "钮扣拉链", value: 26 },
        { label: "地毯席垫", value: 27 },
        { label: "健身器材", value: 28 },
        { label: "食品	", value: 29 },
        { label: "方便食品", value: 30 },
        { label: "饲料种籽", value: 31 },
        { label: "啤酒饮料", value: 32 },
        { label: "酒精饮品", value: 33 },
        { label: "烟草烟具", value: 34 },
        { label: "广告销售", value: 35 },
        { label: "金融物管", value: 36 },
        { label: "建筑修理", value: 37 },
        { label: "通讯服务", value: 38 },
        { label: "运输贮藏", value: 39 },
        { label: "材料加工", value: 40 },
        { label: "教育娱乐", value: 41 },
        { label: "网站服务", value: 42 },
        { label: "餐饮住宿", value: 43 },
        { label: "医疗园艺", value: 44 },
        { label: "社会服务", value: 45 },
      ],
      optionsChar: [
        {
          value: 0,
          label: "不限",
        },
        {
          value: 1,
          label: "1-2字",
        },
        {
          value: 2,
          label: "3字",
        },
        {
          value: 3,
          label: "4字",
        },
        {
          value: 4,
          label: "5字",
        },
        {
          value: 5,
          label: "5字以上",
        },
      ],
      optionsPrice: [
        {
          value: 1,
          label: "不限",
        },
        {
          value: 2,
          label: "1万以下",
        },
        {
          value: 3,
          label: "1-2万",
        },
        {
          value: 4,
          label: "2-5万",
        },
        {
          value: 5,
          label: "5万以上",
        },
      ],
      optionsType: [
        {
          value: 0,
          label: "不限",
        },
        {
          value: 1,
          label: "中文",
        },
        {
          value: 2,
          label: "字母",
        },
        {
          value: 3,
          label: "中文+字母",
        },
        {
          value: 4,
          label: "图形",
        },
        {
          value: 5,
          label: "中文+图形",
        },
        {
          value: 6,
          label: "字母+图形",
        },
        {
          value: 7,
          label: "中文+字母+图形",
        },
        {
          value: 8,
          label: "数字",
        },
        {
          value: 9,
          label: "字母+数字",
        },
      ],
      name: "",
      tableData: [],
      selectedItems: [],
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
        brandName: this.name,
        categoryNo: this.brandClass,
        priceType: this.brandPrice,
        unionType: this.brandType,
        brandSize: this.charLength,
        brandId: this.brandId,
      };
    },
    formQueryUpdate() {
      return {
        userId: this.form.buyerId,
        buyerName: this.form.buyerName,
        buyerMobile: this.form.buyerMobile,
        productNo: this.form.brandIds,
        productName: this.form.brandName,
        category: this.form.brandTypeId,
        categoryName: this.form.brandType,
        prePay: this.form.prePay,
        restPay: this.form.restPay,
        totalPay: this.form.totalPay,
        img: this.form.img,
      };
    },
    rBuyerId() {
      return this.$route.query.buyerId;
    },
    rBuyerName() {
      return this.$route.query.buyerName;
    },
    rBuyerMobile() {
      return this.$route.query.buyerMobile;
    },
  },

  created() {
    // this.reqList()
  },
  methods: {
    onSelectChange(list) {
      this.selectedItems = list;
    },
    onSave(confirm) {
      this.$refs.formFields.validate((valid) => {
        if (valid) {
          this.reqMakeOrder();
        } else {
          return false;
        }
      });
    },
    reqMakeOrder() {
      push(this.formQueryUpdate).then((res) => {
        console.log(res);
        if (res.status === true) {
          this.onClose();
          this.reqList();
          this.$Notice.success({
            title: "订单推送成功，请在订单列表里查看",
          });
        } else {
            this.$Notice.error({
              title: '客户端请求错误',
            })
        }
      });
    },
    formatMapisEnable(row) {
      return row === 2 ? "是" : "否";
    },
    formatMapisQuality(row) {
      return row === 2 ? "是" : "否";
    },
    formatMapPrice(row) {
      return row.price + "~" + row.priceHigh;
    },
    onAdd() {
      this.openType = "add";
      this.popShow = true;
    },
    onEdit() {
      if (this.selectedItems.length <= 0) {
        this.$Notice.error({
          title: "没有选择任何商标",
        });
      } else if (this.selectedItems.length > 1) {
        this.$Notice.error({
          title: "只能选择一个商标",
        });
      } else {
        console.log(this.selectedItems);
        // const item = this.tableData[index]
        this.openType = "edit";
        this.form.buyerId = this.rBuyerId;
        this.form.buyerName = this.rBuyerName;
        this.form.buyerMobile = this.rBuyerMobile;
        this.form.brandName = this.selectedItems[0].brandName;
        this.form.brandIds = this.selectedItems[0].brandId;
        this.form.img = this.selectedItems[0].imageUrl;

        this.selectedItems.forEach((p) => {
          this.form.prePay = p.priceHigh-p.price;
          this.form.totalPay = p.priceHigh;
          this.form.brandType = p.categoryName;
          this.form.brandTypeId = p.category;
        });

        this.form.restPay = this.form.totalPay - this.form.prePay;
        this.popShow = true;
      }
    },
    onPageChange(page) {
      this.currentPage = page;
      this.reqList();
    },
    onClose(confirm) {
      this.popShow = false;
      this.popNewOrderShow = false;
      // if (confirm) this.reqFun(data);
      this.form = this.formClear();
    },
    formClear() {
      return {
        id: "",
        buyerId: "",
        buyerName: "",
        buyerMobile: "",
        brandName: "",
        prePay: 0,
        restPay: 0,
        totalPay: 0,
        brandIds: [],
      };
    },
    onSearch() {
      if (this.name === "") {
        this.$Notice.warning({
          title: "请输入商标名称",
        });
      } else {
        this.reqList();
      }
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

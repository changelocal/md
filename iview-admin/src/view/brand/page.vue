<template>
  <div>
    <Form :inline="true" :label-width="60">
      <Form-item label="商标分类">
        <Select v-model="brandClass" @on-change="brandClassChange" placeholder="请选择">
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
      <Form-item label="名典编号">
        <Input v-model="brandId" placeholder="请输入名典编号" clearable></Input>
      </Form-item>
      <Form-item label="注册号">
        <Input v-model="regNo" placeholder="请输入注册号" clearable></Input>
      </Form-item>
      <Form-item label="子分类">
      <span v-for="(item, index) in subClass" v-bind:key="index" style="margin-right:20px;">
            <checkbox v-model="checked[item.id]">{{item.name}}</checkbox>
          </span>
      </Form-item>
      <Form-item label>
        <Button type="primary" @click="onSearch">搜索</Button>
      </Form-item>
      <Form-item label>
        <Button type="primary" @click="onAdd">添加商标</Button>
      </Form-item>
      <Table
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
        <template slot-scope="{ row, index }" slot="action">
          <Button type="primary" size="small" style="margin-right: 5px" @click="onEdit(index)">编辑</Button>
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
    <!--    <add v-if="popShow" :open-type="openType" :form-data="form" :handle-close="onClose" />-->
    <Modal
      v-model="popShow"
      title="商标编辑"
      :visible="true"
      :close-on-click-modal="false"
      width="30%"
      :closable="false"
      :mask-closable="false"
      @close="onClose(false)"
    >
      <Form :label-width="70" ref="formFields" :model="form">
        <Form-item label="编号" prop>
          <Input v-model="form.brandId" disabled placeholder="请输入" clearable />
        </Form-item>
        <Form-item label="名称" prop>
          <Input v-model="form.brandName" disabled placeholder="请输入" clearable />
        </Form-item>
        <Form-item label="最低价格" prop="price">
          <InputNumber
            :max="99999"
            :min="1"
            :step="1"
            v-model="form.price"
            placeholder="请输入"
            clearable
          />
        </Form-item>
        <Form-item label="最高价格" prop="priceHigh">
          <InputNumber
            :max="99999"
            :min="1"
            :step="1"
            v-model="form.priceHigh"
            placeholder="请输入"
            clearable
          />
        </Form-item>
        <Form-item label="热门">
          <i-switch v-model="form.isQuality" />
        </Form-item>
        <Form-item label="下架/上架">
          <i-switch v-model="form.isEnable" />
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
import { query, update } from "@/api/brand";
import {  subClass } from "@/api/common";
export default {
  name: "PagePermission",
  data() {
    return {
      subClass: [],
      checked: {},
      columns1: [
        { title: "名典编号", key: "brandId" },
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
        { title: "类型", key: "category" },
        { title: "名称", key: "brandName" },
        { title: "注册号", key: "regNo" },
        { title: "分类", key: "brandType" },
        { title: "适合项目", key: "fitProjects" , width: 150},
        { title: "价格区间", key: "price", slot: "price" },
        { title: "热门", key: "isQuality", slot: "isQuality" , width: 60},
        { title: "上架", key: "isEnable", slot: "isEnable", width: 60 },
        { title: "操作", slot: "action", width: 150, align: "center" },
      ],
      brandClass: 0,
      brandPrice: 1,
      charLength: 0,
      brandType: 0,
      openType: "add",
      form: {
        id: "",
        brandName: "",
        brandId: "",
        isEnable: false,
        isQuality: false,
        price: 0,
        priceHigh: 0,
      },
      options: [
        { value: 0, label: "不限" },
        { label: "1-化学原料", value: 1 },
        { label: "2-颜料油漆", value: 2 },
        { label: "3-日化用品", value: 3 },
        { label: "4-燃料油脂", value: 4 },
        { label: "5-医药	", value: 5 },
        { label: "6-金属材料", value: 6 },
        { label: "7-机械设备", value: 7 },
        { label: "8-手工器械", value: 8 },
        { label: "9-科学仪器", value: 9 },
        { label: "10-医疗器械", value: 10 },
        { label: "11-灯具空调", value: 11 },
        { label: "12-运输工具", value: 12 },
        { label: "13-军火烟火", value: 13 },
        { label: "14-珠宝钟表", value: 14 },
        { label: "15-乐器	", value: 15 },
        { label: "16-办公用品", value: 16 },
        { label: "17-橡胶制品", value: 17 },
        { label: "18-皮革皮具", value: 18 },
        { label: "19-建筑材料", value: 19 },
        { label: "20-家具	", value: 20 },
        { label: "21-厨房洁具", value: 21 },
        { label: "22-绳网袋篷", value: 22 },
        { label: "23-纱线丝	", value: 23 },
        { label: "24-布料床单", value: 24 },
        { label: "25-服装鞋帽", value: 25 },
        { label: "26-钮扣拉链", value: 26 },
        { label: "27-地毯席垫", value: 27 },
        { label: "28-健身器材", value: 28 },
        { label: "29-食品	", value: 29 },
        { label: "30-方便食品", value: 30 },
        { label: "31-饲料种籽", value: 31 },
        { label: "32-啤酒饮料", value: 32 },
        { label: "33-酒精饮品", value: 33 },
        { label: "34-烟草烟具", value: 34 },
        { label: "35-广告销售", value: 35 },
        { label: "36-金融物管", value: 36 },
        { label: "37-建筑修理", value: 37 },
        { label: "38-通讯服务", value: 38 },
        { label: "39-运输贮藏", value: 39 },
        { label: "40-材料加工", value: 40 },
        { label: "41-教育娱乐", value: 41 },
        { label: "42-网站服务", value: 42 },
        { label: "43-餐饮住宿", value: 43 },
        { label: "44-医疗园艺", value: 44 },
        { label: "45-社会服务", value: 45 },
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
      brandId: "",
      regNo: "",
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
      this.checked.
      return {
        pageIndex: this.currentPage,
        pageSize: this.pageSize,
        brandName: this.name,
        brandId: this.brandId,
        regNo: this.regNo,
        categoryNo: this.brandClass,
        priceType: this.brandPrice,
        unionType: this.brandType,
        brandSize: this.charLength,
        groups:
      };
    },
    formQueryUpdate() {
      return {
        id: this.form.id,
        note: this.form.note,
        buyerName: this.form.type,
        buyerMobile: this.form.email,
        isEnable: this.form.isEnable ? 2 : 1,
        isQuality: this.form.isQuality ? 2 : 1,
      };
    },
  },

  created() {
    this.reqList();
  },
  methods: {
    brandClassChange(code) {
      subClass(code).then((res) => {
        console.log(res);
        if (res.status === true) {
          this.subClass = res.data.brandClasses
        } else {
          this.$Notice.error({
            title: "客户端请求错误",
          });
        }
      });
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
        console.log(res);
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
    onEdit(index) {
      const item = this.tableData[index];
      this.openType = "edit";
      this.form.id = item.id;
      this.form.brandName = item.brandName;
      this.form.brandId = item.brandId;
      this.form.isEnable = item.isEnable === 2 ? true : false;
      this.form.isQuality = item.isQuality === 2 ? true : false;
      this.form.price = item.price;
      this.form.priceHigh = item.priceHigh;

      this.popShow = true;
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
    onSearch() {
      console.log(this.checked)
      this.currentPage = 1
      this.reqList();
    },
    reqList() {
      query(this.formQuery).then((res) => {
        console.log(res);
        if (res.status === true) {
          const rdata = res.data;
          this.tableData = rdata.list;
          this.totalPage = rdata.count;
          // this.currentPage = rdata.currentPage
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

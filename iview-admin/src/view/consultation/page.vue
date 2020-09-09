<template>
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
        <Form-item label="咨询电话">
          <Input v-model="mobile" placeholder="请输入电话" clearable>
            <Button type="primary" slot="append"  @click="onSearch" />
          </Input>
        </Form-item>
        <Table :columns="columns1" ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
          <!--        <el-table-column property="orderNo" label="咨询订单" width="200px" />-->
          <!--        <el-table-column property="openId" label="咨询人" width="100px" />-->
          <!--        <el-table-column property="buyerMobile" label="咨询人电话" width="150px" />-->
          <!--        <el-table-column property="status" label="状态" width="100px" />-->
          <!--        <el-table-column property="prePay" label="预付款" />-->
          <!--        <el-table-column property="opUserName" label="销售" />-->
          <!--        <el-table-column property="opUserMobile" label="销售电话" />-->
          <!--        <el-table-column property="note" label="备注" />-->
          <!--        <el-table-column property="createTime" label="创建时间" width="150px" />-->
          <!--        <el-table-column label="操作" fixed="right" width="100px">-->
          <!--          <template slot-scope="scope">-->
          <!--            <el-button type="text" @click="onEdit(scope.$index)">编辑</el-button>-->
          <!--            <el-button type="text" @click="onNewOrder(scope.$index)">推单</el-button>-->
          <!--          </template>-->
          <!--        </el-table-column>-->
        </Table>
      </Form>


<!--    <add v-if="popShow" :open-type="openType" :form-data="form" :handle-close="onClose" />-->
<!--    <new-order v-if="popNewOrderShow" :open-type="openType" :form-data="form" :handle-close="onClose" />-->

</template>

<script>
import {query} from '@/api/consultation'
import Add from './add'
import NewOrder from './newOrder'
export default {
  name: 'PagePermission',
  components: {
    Add,
    NewOrder
  },
  data() {
    return {
      columns1: [
        {title: '咨询订单', key: 'orderNo'},
        {title: '咨询人', key: 'openId'},
        {title: '咨询人手机', key: 'buyerMobile'},
        {title: '状态', key: 'status'},
        {title: '预付款', key: 'prePay'},
        {title: '销售姓名', key: 'opUserName'},
        {title: '销售手机', key: 'opUserMobile'},
        {title: '备注', key: 'note'},
        {title: '创建时间', key: 'createTime'},
        {title: '操作', slot: 'action', width: 150, align: 'center'}
      ],
      mapType: 0,
      options: [{
        value: 0,
        label: '全部'
      }, {
        value: 1,
        label: '发起咨询'
      }, {
        value: 2,
        label: '完成咨询'
      }],
      openType: 'add',
      form: {
        id: '',
        nickName: '',
        realName: '',
        mobile: '',
        openid: '',
        idCard: ''
      },
      planStartTime: [],
      pickerOptions2: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      mobile: '',
      tableData: [{ buyerName: 'sxj', buyerMobile: '13813113131', status: '发起咨询' }],
      currentRow: null,
      currentPage: 1,
      popShow: false,
      popNewOrderShow: false,
      currentIndex: null,
      pageSize: 10,
      totalPage: 1
    }
  },
  computed: {
    formQuery() {
      return {
        pageIndex: this.currentPage,
        pageSize: this.pageSize,
        mobile: this.mobile
      }
    }
  },
  onAdd() {
    this.openType = 'add'
    this.popShow = true
  },
  created() {
    this.reqList()
  },
  methods: {
    onEdit(index) {
      const item = this.tableData[index]
      this.openType = 'edit'
      this.form.id = item.id
      this.form.openid = item.openid
      this.form.mobile = item.mobile
      this.form.buyerName = item.buyerName
      this.form.buyerMobile = item.buyerMobile
      this.form.idCard = item.idCard

      this.currentIndex = index
      this.popShow = true
    },
    onNewOrder(index) {
      const item = this.tableData[index]
      this.openType = 'edit'
      this.form.id = item.id
      this.form.openid = item.openid
      this.form.mobile = item.mobile
      this.form.buyerName = item.buyerName
      this.form.buyerMobile = item.buyerMobile
      this.form.idCard = item.idCard

      this.currentIndex = index
      this.popNewOrderShow = true
    },
    onPageChange(page) {
      this.currentPage = page
      this.reqList()
    },
    onSearch() {
      this.reqList()
    },
    onClose(data, confirm) {
      this.popShow = false
      this.popNewOrderShow = false
      // if (confirm) this.reqFun(data);
      this.form = this.formClear()
    },
    reqList() {
      query(this.formQuery).then(res => {
        console.log(res)
        if (res.status === true) {
          const rdata = res.data

          this.tableData = rdata.list
          this.totalPage = rdata.count
          // this.currentPage = rdata.currentPage

        }else {
          this.$notify({
            title: '错误',
            message: res.message,
            type: 'error',
            duration: 2000
          })
        }
      })
    },
  }
}
</script>

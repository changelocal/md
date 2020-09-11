<template>
  <div>
      <Form :inline="true"  :label-width="60">
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
            <Option
              v-for="item in kinds"
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
        <Form-item label="">
          <Button type="primary"  @click="onSearch" >搜索</Button>
        </Form-item>
        <Table :columns="columns1" ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
          <template slot-scope="{ row, index }" slot="status">
            <span >{{ formatMapStatus(row.orderStatus) }}</span>
          </template>
          <template slot-scope="{ row, index }" slot="type">
            <span >{{ formatMapType(row.OrderType) }}</span>
          </template>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="primary" size="small" style="margin-right: 5px" @click="onEdit(index)">编辑</Button>
          </template>
        </Table>
        <Page :current="currentPage" :total="totalPage" @on-change="onPageChange" show-elevator size="small" show-total></Page>

      </Form>
<!--    <add v-if="popShow" :open-type="openType" :form-data="form" :handle-close="onClose" />-->
  </div>
</template>

<script>
import {query} from '@/api/order'
import Add from './add'

export default {
  name: 'PagePermission',
  components: {
    Add
  },
  data() {
    return {
      columns1: [
        {title: '订单号', key: 'orderNo', width: 150},
        {title: '状态', key: 'orderStatus', slot: 'status'},
        {title: '订单类型', key: 'OrderType', slot: 'type'},
        {title: '商标类型', key: 'categoryName'},
        {title: '预付款', key: 'prePay', width: 80},
        {title: '剩余付款', key: 'restPay', width: 90},
        {title: '共付款', key: 'totalPay', width: 80},
        {title: '买家', key: 'userId'},
        {title: '销售', key: 'opUserId'},
        {title: '产品名称', key: 'productName'},
        {title: '创建时间', key: 'createTime', width: 150},
        {title: '操作', slot: 'action', width: 70, align: 'center'}
      ],
      openType: 'add',
      form: {
        no: '',
        status: '',
        type: '',
        prePay: 0,
        restPay: 0,
        totalPay: 0,
        buyer: ''
      },
      options: [{
        value: 0,
        label: '全部'
      }, {
        value: 1,
        label: '待支付定金'
      }, {
        value: 2,
        label: '待提交资料'
      }, {
        value: 3,
        label: '委托受理'
      }, {
        value: 4,
        label: '待支付尾款'
      }, {
        value: 5,
        label: '已完成'
      }],
      kinds: [{
        value: 0,
        label: '全部'
      }, {
        value: 1,
        label: '商标注册'
      }, {
        value: 2,
        label: '商标维权'
      }, {
        value: 3,
        label: '商标信息变更'
      }],
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
      status: 0,
      kind: 0,
      name: '',
      tableData: [],
      currentRow: null,
      currentPage: 1,
      popShow: false,
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
        OrderStatus: this.status,
        OrderType: this.kind
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
    formatMapStatus(row) {
       if(row === 1){
         return '待支付定金'
       }
       else if (row === 2){
         return '待提交资料'
       }
       else if (row === 3){
         return '委托受理'
       }
       else if (row === 4){
         return '待支付尾款'
       }
       else if (row === 5){
         return '已完成'
       }
    },
    formatMapType(row) {
      return row === 1 ? '商标注册' : row === 2 ?'商标维权' :"商标信息变更"
    },

    onAdd() {
      this.openType = 'add'
      this.popShow = true
    },
    onEdit(index) {
      const item = this.tableData[index]
      this.openType = 'edit'
      this.form.no = item.no
      this.form.status = item.status
      this.form.prePay = item.prePay
      this.form.restPay = item.restPay
      this.form.totalPay = item.totalPay
      this.form.enable = item.enable
      this.form.qqAccount = item.qqAccount

      this.currentIndex = index
      this.popShow = true
    },
    onClose(data, confirm) {
      this.popShow = false
      this.form = this.formClear()
      if (confirm && data.openType === 'edit') {
        // this.reqFun(data)
      }
      this.reqList()
    },
    onPageChange(page) {
      this.currentPage = page
      this.reqList()
    },
    onSearch() {
      this.reqList()
    },
    reqList() {
      query(this.formQuery).then(res => {
        console.log(res)
        if (res.status === true) {
          const rdata = res.data
          this.tableData = rdata.list
          this.totalPage = rdata.total
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

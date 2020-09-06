<template>
  <el-container style="height: 100%;">
    <el-main>
      <el-form :inline="true">
        <el-form-item label="状态">
          <el-select v-model="status" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="kind" placeholder="请选择">
            <el-option
              v-for="item in kinds"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
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
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" icon="el-icon-search" @click="onSearch" />
        </el-form-item>
      </el-form>
      <el-table ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
        <el-table-column property="no" label="订单号" width="150px" />
        <el-table-column property="status" label="状态" width="100px" />
        <el-table-column property="type" label="订单类型" width="150px" />
        <el-table-column property="prePay" label="预付款" />
        <el-table-column property="restPay" label="剩余付款" width="100px" />
        <el-table-column property="totalPay" label="共付款" width="100px" />
        <el-table-column property="buyer" label="买家" width="100px" />
        <el-table-column property="sales" label="销售" width="100px" />
        <el-table-column property="creatTime" label="创建时间" width="100px" />
        <el-table-column property="productNo" label="产品编号" width="100px" />
        <el-table-column label="操作" fixed="right" width="100px">
          <template slot-scope="scope">
            <el-button type="text" @click="onEdit(scope.$index)">编辑</el-button>
            <!--            <el-button @click="onDel(scope.$index)" type="text" >删除</el-button>-->
            <!--            <el-button @click="onViewResult(scope.$index)" type="text" >查看</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <div class="text-center">
        <el-pagination
          layout="prev, pager, next"
          :page-size="pageSize"
          :total="totalPage"
          :current-page="currentPage"
          hide-on-single-page
          @current-change="onPageChange"
        />
      </div>
    </el-main>
    <add v-if="popShow" :open-type="openType" :form-data="form" :handle-close="onClose" />
  </el-container>
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
      tableData: [{ no: 'sxj', buyer: 'boss', type: '123', status: '待提交资料', prePay: '222' }],
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
        currentPage: this.currentPage,
        pageSize: this.pageSize,
        name: this.name,
        type: this.type
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
      this.$http.post(API.orderQuery, this.formQuery).then(res => {
        if (res.status === 200) {
          const status = res.data.statusCode
          const rdata = res.data.data
          if (status === 200) {
            this.tableData = rdata.list
            this.totalPage = rdata.total
            this.currentPage = rdata.currentPage
          }
        }
      })
    }
  }
}
</script>

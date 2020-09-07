<template>
  <el-container style="height: 100%;">
    <el-main>
      <el-form :inline="true">
        <el-form-item label="状态">
          <el-select v-model="mapType" placeholder="请选择">
            <el-option
              v-for="item in options"
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
        <el-form-item label="咨询电话">
          <el-input v-model="mobile" placeholder="请输入电话" clearable>
            <el-button slot="append" icon="el-icon-search" @click="onSearch" />
          </el-input>
        </el-form-item>

      </el-form>
      <el-table ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
        <el-table-column property="orderNo" label="咨询订单" width="200px" />
        <el-table-column property="buyerName" label="咨询人" width="100px" />
        <el-table-column property="buyerMobile" label="咨询人电话" width="150px" />
        <el-table-column property="status" label="状态" width="100px" />
        <el-table-column property="prePay" label="预付款" />
        <el-table-column property="opUserName" label="销售" />
        <el-table-column property="opUserName" label="销售电话" />
        <el-table-column property="note" label="备注" />
        <el-table-column property="createTime" label="创建时间" width="150px" />
        <el-table-column label="操作" fixed="right" width="100px">
          <template slot-scope="scope">
            <el-button type="text" @click="onEdit(scope.$index)">编辑</el-button>
            <el-button type="text" @click="onNewOrder(scope.$index)">推单</el-button>
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
    <new-order v-if="popNewOrderShow" :open-type="openType" :form-data="form" :handle-close="onClose" />
  </el-container>
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
          this.currentPage = rdata.currentPage

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

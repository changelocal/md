<template>
  <el-container style="height: 100%;">
    <el-main>
      <el-form :inline="true">

        <el-form-item label="手机">
          <el-input v-model="mobile" placeholder="请输入手机" clearable>
            <el-button slot="append" icon="el-icon-search" @click="onSearch" />
          </el-input>
        </el-form-item>

      </el-form>
      <el-table ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
        <el-table-column property="minId" label="openID" width="200px" />
        <el-table-column property="nickName" label="昵称" width="100px" />
        <el-table-column property="realName" label="真实姓名" width="100px" />
        <el-table-column property="idCard" label="身份证" />
        <el-table-column property="mobile" label="手机" />
        <el-table-column property="address" label="地址" width="200px" />
        <el-table-column property="createTime" label="关注时间" width="150px" />
        <el-table-column label="操作" fixed="right" width="100px">
          <template slot-scope="scope">
            <el-button type="text" @click="onEdit(scope.$index)">编辑</el-button>
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
import {query} from '@/api/wxuser'
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
        id: '',
        nickName: '',
        realName: '',
        mobile: '',
        openid: '',
        idCard: ''
      },
      mobile: '',
      tableData: [{ openoid: '2234244', nickName: 'xj', mobile: '13713131313', openId: '123123213', idCard: '2132123', realName: 'sxj', address: 'sdfdsf' }],
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
      this.form.openid = item.openId
      this.form.mobile = item.mobile
      this.form.nickName = item.nickName
      this.form.realName = item.realName
      this.form.idCard = item.idCard

      this.currentIndex = index
      this.popShow = true
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

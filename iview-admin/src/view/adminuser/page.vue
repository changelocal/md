<template>

  <el-container style="height: 100%;">
    <el-main>
      <el-form :inline="true">
        <el-form-item label="账号种类">
          <select v-model="mapType" placeholder="请选择">
            <option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </select>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="name" placeholder="请输入姓名" clearable>
            <el-button slot="append" icon="el-icon-search" @click="onSearch" />
          </el-input>
        </el-form-item>
        <el-form-item label="">
          <el-button type="primary" icon="el-icon-circle-plus-outline" @click="onAdd">添加销售</el-button>
        </el-form-item>
      </el-form>
      <el-table ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
        <el-table-column property="nickname" label="姓名" width="100px" />
        <el-table-column property="account" label="账号" width="150px" />
        <el-table-column property="type" label="类型" width="100px" />
        <el-table-column property="email" label="邮件" />
        <el-table-column property="mobile" label="电话" width="150px" />
        <el-table-column property="title" label="头衔" width="100px" />
        <el-table-column property="qqAccount" label="QQ" width="150px" />
        <el-table-column property="enable" label="是否有效" width="100px" :formatter="formatMapType" />
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
import Add from './add'
import { query } from '@/api/adminuser'
export default {
  name: 'PagePermission',
  components: {
    Add
  },
  data() {
    return {
      mapType: 0,
      openType: 'add',
      form: {
        id: '',
        name: '',
        title: '',
        mobile: '',
        type: '',
        qqAccount: '',
        enable: true
      },
      options: [{
        value: 0,
        label: '全部'
      }, {
        value: 1,
        label: '管理员'
      }, {
        value: 2,
        label: '普通人员'
      }],
      name: '',
      tableData: [{ name: 'sxj', title: 'boss', mobile: '13412121212', type: 'admin', qqAccount: '212731', email: 'sxj@123.com' }],
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
        name: this.name,
        type: this.type
      }
    }
  },

  created() {
    this.reqList()
  },
  methods: {
    formatMapType(row) {
      return row.isEnable === 1 ? '有效' : '无效'
    },
    onAdd() {
      this.openType = 'add'
      this.popShow = true
    },
    onEdit(index) {
      const item = this.tableData[index]
      this.openType = 'edit'
      this.form.id = item.id
      this.form.name = item.name
      this.form.mobile = item.mobile
      this.form.type = item.type
      this.form.title = item.title
      this.form.enable = item.enable
      this.form.email = item.email
      this.form.qqAccount = item.qqAccount

      this.currentIndex = index
      this.popShow = true
    },
    handleRolesChange() {
      this.$router.push({ path: '/permission/index?' + +new Date() })
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
      this.form = this.formClear()
      if (confirm && data.openType === 'edit') {
        // this.reqFun(data)
      }
      this.reqList()
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
            title: 'Success',
            message: 'Created Successfully',
            type: 'success',
            duration: 2000
          })
        }
      })
    },
    // reqList() {
    //   this.$http.post(API.adminuserQuery, this.formQuery).then(res => {
    //     if (res.status === 200) {
    //       const status = res.data.statusCode
    //       const rdata = res.data.data
    //       if (status === 200) {
    //         this.tableData = rdata.list
    //         this.totalPage = rdata.total
    //         this.currentPage = rdata.currentPage
    //       }
    //     }
    //   })
    // }
  }
}
</script>

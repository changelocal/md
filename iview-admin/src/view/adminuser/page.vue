<template>
      <Form inline :label-width="80">
        <FormItem  label="账号种类">
          <Select v-model="mapType" placeholder="请选择">
            <Option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </FormItem >
        <FormItem  label="姓名">
          <Input v-model="name" placeholder="请输入姓名" clearable>
          </Input>
        </FormItem >
          <FormItem  label="">
            <Button type="primary"  @click="onSearch" >搜索</Button>
        </FormItem >
        <FormItem  label="">
          <Button type="primary"  @click="onAdd">添加销售</Button>
        </FormItem >
        <Table :columns="columns1" ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
          <template slot-scope="{ row, index }" slot="type">
            <span >{{ formatMapType(row.type) }}</span>
          </template>
          <template slot-scope="{ row, index }" slot="enable">
            <span >{{ formatMapEnable(row.enable) }}</span>
          </template>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="primary" size="small" style="margin-right: 5px" @click="onEdit(index)">编辑</Button>
          </template>
        </Table>
        <Page :total="100" />
        <!--    <add v-if="popShow" :open-type="openType" :form-data="form" :handle-close="onClose" />-->
      </Form>
</template>

<script>
// import Add from './add'
import { query } from '@/api/adminuser'
export default {
  name: 'PagePermission',
  // components: {
  //   Add
  // },
  data() {
    return {
      columns1: [
        {title: '姓名', key: 'nickname'},
        {title: '账号', key: 'account'},
        {title: '类型', key: 'type',slot: 'type'},
        {title: '邮件', key: 'email'},
        {title: '手机', key: 'mobile'},
        {title: '头衔', key: 'title'},
        {title: '地址', key: 'address'},
        {title: 'QQ', key: 'qqAccount'},
        {title: '是否有效', key: 'enable',slot: 'enable'},
        {title: '操作', slot: 'action', width: 150, align: 'center'}
      ],
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
        nickname: this.name,
        type: this.type
      }
    }
  },

  created() {
    this.reqList()
  },
  methods: {
    formatMapType(row) {
      return row === 1 ? '销售' : '老板'
    },
    formatMapEnable(row) {
      return row === 1 ? '有效' : '无效'
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

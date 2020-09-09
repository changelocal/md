<template>
      <Form :inline="true" :label-width="60">
        <Form-item label="手机">
          <Input v-model="mobile" placeholder="请输入手机" clearable>
          </Input>
        </Form-item>
        <Form-item label="">
          <Button type="primary"  @click="onSearch" >搜索</Button>
        </Form-item>
        <Table  :columns="columns1" ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
          <template slot-scope="{ row, index }" slot="action">
            <Button type="primary" size="small" style="margin-right: 5px" @click="onEdit(index)">编辑</Button>
          </template>
        </Table>
      </Form>
<!--    <add v-if="popShow" :open-type="openType" :form-data="form" :handle-close="onClose" />-->

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
      columns1: [
        {title: 'openID', key: 'minId'},
        {title: '昵称', key: 'nickName'},
        {title: '真实姓名', key: 'realName'},
        {title: '身份证', key: 'idCard'},
        {title: '手机', key: 'mobile'},
        {title: '地址', key: 'address'},
        {title: '关注时间', key: 'createTime'},
        {title: '操作', slot: 'action', width: 150, align: 'center'}
      ],
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

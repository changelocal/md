<template>
  <div>
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
        <Page :current="currentPage" :total="totalPage" @on-change="onPageChange" show-elevator size="small" show-total></Page>

      </Form>
    <Modal
      v-model="popShow"
      title="用户信息修改"
      :visible="true"
      :close-on-click-modal="false"
      width="30%"
      :closable="false"
      :mask-closable="false"
      @close="onClose(false)"
    >
      <Form  :label-width="80" ref="formFields" :model="form" :rules="rulesRight">
        <Form-item label="openid" prop="" >
          <Input v-model="form.minId" disabled clearable />
        </Form-item>
        <Form-item label="昵称" prop="" >
          <Input v-model="form.nickName" disabled clearable />
        </Form-item>
        <Form-item label="电话" prop="" >
          <Input v-model="form.mobile" disabled clearable />
        </Form-item>
        <Form-item label="真实姓名" prop="realName" >
          <Input v-model="form.realName" placeholder="请输入姓名" clearable />
        </Form-item>
        <Form-item label="身份证" prop="" >
          <Input v-model="form.idCard" placeholder="请输入身份证" clearable />
        </Form-item>

      </Form>
      <div slot="footer" class="dialog-footer">
        <Button type="primary" @click="onSave(true)">保 存</Button>
        <Button @click="onClose(false)">取 消</Button>
      </div>
    </Modal>
<!--    <add v-if="popShow" :open-type="openType" :form-data="form" :handle-close="onClose" />-->
  </div>
</template>

<script>
import {query,update} from '@/api/wxuser'
// import Add from './add'
export default {
  name: 'PagePermission',
  // components: {
  //   Add
  // },
  data() {
    return {
      rulesRight: {
        realName: [{ required: true, message: '请输入', trigger: 'blur' }],
        // mobile: [{ required: true, message: '请输入', trigger: 'blur' }],
        // idCard: [{ required: true, message: '请输入', trigger: 'blur' }]
      },
      columns1: [
        {title: 'openID', key: 'minId', width: 250},
        {title: '昵称', key: 'nickName'},
        {title: '真实姓名', key: 'realName', width: 100},
        {title: '身份证', key: 'idCard'},
        {title: '手机', key: 'mobile', width: 100},
        {title: '地址', key: 'address'},
        {title: '关注时间', key: 'createTime'},
        {title: '操作', slot: 'action', width: 70, align: 'center'}
      ],
      openType: 'add',
      form: {
        id: '',
        nickName: '',
        realName: '',
        mobile: '',
        minId: '',
        idCard: ''
      },
      mobile: '',
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
        mobile: this.mobile
      }
    },
    formQueryUpdate() {
      return {
        id: this.form.id,
        mobile: this.form.mobile,
        nickName: this.form.nickName,
        realName: this.form.realName,
        idCard: this.form.idCard,
      }
    }
  },
  created() {
    this.reqList()
  },
  methods: {
    onSave(confirm) {
      this.$refs.formFields.validate(valid => {
        if (valid) {
          if (this.openType === 'edit') {
            this.reqEdit()
          } else {

          }
        } else {
          return false
        }
      })
    },
    reqEdit() {
      update(this.formQueryUpdate).then(res => {
        console.log(res)
        if (res.status === true) {
          this.onClose()
          this.reqList()
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
    onEdit(index) {
      const item = this.tableData[index]
      console.log(item)
      this.openType = 'edit'
      this.form.id = item.id
      this.form.minId = item.minId
      this.form.mobile = item.mobile
      this.form.nickName = item.nickName
      this.form.realName = item.realName
      this.form.idCard = item.idCard

      // this.currentIndex = index
      this.popShow = true
    },
    onPageChange(page) {
      this.currentPage = page
      this.reqList()
    },
    onSearch() {
      this.reqList()
    },
    onClose( confirm) {
      this.popShow = false
      // if (confirm) this.reqFun(data);
      this.form = this.formClear()
    },
    formClear() {
      return {
        id: '',
        nickName: '',
        realName: '',
        mobile: '',
        minId: '',
        idCard: ''
      }
    },
    reqList() {
      query(this.formQuery).then(res => {
        if (res.status === true) {
          const rdata = res.data
          this.tableData = rdata.list
          this.totalPage = rdata.count
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

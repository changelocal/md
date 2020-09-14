<template>
  <div>
      <Form inline :label-width="60">
        <FormItem  label="账号种类">
          <Select v-model="type" placeholder="请选择">
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
            <span >{{ formatMapEnable(row.isEnable) }}</span>
          </template>
          <template slot-scope="{ row, index }" slot="action">
            <Button type="primary" size="small" style="margin-right: 5px" @click="onEdit(index)">编辑</Button>
          </template>
        </Table>
        <Page :current="currentPage" :total="totalPage" @on-change="onPageChange" show-elevator size="small" show-total></Page>
      </Form>

  <Modal
    v-model="popShow"
    title="销售管理"
    :visible="true"
    :close-on-click-modal="false"
    width="30%"
    :closable="false"
    :mask-closable="false"
    @close="onClose(false)"
  >
    <Form  :label-width="70" ref="formFields" :model="form" :rules="rulesRight">
      <Form-item label="姓名" prop="name" >
        <Input v-model="form.name" placeholder="请输入姓名" clearable />
      </Form-item>
      <Form-item label="账号" prop="account" >
        <Input v-model="form.account" placeholder="请输入账号" clearable />
      </Form-item>
      <Form-item label="密码" prop="password" >
        <Input v-model="form.password" type="password" placeholder="请输入账号" clearable />
      </Form-item>
      <Form-item label="重复密码" prop="password2" >
        <Input v-model="form.password2" type="password" placeholder="请输入账号" clearable />
      </Form-item>
      <Form-item label="电话" prop="mobile" >
        <Input v-model="form.mobile" placeholder="请输入电话" clearable />
      </Form-item>
      <Form-item label="头衔" prop="title" >
        <Input v-model="form.title" placeholder="请输入头衔" clearable />
      </Form-item>
      <Form-item label="邮件" prop="email" >
        <Input v-model="form.email" placeholder="请输入邮件" clearable />
      </Form-item>
      <Form-item label="QQ" prop="qqAccount" >
        <Input v-model="form.qqAccount" placeholder="请输入QQ" clearable />
      </Form-item>
      <Form-item label="权限" >
        <Radio-group v-model="form.type" >
          <Radio :label="2">管理员</Radio>
          <Radio :label="1">普通</Radio>
        </Radio-group>
      </Form-item>
      <Form-item label="禁用/启用" >
        <i-switch v-model="form.enable"/>
      </Form-item>
    </Form>
    <div slot="footer" class="dialog-footer">
      <Button type="primary"  @click="onSave(true)">保 存</Button>
      <Button @click="close()">取 消</Button>
    </div>
  </Modal>
  </div>
</template>

<script>

import  { query,add,update } from '@/api/adminuser'
export default {
  name: 'PagePermission',
  data() {
    return {
      rulesRight: {
        account: [{ required: true, message: '请输入', trigger: 'blur' }],
        password: [{ required: true, message: '请输入', trigger: 'blur' }],
        password2: [{ required: true, message: '请输入', trigger: 'blur' }],
        name: [{ required: true, message: '请输入', trigger: 'blur' }],
        mobile: [{ required: true, message: '请输入', trigger: 'blur' }],
        title: [{ required: true, message: '请输入', trigger: 'blur' }],
        qqAccount: [{ required: true, message: '请输入', trigger: 'blur' }],
        email: [{ required: true, message: '请输入', trigger: 'blur' }]
      },
      columns1: [
        {title: '姓名', key: 'nickname', width: 80},
        {title: '账号', key: 'account'},
        {title: '类型', key: 'type',slot: 'type', width: 80},
        {title: '邮件', key: 'email', width: 170},
        {title: '手机', key: 'mobile'},
        {title: '头衔', key: 'title'},
        {title: '头像', key: 'avatar',render: (h, params) => {
            return h('div', [
              h('img', {
                attrs: {
                  src: params.row.avatar
                },
                style: {
                  width: '50px',
                  height: '50px'
                }
              }),
            ]);
          }},
        {title: 'QQ', key: 'qqAccount'},
        {title: '是否有效', key: 'isEnable',slot: 'enable'},
        {title: '操作', slot: 'action', width: 70, align: 'center'}
      ],
      type: 0,
      openType: 'add',
      form: {
        id: '',
        name: '',
        title: '',
        mobile: '',
        account: '',
        password: '',
        password2: '',
        email:'',
        type: 1,
        qqAccount: '',
        enable: true
      },
      options: [{
        value: 0,
        label: '全部'
      }, {
        value: 2,
        label: '管理员'
      }, {
        value: 1,
        label: '普通人员'
      }],
      name: '',
      tableData: [
        // { name: 'sxj', title: 'boss', mobile: '13412121212', type: 'admin', qqAccount: '212731', email: 'sxj@123.com' }
        ],
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
    },
    formQueryAdd() {
        return {
          password: this.form.password,
          nickname: this.form.name,
          type: this.form.type,
          account: this.form.account,
          email: this.form.email,
          mobile: this.form.mobile,
          title: this.form.title,
          isEnable: this.form.enable?1:0,
          qqAccount: this.form.qqAccount
        }
    },
    formQueryUpdate() {
      return {
        id: this.form.id,
        password: this.form.password,
        nickname: this.form.name,
        account: this.form.account,
        type: this.form.type,
        email: this.form.email,
        mobile: this.form.mobile,
        title: this.form.title,
        isEnable: this.form.enable?1:0,
        qqAccount: this.form.qqAccount
      }
    }
  },

  created() {
    this.reqList()
  },
  methods: {
    formatMapType(row) {
      return row === 1 ? '普通' : '管理员'
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
      this.form.account = item.account
      this.form.name = item.nickname
      this.form.mobile = item.mobile
      this.form.type = item.type
      this.form.title = item.title
      this.form.enable = item.isEnable
      this.form.email = item.email
      this.form.qqAccount = item.qqAccount

      this.popShow = true
    },
    onSave(confirm) {
      this.$refs.formFields.validate(valid => {
        if (valid) {
          if (this.openType === 'edit') {
            this.reqEdit()
          } else {
            this.reqAdd()
          }
        } else {
          return false
        }
      })
    },
    reqAdd() {
      if(this.form.password === this.form.password2) {
        add(this.formQueryAdd).then(res => {
          if (res.status === true) {
            this.close()
            this.reqList()
          } else {
            this.$notify({
              title: 'Success',
              message: 'Created Successfully',
              type: 'success',
              duration: 2000
            })
          }
        })
      }else{
        this.$Notice.error({
          title: '两次密码不一致',
        });
      }
    },
    reqEdit() {
      update(this.formQueryUpdate).then(res => {
        if (res.status === true) {
          this.close()
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
    onPageChange(page) {
      this.currentPage = page
      this.reqList()
    },
    onSearch() {
      this.reqList()
    },
    close() {
      this.popShow = false
      this.form = this.formClear()
      // if (confirm && data.openType === 'edit') {
        // this.reqFun(data)
      // }
      // this.reqList()
    },
    formClear() {
      return {
        id:'',
        enable:true,
        name: '',
        mobile: '',
        password: '',
        password2: '',
        account: '',
        type: 1,
        title: '',
        qqAccount: '',
        email: ''
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
            title: 'Success',
            message: 'Created Successfully',
            type: 'success',
            duration: 2000
          })
        }
      })
    },

  }
}
</script>

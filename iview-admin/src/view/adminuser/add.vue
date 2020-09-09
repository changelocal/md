<template>
  <Modal
    :title="title"
    :visible="true"
    :close-on-click-modal="false"
    width="30%"
    @close="onClose(false)"
  >
    <Form ref="formFields" :model="form" :rules="rulesRight">
      <Form-item label="姓名" prop="name" >
        <Input v-model="form.name" placeholder="请输入姓名" clearable />
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
        <Radio-group v-model="form.type" :disabled="readOnly">
          <Radio :label="1">管理员</Radio>
          <Radio :label="2">销售</Radio>
        </Radio-group>
      </Form-item>
      <Form-item label="禁用/启用" >
        <Switch
          v-model="form.enable"
          active-color="#13ce66"
          inactive-color="#ff4949"
        />
      </Form-item>
    </Form>
    <div slot="footer" class="dialog-footer">
      <Button type="primary" :disabled="readOnly" @click="onSave(true)">保 存</Button>
      <Button @click="onClose(false)">取 消</Button>
    </div>
  </Modal>
</template>

<script>
import API from '@/api/adminuser'

export default {

  props: {
    handleClose: {
      type: Function,
      default() {
        return () => {}
      }
    },
    openType: {
      type: String,
      default: 'add'
    },
    formData: {
      type: Object,
      default() {
        return {
          id: '',
          name: '',
          title: '',
          mobile: '',
          type: '',
          qqAccount: '',
          enable: true
        }
      }
    }
  },
  data() {
    return {
      dataSourceCiTypeLoad: false,
      dataSourceId: '',
      filterList: [null],
      form: {
        name: '',
        mobile: '',
        type: 2,
        title: '',
        qq: '',
        email: '',
        enable: true
      },
      rulesRight: {
        name: [{ required: true, message: '请输入', trigger: 'blur' }],
        mobile: [{ required: true, message: '请选择', trigger: 'blur' }],
        title: [{ required: true, message: '请选择', trigger: 'blur' }],
        qqAccount: [{ required: true, message: '请选择', trigger: 'blur' }],
        email: [{ required: true, message: '请选择', trigger: 'blur' }]
      }
    }
  },
  computed: {
    formQueryAdd() {
      return {
        name: this.form.name,
        type: this.form.type,
        mail: this.form.mail,
        mobile: this.form.mobile,
        title: this.form.title,
        qq: this.form.qq
      }
    },
    dialogFormVisible() {
      return this.show
    },
    title() {
      const type = this.openType === 'add' ? '添加' : this.openType === 'edit' ? '修改' : '查看'
      return type + '销售'
    },
    readOnly() {
      return this.openType === 'view'
    }
  },
  created() {
    if (this.openType !== 'add') {
      this.form.id = this.formData.id
      this.form.name = this.formData.name
      this.form.title = this.formData.title
      this.form.mobile = this.formData.mobile
      this.form.qqAccount = this.formData.qqAccount
      this.form.email = this.formData.email
      this.form.enable = this.formData.enable
      this.form.type = this.formData.type
    }
  },
  methods: {

    formClear() {
      return {
        name: '',
        mobile: '',
        type: 1,
        title: '',
        qq: '',
        email: ''
      }
    },
    onClose(confirm) {
      this.form = this.formClear()
      this.handleClose(this.form, confirm)
    },
    showMsg(message, type) {
      this.$message({
        showClose: true,
        message,
        type
      })
    },
    onSave(confirm) {
      this.$refs.formFields.validate(valid => {
        if (valid) {
          if (this.openType === 'edit') {
            this.handleClose(this.form, confirm)
          } else {
            this.reqAdd()
          }
        } else {
          return false
        }
      })
    },
    reqAdd() {
      this.$http.post(API.adminuserAdd, this.formQueryAdd).then(res => {
        if (res.status === 200) {
          const status = res.data.statusCode
          const message = res.data.message
          if (status === 200) {
            this.showMsg(message, 'success')
            this.handleClose(this.form, true)
          } else {
            this.showMsg(message, 'error')
          }
        }
      })
    }
  }
}
</script>

<template>
  <el-dialog
    :title="title"
    :visible="true"
    :close-on-click-modal="false"
    width="30%"
    @close="onClose(false)"
  >
    <el-form ref="formFields" :model="form" :rules="rulesRight">
      <el-form-item label="openid" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.openid" :disabled="readOnly" clearable />
      </el-form-item>
      <el-form-item label="昵称" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.nickName" :disabled="readOnly" clearable />
      </el-form-item>
      <el-form-item label="电话" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.mobile" :disabled="readOnly" clearable />
      </el-form-item>
      <el-form-item label="真实姓名" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.realName" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item label="身份证" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.idCard" placeholder="请输入身份证" clearable />
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onSave(true)">保 存</el-button>
      <el-button @click="onClose(false)">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import API from '@/api/wxuser'

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
          nickName: '',
          realName: '',
          mobile: '',
          openid: '',
          idCard: ''
        }
      }
    }
  },
  data() {
    return {
      form: {
        id: '',
        nickName: '',
        realName: '',
        mobile: '',
        openid: '',
        idCard: ''
      },
      formLabelWidth: '80px',
      rulesRight: {
        realName: [{ required: true, message: '请输入', trigger: 'blur' }],
        mobile: [{ required: true, message: '请输入', trigger: 'blur' }],
        idCard: [{ required: true, message: '请输入', trigger: 'blur' }]
      }
    }
  },
  computed: {
    formQueryAdd() {
      return {
        id: this.form.id,
        realName: this.form.realName,
        mobile: this.form.mobile,
        idCard: this.form.idCard
      }
    },
    dialogFormVisible() {
      return this.show
    },
    title() {
      const type = this.openType === 'add' ? '添加' : this.openType === 'edit' ? '修改' : '查看'
      return type + '用户'
    },
    readOnly() {
      return this.openType !== 'add'
    }
  },
  created() {
    if (this.openType !== 'add') {
      this.form.nickName = this.formData.nickName
      this.form.realName = this.formData.realName
      this.form.mobile = this.formData.mobile
      this.form.idCard = this.formData.idCard
      this.form.openid = this.formData.openid
      this.form.id = this.formData.id
    }
  },
  methods: {

    formClear() {
      return {
        id: '',
        nickName: '',
        realName: '',
        mobile: '',
        openid: '',
        idCard: ''
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
          // if (this.openType === 'edit') {
          //   this.handleClose(this.form, confirm)
          // } else {
          this.reqAdd()
          // }/
        } else {
          return false
        }
      })
    },
    reqAdd() {
      this.$http.post(API.wxuserEdit, this.formQueryAdd).then(res => {
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

<template>
  <el-dialog
    :title="title"
    :visible="true"
    :close-on-click-modal="false"
    width="30%"
    @close="onClose(false)"
  >
    <el-form ref="formFields" :model="form" :rules="rulesRight">
      <el-form-item label="咨询人" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.buyerName" :disabled="readOnly" clearable />
      </el-form-item>
      <el-form-item label="咨询电话" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.buyerMobile" :disabled="readOnly" clearable />
      </el-form-item>
      <el-form-item label="状态" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.status" :disabled="readOnly" clearable />
      </el-form-item>
      <el-form-item label="预付款" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.prePay" clearable />
      </el-form-item>
      <el-form-item label="备注" prop="note" :label-width="formLabelWidth">
        <el-input v-model="form.note" placeholder="请输入备注" clearable />
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onSave(true)">推 单</el-button>
      <el-button @click="onClose(false)">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import API from '@/api/consultation'

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
          buyerName: '',
          buyerMobile: '',
          status: '',
          note: ''
        }
      }
    }
  },
  data() {
    return {
      form: {
        id: '',
        buyerName: '',
        buyerMobile: '',
        status: '',
        note: ''
      },
      formLabelWidth: '80px',
      rulesRight: {
        note: [{ required: true, message: '请输入', trigger: 'blur' }]
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
      return type + '推单'
    },
    readOnly() {
      return this.openType !== 'add'
    }
  },
  created() {
    if (this.openType !== 'add') {
      this.form.buyerName = this.formData.buyerName
      this.form.buyerMobile = this.formData.buyerMobile
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

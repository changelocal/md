<template>
  <el-dialog
    :title="title"
    :visible="true"
    :close-on-click-modal="false"
    width="30%"
    @close="onClose(false)"
  >
    <el-form ref="formFields" :model="form" :rules="rulesRight">
      <el-form-item label="订单号" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.no" :disabled="readOnly" placeholder="" clearable />
      </el-form-item>
      <el-form-item label="状态" prop="status" :label-width="formLabelWidth">
        <el-select v-model="form.status" placeholder="请选择">
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.type" :disabled="readOnly" placeholder="" clearable />
      </el-form-item>
      <el-form-item label="预付款" prop="" :label-width="formLabelWidth">
        <el-input-number v-model="form.prePay" :min="0" :max="99999" controls-position="right" placeholder="请输入预付款" clearable />
      </el-form-item>
      <el-form-item label="剩余款" prop="" :label-width="formLabelWidth">
        <el-input-number v-model="form.restPay" :min="0" :max="99999" controls-position="right" placeholder="请输入剩余款" clearable />
      </el-form-item>
      <el-form-item label="共付款" prop="" :label-width="formLabelWidth">
        <el-input-number v-model="form.totalPay" :min="0" :max="99999" controls-position="right" placeholder="请输入共付款" clearable />
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onSave(true)">保 存</el-button>
      <el-button @click="onClose(false)">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import API from '@/api/order'

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
          dataSourceId: '',
          dataSourceCiType: '',
          dataSourceCiField: '',
          produceField: '',
          produceCiType: '',
          mapType: 1
        }
      }
    }
  },
  data() {
    return {
      options: [{
        value: 0,
        label: '全部'
      }, {
        value: 1,
        label: '待支付定金'
      }, {
        value: 2,
        label: '待提交资料'
      }, {
        value: 3,
        label: '委托受理'
      }, {
        value: 4,
        label: '待支付尾款'
      }, {
        value: 5,
        label: '已完成'
      }],
      form: {
        name: '',
        mobile: '',
        type: 1,
        title: '',
        qq: '',
        email: ''
      },
      formLabelWidth: '80px',
      rulesRight: {
        name: [{ required: true, message: '请输入', trigger: 'blur' }],
        mobile: [{ required: true, message: '请选择', trigger: 'blur' }],
        title: [{ required: true, message: '请选择', trigger: 'blur' }],
        qq: [{ required: true, message: '请选择', trigger: 'blur' }],
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
        qq: this.qq
      }
    },
    dialogFormVisible() {
      return this.show
    },
    title() {
      const type = this.openType === 'add' ? '添加' : this.openType === 'edit' ? '修改' : '查看'
      return type + '订单'
    },
    readOnly() {
      return this.openType === 'edit'
    }
  },
  created() {
    if (this.openType !== 'add') {
      this.form.no = this.formData.no
      this.form.type = this.formData.type
      this.form.status = this.formData.status
      this.form.prePay = this.formData.prePay
      this.form.restPay = this.formData.restPay
      this.form.totalPay = this.formData.totalPay
    }
  },
  methods: {

    formClear() {
      return {
        no: '',
        status: '',
        type: 0,
        prePay: '',
        restPay: '',
        totalPay: ''
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

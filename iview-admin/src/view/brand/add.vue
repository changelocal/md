<template>
  <el-dialog
    :title="title"
    :visible="true"
    :close-on-click-modal="false"
    width="40%"
    @close="onClose(false)"
  >
    <el-form ref="formFields" :model="form" :rules="rulesRight">
      <el-form-item label="编号" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.name" :disabled="readOnly" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item label="名称" prop="" :label-width="formLabelWidth">
        <el-input v-model="form.mobile" :disabled="readOnly" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item label="头衔" prop="title" :label-width="formLabelWidth">
        <el-input v-model="form.title" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item label="最低价格" prop="email" :label-width="formLabelWidth">
        <el-input v-model="form.email" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item label="最高价格" prop="qq" :label-width="formLabelWidth">
        <el-input v-model="form.qq" placeholder="请输入姓名" clearable />
      </el-form-item>
      <el-form-item label="下架/上架" :label-width="formLabelWidth">
        <el-switch
          v-model="form.enable"
          active-color="#13ce66"
          inactive-color="#ff4949"
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="onSave(true)">保 存</el-button>
      <el-button @click="onClose(false)">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {query} from '@/api/brand'

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
          enable: ''
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
        qq: this.form.qq
      }
    },
    dialogFormVisible() {
      return this.show
    },
    title() {
      const type = this.openType === 'add' ? '添加' : this.openType === 'edit' ? '修改' : '查看'
      return type + '商标'
    },
    readOnly() {
      return this.openType === 'edit'
    }
  },
  created() {
    if (this.openType !== 'add') {
      this.form.id = this.formData.id
      this.form.name = this.formData.name
      this.form.title = this.formData.title
      this.form.mobile = this.formData.mobile
      this.form.qq = this.formData.qq
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

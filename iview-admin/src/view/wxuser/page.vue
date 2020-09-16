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
            <Button type="primary" size="small" style="margin-right: 5px" @click="onMakeService(index)">推服务订单</Button>
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
    <Modal
      v-model="popShowService"
      title="推服务订单"
      :visible="true"
      :close-on-click-modal="false"
      width="30%"
      :closable="false"
      :mask-closable="false"
      @close="onClose(false)"
    >
      <Form  :label-width="80"  ref="formFields" :model="formService" :rules="rulesRightService">
        <Form-item label="买家名字" prop="" >
          <Input v-model="formService.buyerName" disabled placeholder="" clearable />
        </Form-item>
        <Form-item label="买家手机" prop="" >
          <Input v-model="formService.buyerMobile" disabled placeholder="" clearable />
        </Form-item>
        <Form-item label="服务分类" prop="" >
          <RadioGroup v-model="animal" @on-change="radioChange">
            <Radio label="维权"></Radio>
            <Radio label="信息变更"></Radio>
          </RadioGroup>
        </Form-item>
        <Form-item label="服务内容" prop="" >
          <Select v-model="formService.brandType" placeholder="请选择" @on-change="selectChange">
            <Option
              v-for="item in optionsPrice"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            />
          </Select>
        </Form-item>
        <Form-item label="定金" prop="prePay" >
          <InputNumber :max="99999" :min="1" :step="1"  v-model="formService.prePay" placeholder="请输入" clearable />
        </Form-item>
        <Form-item label="总价" prop="totalPay" >
          <InputNumber :max="99999" :min="1" :step="1" v-model="formService.totalPay" placeholder="请输入" clearable />
        </Form-item>

      </Form>
      <div slot="footer" class="dialog-footer">
        <Button type="primary" @click="onSaveService(true)">立即推单</Button>
        <Button @click="onCloseService(false)">取 消</Button>
      </div>
    </Modal>
  </div>
</template>

<script>
import {query,update} from '@/api/wxuser'
import {loadSelect} from '@/api/common'
import {pushService} from '@/api/order'
export default {
  name: 'PagePermission',
  data() {
    return {
      animal:'维权',
      rulesRight: {
        realName: [{ required: true, message: '请输入', trigger: 'blur' }],
        // mobile: [{ required: true, message: '请输入', trigger: 'blur' }],
        // idCard: [{ required: true, message: '请输入', trigger: 'blur' }]
      },
      rulesRightService: {
        prePay: [{ required: true, message: '请输入' }, { type:'number', message: '必须是数字' } ],
        totalPay: [{ required: true, message: '请输入' }, { type:'number', message: '必须是数字' } ],
      },
      columns1: [
        {title: 'openID', key: 'minId', width: 250},
        {title: '昵称', key: 'nickName'},
        {title: '真实姓名', key: 'realName', width: 100},
        {title: '身份证', key: 'idCard'},
        {title: '手机', key: 'mobile', width: 120},
        {title: '地址', key: 'address'},
        {title: '关注时间', key: 'createTime'},
        {title: '操作', slot: 'action', width: 180, align: 'center'}
      ],
      optionsPrice: [],
      rights: [],
      changes: [],
      openType: 'add',
      form: {
        id: '',
        nickName: '',
        realName: '',
        mobile: '',
        minId: '',
        idCard: ''
      },
      formService: {
        buyerId: '',
        buyerName: '',
        buyerMobile: '',
        serviceId: '',
        prePay: 0,
        totalPay: 0,
      },
      mobile: '',
      tableData: [],
      currentRow: null,
      currentPage: 1,
      popShow: false,
      popShowService: false,
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
    },
    formQueryPush() {
      return {
        userId: this.formService.buyerId,
        prePay: this.formService.prePay,
        totalPay: this.formService.totalPay,
        productNo: this.formService.serviceId,
      }
    }

  },
  created() {
    this.reqList()
    this.loadSelect()
  },
  methods: {
    selectChange() {
      if(this.animal==='维权'){
        this.rights.forEach(p=>{
          if(p.id === this.formService.brandType) {
            this.formService.prePay = p.prepay
            this.formService.totalPay = p.price
          }
        })
      }else{
        this.changes.forEach(p=>{
          if(p.id === this.formService.brandType) {
            this.formService.prePay = p.prepay
            this.formService.totalPay = p.price
          }
        })
      }
    },
    loadSelect(){
      loadSelect().then(res => {
        console.log(res)
        if (res.status === true) {
          const rdata = res.data
          this.rights = rdata.rights
          this.changes = rdata.changes
          this.optionsPrice = this.rights
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
    radioChange(){
      if(this.animal==='维权'){
        this.optionsPrice = this.rights
      }else{
        this.optionsPrice = this.changes
      }
    },
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
    /**
     * 推服务订单
     * @param confirm
     */
    onSaveService(confirm) {
      this.$refs.formFields.validate(valid => {
        if (valid) {
          if(this.formService.prePay>= this.formService.totalPay){
            this.$Notice.error({
              title: '定金不能大于等于总价',
            });
            return  false
          }
          pushService(this.formQueryPush).then(res => {
            console.log(res)
            if (res.status === true) {
              this.onCloseService()
              this.$notify({
                title: 'Success',
                message: '服务订单推送成功，请在订单列表里查看',
                type: 'success',
                duration: 2000
              })
            }else {
              this.$notify({
                title: 'Success',
                message: 'Created Successfully',
                type: 'success',
                duration: 2000
              })
            }
          })




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

      this.popShow = true
    },
    onMakeService(index){
      const item = this.tableData[index]
      if(item.mobile==='') {
        this.$Notice.error({
          title: '手机不能为空',
        });
      }else {
        this.formService.buyerId = item.id
        this.formService.buyerMobile = item.mobile
        this.popShowService = true
      }
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
    onCloseService( confirm) {
      this.popShowService = false
      // if (confirm) this.reqFun(data);
      this.form = this.formServiceClear()
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
    formServiceClear() {
      return {
        buyerId: '',
        buyerName: '',
        buyerMobile: '',
        serviceId: '',
        prePay: 0,
        totalPay: 0,
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

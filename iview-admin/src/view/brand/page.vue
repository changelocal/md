<template>
      <Form :inline="true" :label-width="60">
        <Form-item label="商标分类">
          <Select v-model="brandClass" placeholder="请选择">
            <Option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="字符长度">
          <Select v-model="charLength" placeholder="请选择">
            <Option
              v-for="item in optionsChar"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="商标类型">
          <Select v-model="brandType" placeholder="请选择">
            <Option
              v-for="item in optionsType"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="出售价格">
          <Select v-model="brandPrice" placeholder="请选择">
            <Option
              v-for="item in optionsPrice"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </Select>
        </Form-item>
        <Form-item label="商标名字">
          <Input v-model="name" placeholder="请输入商标名字" clearable>
          </Input>
        </Form-item>
        <Form-item label="">
          <Button type="primary" @click="onSearch" >搜索</Button>
        </Form-item>
        <Form-item label="">
          <Button type="primary"  @click="onAdd">添加商标</Button>
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
import {query} from '@/api/brand'
import Add from './add'
export default {
  name: 'PagePermission',
  components: {
    Add
  },
  data() {
    return {
      columns1: [
        {title: '编号', key: 'minId'},
        {title: '图片', key: 'nickName'},
        {title: '类型', key: 'realName'},
        {title: '名称', key: 'idCard'},
        {title: '注册号', key: 'mobile'},
        {title: '分类', key: 'address'},
        {title: '价格区间', key: 'createTime'},
        {title: '上架', key: 'createTime'},
        {title: '操作', slot: 'action', width: 150, align: 'center'}
      ],
      brandClass: 0,
      brandPrice: 0,
      charLength: 0,
      brandType: 0,
      openType: 'add',
      form: {
        id: '',
        name: '',
        title: '',
        mobile: '',
        type: '',
        qqAccount: '',
        enable: ''
      },
      options: [{
        value: 0, label: '全部'
      }, {
        value: 1, label: '管理员'
      }, {
        value: 2, label: '普通人员'
      }],
      optionsChar: [{
        value: 0, label: '不限'
      }, {
        value: 1, label: '1-2字'
      }, {
        value: 2, label: '3字'
      }, {
        value: 3, label: '4字'
      }, {
        value: 4, label: '5字'
      }, {
        value: 5, label: '5字以上'
      }],
      optionsPrice: [{
        value: 0, label: '不限'
      }, {
        value: 1, label: '1万以下'
      }, {
        value: 2, label: '1-2万'
      }, {
        value: 3, label: '2-5万'
      }, {
        value: 4, label: '5万以上'
      }],
      optionsType: [{
        value: 0, label: '不限'
      }, {
        value: 1, label: '中文'
      }, {
        value: 2, label: '字母'
      }, {
        value: 3, label: '中文+字母'
      }, {
        value: 4, label: '图形'
      }, {
        value: 5, label: '中文+图形'
      }, {
        value: 6, label: '字母+图形'
      }, {
        value: 7, label: '中文+字母+图形'
      }, {
        value: 8, label: '数字'
      }, {
        value: 9, label: '字母+数字'
      }],
      name: '',
      tableData: [{ name: 'sxj', title: 'boss' }],
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
        currentPage: this.currentPage,
        pageSize: this.pageSize,
        name: this.name,
        type: this.type
      }
    }
  },

  created() {
    this.reqList()
  },
  methods: {
    formatMapType(row) {
      return row.enable === 1 ? '有效' : '无效'
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
    onClose(data, confirm) {
      this.popShow = false
      this.popNewOrderShow = false
      // if (confirm) this.reqFun(data);
      this.form = this.formClear()
    },
    onSearch() {
      this.reqList()
    },
    reqList() {
      this.$http.post(API.adminuserQuery, this.formQuery).then(res => {
        if (res.status === 200) {
          const status = res.data.statusCode
          const rdata = res.data.data
          if (status === 200) {
            this.tableData = rdata.list
            this.totalPage = rdata.total
            this.currentPage = rdata.currentPage
          }
        }
      })
    }
  }
}
</script>

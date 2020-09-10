<template>
  <div>
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
        <Page :current="currentPage" :total="totalPage" @on-change="onPageChange" show-elevator size="small" show-total></Page>

      </Form>
<!--    <add v-if="popShow" :open-type="openType" :form-data="form" :handle-close="onClose" />-->
  </div>
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
      options: [
        {value: 0, label: '不限'},
        {label: '化学原料',value:1},
        {label: '颜料油漆',value:2},
        {label: '日化用品',value:3},
        {label: '燃料油脂',value:4},
        {label: '医药	',value:5 },
        {label: '金属材料',value:6},
        {label: '机械设备',value:7},
        {label: '手工器械',value:8},
        {label: '科学仪器',value:9},
        {label: '医疗器械',value:10},
        {label: '灯具空调',value:11},
        {label: '运输工具',value:12},
        {label: '军火烟火',value:13},
        {label: '珠宝钟表',value:14},
        {label: '乐器	',value:15 },
        {label: '办公用品',value:16},
        {label: '橡胶制品',value:17},
        {label: '皮革皮具',value:18},
        {label: '建筑材料',value:19},
        {label: '家具	',value:20 },
        {label: '厨房洁具',value:21},
        {label: '绳网袋篷',value:22},
        {label: '纱线丝	',value:23 },
        {label: '布料床单',value:24},
        {label: '服装鞋帽',value:25},
        {label: '钮扣拉链',value:26},
        {label: '地毯席垫',value:27},
        {label: '健身器材',value:28},
        {label: '食品	',value:29 },
        {label: '方便食品',value:30},
        {label: '饲料种籽',value:31},
        {label: '啤酒饮料',value:32},
        {label: '酒精饮品',value:33},
        {label: '烟草烟具',value:34},
        {label: '广告销售',value:35},
        {label: '金融物管',value:36},
        {label: '建筑修理',value:37},
        {label: '通讯服务',value:38},
        {label: '运输贮藏',value:39},
        {label: '材料加工',value:40},
        {label: '教育娱乐',value:41},
        {label: '网站服务',value:42},
        {label: '餐饮住宿',value:43},
        {label: '医疗园艺',value:44},
        {label: '社会服务',value:45},
        ],
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
        pageIndex: this.currentPage,
        pageSize: this.pageSize,
        brandName: this.name,
        categoryNo: this.brandClass,
        priceType: this.brandPrice,
        unionType: this.brandType,
        brandSize: this.charLength,
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
      query(this.formQuery).then(res => {
        console.log(res)
        if (res.status === true) {
          const rdata = res.data

          this.tableData = rdata.list
          this.totalPage = rdata.count
          // this.currentPage = rdata.currentPage

        }else {
          this.$notify({
            title: '错误',
            message: res.message,
            type: 'error',
            duration: 2000
          })
        }
      })
    }
  }
}
</script>

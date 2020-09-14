<template>
  <div class="demo-upload-list" >
    <card>
    <Table  :columns="columns1" ref="singleTable" :data="tableData" highlight-current-row style="width: 100%">
    </Table>
    <Modal title="View Image" v-model="visible">
      <img :src="'https://o5wwk8baw.qnssl.com/' + imgName + '/large'"  style="width: 100%">
    </Modal>
    </card>
  </div>

</template>
<script>
import {image} from '@/api/order'
export default {
  data () {
    return {
      columns1: [
        {title: '类型', key: 'imageType', width: 200},
        {title: '图片', key: 'imageUrl',columns: {
            'width':'50px'
          },
          render: (h, params) => {
            return h('div', [
              h('img', {
                attrs: {
                  src: params.row.imageUrl
                },
                style: {
                  width: '200px',
                  height: '200px'
                }
              }),
            ]);
          }},
      ],
      tableData: [
        {
          'imageType': '身份证正面',
          'imageUrl': 'https://o5wwk8baw.qnssl.com/a42bdcc1178e62b4694c830f028db5c0/avatar'
        },
        {
          'imageType': '身份证反面',
          'imageUrl': 'https://o5wwk8baw.qnssl.com/bc7521e033abdd1e92222d733590f104/avatar'
        }
      ],
      imgName: '',
      visible: false,
      uploadList: []
    }
  },
  computed:{
    rOrderId() {
      return this.$route.query.orderId;
    },
    formQuery() {
      return {
        orderId: this.rOrderId,
      }
    },
  },
  created() {
    this.reqList()
  },
  methods: {
    reqList() {
      image(this.formQuery).then(res => {
        console.log(res)
        if (res.status === true) {
          const rdata = res.data
          this.tableData = rdata.list

        } else {
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

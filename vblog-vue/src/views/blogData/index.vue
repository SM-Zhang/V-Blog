<template>
    <div>
        <ve-pie :data="chartData"></ve-pie>
    </div>
    
</template>

<script>
import { blogData } from '@/api/articleApi'

export default {
    data () {
      return {
        chartData: {
          columns: ['博客', '条数'],
          rows: [
            { '博客': '已发布', '条数': 0 },
            { '博客': '私密', '条数': 0 },
            { '博客': '草稿箱', '条数': 0 },
            { '博客': '回收站', '条数': 0 },
          ]
        },
        uid: this.$store.state.user.userInfo.uid,
      }
    },

    created() {
        this.fetchData()
    },

    methods: {
        fetchData(){
            blogData(this.uid).then(response => {
                console.log("-=-=-=---=---=-=-=--=-==-")
                console.log(response.data)
                var i = 0;
                this.chartData.rows.forEach(row => {
                  row['条数'] = response.data.data[i];
                  console.log(row['条数']);
                  console.log(response.data.data[i]);
                  i++;
                  console.log("i", i);
                });

            })
        }
    },
}
</script>

<style scoped>

</style>
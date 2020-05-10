<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="个人资料" name="details">
      <Details ></Details>
    </el-tab-pane>
    <el-tab-pane label="我的博客" name="myBlog">
      <my-blog ></my-blog>
    </el-tab-pane>
    <el-tab-pane label="草稿箱" name="myDraft">
      <my-draft ></my-draft>
    </el-tab-pane>
    <el-tab-pane label="回收站" name="myRecycle">
      <my-recycle ></my-recycle>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import Details from './details'
import MyBlog from './myBlog'
import MyDraft from './myDraft'
import MyRecycle from './myRecycle'

  export default {
    components: {
          Details,
          MyBlog,
          MyDraft,
          MyRecycle
    },
    data() {
      return {
        activeName: 'details',
      };
    },

    mounted() {
      this.refreshPage();
    },

    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
        console.log(tab.name);
        var rp = 'details';
        if(tab.name == 'details'){
          rp = 'details';
        }else if(tab.name == 'myBlog'){
          rp = 'myBlog';
        }else if(tab.name == 'myDraft'){
          rp = 'myDraft';
        }else if(tab.name == 'myRecycle'){
          rp = 'myRecycle';
        }
        this.$router.push({
          path: '/personInfo',
          query: {
              rpName: rp
          }
        })
      },

      refreshPage(){
        var rp = this.$route.query.rpName;
        if(rp == null){
          rp = 'details'
        }
        this.activeName = rp;
      }
    }
  };
</script>

<style scoped>

</style>
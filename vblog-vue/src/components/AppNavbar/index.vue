<template>
  <div id="navbar">
      <!-- @open="handleOpen"
      @close="handleClose" -->
    <el-menu
      :router="true"
      :default-active="$route.path"
      class="el-menu-vertical-demo"
      text-color="#303133"
      background-color="#fff"
      active-text-color="#33a3dc">
        <el-menu-item index="/home" >
            <i class="el-icon-s-home"></i>
            <span slot="title" >&nbsp;首页</span>
        </el-menu-item>
        <el-menu-item index="/blogList">
            <i class="el-icon-tickets"></i>
            <span slot="title">&nbsp;博客列表</span>
        </el-menu-item>
        <el-menu-item index="/sendBlog">
            <i class="el-icon-document-add"></i>
            <span slot="title">&nbsp;博客编辑</span>
        </el-menu-item>
        <el-menu-item index="/personInfo">
            <i class="el-icon-notebook-2"></i>
            <span slot="title">&nbsp;我的主页</span>
        </el-menu-item>
        <el-menu-item index="/blogInfo">
            <i class="el-icon-document-copy"></i>
            <span slot="title">&nbsp;博客详情</span>
        </el-menu-item>

        <el-submenu index="blogSettings" v-if="uroles != 1">
            <template slot="title">
                <i class="el-icon-setting"></i>
                <span>&nbsp;博客管理</span>
            </template>
            <el-menu-item-group>
                <template slot="title">&nbsp;管理员</template>
                <el-menu-item index="/userManage">
                    <i class="el-icon-user"></i>
                    <span slot="title" >&nbsp;用户管理</span>
                </el-menu-item>
                <el-menu-item index="/blogCategory">
                    <i class="el-icon-menu"></i>
                    <span slot="title">&nbsp;博客类型</span>
                </el-menu-item>
                <el-menu-item index="/blogTag">
                    <i class="el-icon-price-tag"></i>
                    <span slot="title" >&nbsp;博客标签</span>
                </el-menu-item>
            </el-menu-item-group>
        </el-submenu>

        <el-menu-item index="/blogData">
            <i class="el-icon-data-analysis"></i>
            <span slot="title" >&nbsp;数据统计</span>
        </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
    data() {
        return {
            uroles: this.$store.state.user.userInfo.uroles,
        }
    },
};
</script>

<style>
#navbar {
  position: absolute;
  width: 300px;
  top: 50px;
  /* bottom: 0; */
  background: #723030;
  overflow-y: auto;
}
.el-menu {
    border-right: 0;
}
.el-menu > .el-menu-item {
    padding-left: 70px !important;
}
.el-submenu > .el-submenu__title {
    padding-left: 70px !important;
}
.el-menu-item-group__title {
    padding-left: 75px !important;
}
.el-menu-item-group > ul > .el-menu-item {
    padding-left: 100px !important;
}
</style>
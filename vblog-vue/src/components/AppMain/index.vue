<template>
    <div id="main">
        <!--加载面包屑组件-->
        <bread-crumb v-show="$route.path !== '/home'"></bread-crumb>
        <!--路由渲染出口-->
        <keep-alive>
            <router-view></router-view>
        </keep-alive>
    </div>
</template>

<script>
import BreadCrumb from './breadcrumb'

export default {
    components: {
        BreadCrumb
    }
}
</script>

<style scoped>
#main {
    position: absolute;
    top: 50px;
    border-left: 12px solid;
    border-image: linear-gradient(to bottom,transparent,#bdc3c7,#8098af,transparent) 30 30;
    left: 300px;
    background: #ffffff;
    right: 20px;
    padding: 20px 20px 0 30px;
}
</style>
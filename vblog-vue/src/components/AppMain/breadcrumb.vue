<template>
    <!-- > 型面包屑 -->
    <el-breadcrumb separator-class="el-icon-arrow-right" class="breadcrumb">
        <el-breadcrumb-item v-show="$route.path == '/userManage' || $route.path == '/blogType' || $route.path == '/blogTag'">博客管理</el-breadcrumb-item>
        <el-breadcrumb-item  :to="{ path: $route.path }">
            {{ $route.meta.title }}
        </el-breadcrumb-item>
    </el-breadcrumb>
</template>

<style scoped>
    .el-breadcrumb {
        height: 20px;
        padding: 9px 0 9px 25px;
        box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        border-left: 6px solid #31715c;
        line-height: 20px;
        margin-bottom: 20px;
    }
</style>
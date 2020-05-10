import Vue from "vue";
import VueRouter from "vue-router";
import Login from "@/views/login"
import Layout from "@/components/Layout.vue";
// 引入路由跳转页面
import Home from "@/views/home"
import BlogList from "@/views/blogList"
import SendBlog from "@/views/sendBlog"
import personInfo from "@/views/personInfo"
import BlogInfo from "@/views/blogInfo"
import UserManage from "@/views/blogSettings/userManage"
import BlogCategory from "@/views/blogSettings/blogCategory"
import BlogTag from "@/views/blogSettings/blogTag"
import BlogData from "@/views/blogData"

Vue.use(VueRouter);

// 点击没问题 如果在这个点击页面 在点击一次router-link 就会报这个错 但是不影响功能
// 去你引用vue-router的页面添加一段代码
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

// const routes = [
//   {
//     path: "/",
//     name: "Layout",
//     component: Layout
//   }

// ];

// const router = new VueRouter({
//   mode: "history",
//   base: process.env.BASE_URL,
//   routes
// });

// export default router;

// 注意切换的路由组件时compontent 没有s
// 通过一个名称来标识一个路由显得更方便一些 name
export default new VueRouter({
  // mode: "history",
  base: process.env.BASE_URL,

  routes: [
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/',
      component: Layout,
      redirect: '/home',
      children: [
        {
          path: '/home',
          component: Home,
        }
      ]
    },
    // 防止一个Layout中写多个子路由，显得代码冗余，提取代码
    // 先把Layout布局渲染在App.vue中的路由出口，
    // 再定义子路由渲染再Layout.vue中的路由出口
    // 因为Layout的路由出口在AppMain中定义，所以子路由渲染在AppMain中
    {
      path: '/blogList',
      component: Layout,
      children: [
        {
          path: '',
          component: BlogList,
          meta: {title: '博客列表'}
        }
      ]
    },
    {
      path: '/sendBlog',
      component: Layout,
      children: [
        {
          path: '',
          component: SendBlog,
          meta: {title: '发布博客'}
        }
      ]
    },
    {
      path: '/personInfo',
      component: Layout,
      children: [
        {
          path: '',
          component: personInfo,
          meta: {title: '我的主页'}
        }
      ]
    },
    {
      path: '/blogInfo',
      component: Layout,
      children: [
        {
          path: '',
          component: BlogInfo,
          meta: {title: '博客详情'}
        }
      ]
    },
    {
      path: '/userManage',
      component: Layout,
      children: [
        {
          path: '',
          component: UserManage,
          meta: {title: '用户管理'}
        }
      ]
    },
    {
      path: '/blogCategory',
      component: Layout,
      children: [
        {
          path: '',
          component: BlogCategory,
          meta: {title: '博客类型'}
        }
      ]
    },
    {
      path: '/blogTag',
      component: Layout,
      children: [
        {
          path: '',
          component: BlogTag,
          meta: {title: '博客标签'}
        }
      ]
    },
    {
      path: '/blogData',
      component: Layout,
      children: [
        {
          path: '',
          component: BlogData,
          meta: {title: '数据统计'}
        }
      ]
    },
  ]
})

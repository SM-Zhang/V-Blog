import axios from 'axios'
// 以服务方式使用加载动画
import { Loading, Message } from 'element-ui'
import {removeToken} from './tokenUserInfoUtil'
import store from '@/store'

const request = axios.create({
    baseURL: process.env.VUE_APP_BASE_API,
    timeout: 1000 * 60 * 2
})

export default request

// loadingInstance采用单例模式创建，防止响应异常时频繁切换路由导致重复创建
const loading = {
    loadingInstance: null,
    // 打开加载
    openLoading: function() {
        if( this.loadingInstance === null ) {
            this.loadingInstance = Loading.service({ // 以服务的方式创建
                target: '#main',
                text: '拼命加载中...',
                background: 'rgba(255,254,249, 0.6)'
            })
        }
    },
    // 关闭加载
    closeLoading: function() {
        if(this.loadingInstance !== null ) {
            this.loadingInstance.close();
        }
        this.loadingInstance = null;
    }
}

// 请求拦截器
request.interceptors.request.use( config => {
    loading.openLoading()
    if(store.state.user.token != null){
        const token = 'Bearer ' + store.state.user.token;
        config.headers.Authorization = token;
    }
    return config;
},error => {
    loading.closeLoading(); // 出现异常，关闭加载结果
    return Promise.reject(error);
})

// 响应拦截器
request.interceptors.response.use( response => {
    loading.closeLoading();
    // 响应请求时，无数据或者超时，需要处理异常(根据响应状态码)
    if(response.data.code === 99999) {
        Message({
            message: response.data.message || '系统异常',
            type: 'warning',
            duration: 3 * 1000 // 停留3s
        })
    }
    return response;
},error => {
    loading.closeLoading(); // 出现异常，关闭加载结果
    // 当接口请求出错时，进行弹出错误提示，如 404，500，请求超时
    Message({
        message: "请求失败，请稍后再试",
        type: 'error',
        duration: 3 * 1000
    })
    removeToken;
    return Promise.reject(error);
})
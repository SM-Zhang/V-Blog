import {login, getUserInfo, updateUserInfo, logout} from '@/api/userApi'
import {getToken, setToken, setUser, getUser, removeToken} from '@/utils/tokenUserInfoUtil'

const user = {
    // 第一级，数据传递由3-2-1
    state: {
        token: getToken(), // 默认从本地存储中调用 token，防止刷新造成 token 为 null,跳转到登录页面
        userInfo: getUser() // 默认从本地存储中调用用户信息
    },

    // 第二级
    mutations: {
        SET_TOKEN(state, token) {
            state.token = token;
            setToken(token);
        },
        SET_USER(state, userInfo) {
            state.userInfo = userInfo;
            setUser(userInfo);
        }
    },

    // 第三级
    actions: {
        // 通过登录获取 token 信息，通过 commit 将数据提交给 mutations
        // loginForm 为载荷，需要用户输入传入的数据
        Login({commit}, loginForm) {
            // 提交表单给后台进行验证是否正确
            // resolve 触发成功处理， reject 触发失败处理
            // return new Promise((resolve, reject) 使其他模块知道当前执行的操作是成功还是失败
            return new Promise((resolve, reject) => { 
                login( loginForm.umail, loginForm.upassword ).then(response => {
                    const resp = response.data; // 此时获取的就是后台响应的 data 的数据
                    if(resp.data.token !== null && resp.data.token !== ''){
                        commit('SET_TOKEN', resp.data.token);
                    }
                    resolve(resp);
                }).catch(error => {
                    reject(error);
                })
            })
        },

        // 通过 token 获取用户信息; 需要使用已经更新成功或已存在的 token
        // 此时是需要获取 state 中的 token,不是载荷
        GetUserInfo({commit, state}) {
            return new Promise((resolve, reject) => {
                getUserInfo(state.token).then(response => {
                    const resp = response.data;
                    commit('SET_USER', resp.data);
                    resolve(resp); // 把数据响应给调用当前函数的位置
                }).catch(error => {
                    reject(error);
                })
            })
        },

        // 写入用户信息
        SetUserInfo({commit}, user){
            return new Promise((_resolve, _reject) => {
                commit("SET_USER", user);
            })
        },

        // 更新用户信息
        UpdateUserInfo({commit}, user){
            return new Promise((resolve, reject) => {
                updateUserInfo(user).then(response => {
                    const resp = response.data;
                    commit("SET_USER", resp.data);
                    resolve(resp);
                }).catch(error => {
                    reject(error);
                })
            })
        },

        // 退出
        // 注意 数据传入 的 载荷 的区别
        Logout({commit, state}) {
            return new Promise((resolve, reject) => {
                logout(state.token).then(response => {
                    const resp = response.data;
                    commit('SET_TOKEN',''); // 可以为 null
                    commit('SET_USER', null); // 用户信息是对象类型 {}，即赋值为空的对象，为 空值，和 null(空) 是有区别的
                    removeToken()
                    resolve(resp)
                }).catch(error => {
                    reject(error);
                })
            })
        }

    }
}

export default user;
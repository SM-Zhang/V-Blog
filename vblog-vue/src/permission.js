import router from '@/router';
import store from '@/store'
// 注意权限需要在 main.js 中导入

router.beforeEach((to, from, next) => {
    // 1. 获取 token 
    const token = store.state.user.token;
    // 2. 如果本地没有获取到 token
    if(!token){
        if(to.path !== '/login'){
            // 2.1 如果访问页面不是 '/login', 则路由跳转到 '/login'
            next({path: '/login'});
            console.log('权限不足，请登录');
        } else {
            // 2.2 如果访问的页面是 '/login', 则进行下一步的目标路由跳转
            next();
        }
    } else {
        // 3. 如果获取到 token
        if(to.path === '/login') {
            // 3.1 如果目标路由(地址栏)是 '/login', 则进行目标路由跳转, 跳转到 '/login'
            next();
        } else {
            //  const userInfo = localStorage.getItem('zsm-mms-userInfo');
            // user 代表模块名，首先应获取 state,再进行选择是哪一个模块名
            const userInfo = store.state.user.userInfo;
            // 3.2 如果目标路由不是 '/login', 查看是否有用户信息
            if(userInfo) {
                // 3.2.1 如果本地有用户信息，则进行目标路由跳转
                next();
            } else {
                // 3.2.2 如果没有用户信息，则从后台获取用户信息(必须 token 未失效的情况下)
                store.dispatch('GetUserInfo').then(response => {
                    if(response.flag) {
                        next();
                    }else {
                        next({path: '/login'})
                    }
                }).catch(error => {
                    console.log(error);
                    return false;
                })
            }
        }
    }
})
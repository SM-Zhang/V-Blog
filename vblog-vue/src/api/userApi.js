import request from '@/utils/axiosUtil'

// 登录
export function login(umail, upassword){
    return request ({
        url: '/userLog/login',
        method: 'post',
        data: {
            umail,
            upassword
        }
    })
}

// 注册
export function register(regForm){
    return request({
        url: '/user/addUser',
        method: 'post',
        data: regForm
    })
}

// 获取个人用户信息
export function getUserInfo(token){
    return request ({
        url: `/user/userInfo/${token}`,
        method: 'get',
    })
}

// 获取个人用户信息(umail)
export function userInfoByUmail(umail){
    return request ({
        url: `/user/userInfoByUmail/${umail}`,
        method: 'get',
    })
}

// 修改用户信息
export function updateUserInfo(user){
    return request ({
        url: `/user/updateUser`,
        method: 'post',
        data: user
    })
}

// 退出登录
export function logout(token){
    return request ({
        url: '/userLog/logout',
        method: 'post',
        data: {
            token
        }
    })
}

// 管理员 / 超级管理员(可以查看被删除的用户)
// 获取用户列表信息
// param: keywords(昵称),umail， uroles(非超级管理员不能查询到超级管理员)
export function getAllUsers(currentPage, pageSize, user){
    return request({
        url: `/user/getAllUsers/${currentPage}/${pageSize}`,
        method: 'post',
        data: user
    })
}

// 添加用户 1. 添加为管理员 / 2. 添加为普通用户
export function addUser(user){
    return request({
        url: '/user/addUser',
        method: 'post',
        data: user
    })
}

// 超级管理员恢复用户
export function recoverUser(umail){
    return request({
        url: `/user/recoverUser/${umail}`,
        method: 'get'
    })
}

// 修改用户权限 1. 将管理员设置为普通用户 / 2. 将普通用户设置为管理员 (超级用户可进行修改)
export function changePrivilege(){

}

// 删除用户 1. 管理员可删除普通用户 / 2. 超级管理员可删除用户和管理员 
export function delUserByUmail(umail){
    return request({
        url: `/user/delUserByUmail/${umail}`,
        method: 'post'
    })
}

// 用户忘记密码
export function resetPassword(umail){
    return request({
        url: `/resetPasswd/resetPassword/${umail}`,
        method: 'post',
    })
}


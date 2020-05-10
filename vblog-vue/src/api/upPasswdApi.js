import request from '@/utils/axiosUtil'

export default {
    
    // 异步请求校验原密码是否正确
    checkPassword(umail, oldPwd) {
        return request({
            url: '/userLog/checkPassword',
            method: 'post',
            data: {
                umail,
                oldPwd
            }
        })
    },
    // 修改密码
    updatePassword(umail, newPwd) {
        return request({
            url: '/userLog/updatePassword',
            method: 'put',
            data: {
                umail,
                newPwd
            }
        })
    }
}
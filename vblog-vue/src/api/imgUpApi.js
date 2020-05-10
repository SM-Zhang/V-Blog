import request from '@/utils/axiosUtil'

// 图片上传(单张)
// headers: { 'Content-Type': 'multipart/form-data' },
export function imgUpload(formdata){
    return request({
        url: '/article/uploadImg',
        method: 'post',
        data: formdata,
        headers: { 'Content-Type': 'multipart/form-data' },
    })
}
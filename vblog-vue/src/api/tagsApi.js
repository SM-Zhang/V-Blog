import request from '@/utils/axiosUtil'

// 根据aid删除标签
export function delTagsByAid(aid){
    return request ({
        url: `/tag/delTagsByAid/${aid}`,
        method: 'get',
    })
}

// 根据 tid 获取当前数据
export function getTagByTid(tid){
    return request ({
        url: `/tag/getTagByTid/${tid}`,
        method: 'get',
    })  
}

// 根据tid删除标签
export function delTagsByTid(tid){
    return request ({
        url: `/tag/delTagsByTid/${tid}`,
        method: 'post',
    })
}

// 查询所有标签 + 分页
export function getTags(currentPage, pageSize, searchForm){
    return request ({
        url: `/tag/getTags/${currentPage}/${pageSize}`,
        method: 'post',
        data: searchForm
    })
}

// 更新当前标签
export function updateTag(tagForm){
    return request ({
        url: `/tag/updateTag`,
        method: 'post',
        data: tagForm
    })
}
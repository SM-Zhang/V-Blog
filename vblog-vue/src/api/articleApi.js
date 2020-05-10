import request from '@/utils/axiosUtil'

// 添加文章
export function addUpArticle(article) {
    return request ({
        url: '/article/addUpArticle',
        method: 'post',
        data: article
    })
};

// 查看文章列表
export function getAllArticles(currentPage, pageSize, searchMap){
    return request({
        url: `/article/getAllArticles/${currentPage}/${pageSize}`,
        method: 'post',
        data: searchMap
    })
};

// 点击某一篇文章，显示文章详情
export function getArticleById(aid){
    return request ({
        url: `/article/getArticleById/${aid}`,
        method: 'get'
    })
};

// 在我的博客中将某一篇文章删除至回收站
// 从回收站恢复至草稿箱
// 在草稿箱中直接发布文章
export function delUpArticleStateById(aid, aState){
    return request ({
        url: '/article/delUpArticleStateById',
        method: 'post',
        data: {
            aid,
            aState
        }
    })

}

// 查看文章数据
export function blogData(uid){
    return request({
        url: `/article/blogData/${uid}`,
        method: 'post'
    })
}

// 从回收站中删除文章
export function delArticleById( aid ){
    return request ({
        url: `/article/delArticleById/${aid}`,
        method: 'post',
    })
}

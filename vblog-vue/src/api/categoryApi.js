import request from '@/utils/axiosUtil'

export default {

    // 1. 查询所有文章分类+分页
    getCategories(currentPage, pageSize, searchForm){
        return request ({
            url: `/category/getCategories/${currentPage}/${pageSize}`,
            method: 'post',
            data: searchForm
        })
    },
    // 2. 根据id查询当前分类
    getCategoryById(cid){
        return request ({
            url: `/category/getCategoryById/${cid}`,
            method: 'get',
        })
    },
    // 3. 添加文章分类
    addCategory(categoryForm){
        return request ({
            url: `/category/addCategory`,
            method: 'post',
            // data传递数据方式1，第一个参数 可以为空
            data: categoryForm
        })
    },
    // 4. 修改文章分类
    updateCategoryById(categoryForm){
        return request ({
            url: `/category/updateCategoryById`,
            method: 'post',
            // data传递数据方式1, 第一个参数 不可以为空
            data: categoryForm
        })
    },
    // 5. 删除文章分类
    delCategoryById(cid){
        return request ({
            url: `/category/delCategoryById/${cid}`,
            method: 'post',
        })
    }

}
package com.zsm.springboot.service;

import com.github.pagehelper.PageInfo;
import com.zsm.springboot.entity.Category;

public interface CategoryService {

    // 1. 查询所有文章分类+分页
    public PageInfo<Category> getCategories(Integer currentPage, Integer pageSize, String search);

    // 2. 根据id查询当前分类
    public Category getCategoryById(Integer cid);

    // 3. 添加文章分类
    public Integer addCategory(Category category);

    // 4. 修改文章分类
    public Integer updateCategoryById(Category category);

    // 5. 删除文章分类
    public Integer delCategoryById(Integer cid);

}

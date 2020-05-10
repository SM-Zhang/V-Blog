package com.zsm.springboot.dao;

import com.zsm.springboot.entity.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryDao {

    // 1. 查询所有文章分类+分页
    public List<Category> getCategories(String search);

    // 2. 根据id查询当前分类
    public Category getCategoryById(Integer cid);

    // 3. 添加文章分类
    public Integer addCategory(Category category);

    // 4. 修改文章分类
    public Integer updateCategoryById(Category category);

    // 5. 删除文章分类
    public Integer delCategoryById(Integer cid);

}

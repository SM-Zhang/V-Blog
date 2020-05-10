package com.zsm.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsm.springboot.dao.CategoryDao;
import com.zsm.springboot.entity.Article;
import com.zsm.springboot.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private ArticleService articleService;

    @Override
    public PageInfo<Category> getCategories(Integer currentPage, Integer pageSize, String search) {
        PageHelper.startPage(currentPage, pageSize);
        List<Category> categories = categoryDao.getCategories(search);
        PageInfo<Category> categoryPageInfo = new PageInfo<>(categories);
        return categoryPageInfo;
    }

    @Override
    public Category getCategoryById(Integer cid) {
        Category categoryById = categoryDao.getCategoryById(cid);
        return categoryById;
    }

    @Override
    public Integer addCategory(Category category) {
        Integer integer = categoryDao.addCategory(category);
        return integer;
    }

    @Override
    public Integer updateCategoryById(Category category) {
        Integer integer = categoryDao.updateCategoryById(category);
        return integer;
    }

    @Override
    public Integer delCategoryById(Integer cid) {
        Article article = articleService.checkArticleByCid(cid);
        if (article != null){ // 已绑定
            return 99;
        }else {
            Integer integer = categoryDao.delCategoryById(cid);
            return integer;
        }
    }
}

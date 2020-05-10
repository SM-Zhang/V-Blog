package com.zsm.springboot.service;

import com.github.pagehelper.PageInfo;
import com.zsm.springboot.entity.Article;
import com.zsm.springboot.entity.User;

import java.util.List;

public interface ArticleService {

    // 添加文章
    public Integer addArticle(Article article);

    // 根据当前文章 id 查询文章详情
    public Article getArticleById(Integer aid);

    // 查询 当前用户文章 或者 所有文章
    public PageInfo<Article> getAllArticles(User user, Integer currentPage, Integer pageSize);

    // 根据文章id修改当前文章
    public Integer updateArticleById(Article article);

    // 根据文章id
    //      1. 删除当前文章，删除到回收站中，修改a_state
    //      2. 从回收站中恢复
    //      3. 从草稿箱中直接发布
    public Integer delUpArticleStateById(Article article);

    // 1. 已发布的博客的数量
    public Integer getAllArticlesCount(Integer uid);
    // 2. 私密文章的数量
    public Integer getPriArticlesCount(Integer uid);
    // 3. 草稿箱中的数量
    public Integer getDraArticlesCount(Integer uid);
    // 4. 远程回收站的数量
    public Integer getRecArticlesCount(Integer uid);

    // 根据文章id删除当前文章，彻底删除，修改udel
    public Integer delArticleById(Integer aid);

    // 查询当前文章绑定的 类型
    public Article checkArticleByCid(Integer cid);
}

package com.zsm.springboot.dao;

import com.zsm.springboot.entity.Article;
import com.zsm.springboot.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleDao {

    // 添加文章
    public int addArticle(Article article);

    // 根据当前文章 id 查询文章详情
    public Article getArticleById(Integer aid);

    // 查询 当前用户文章 或者 所有文章
    public List<Article> getAllArticles(User user);

    // 查询当前用户下的草稿箱中的文章
    public List<User> getArticleByDraft(User user);

    // 查询当前用户下的回收站中的文章
    public List<User> getArticleByRecycle(User user);

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
    public Integer delArticleById(@Param("aid") Integer aid, @Param("adelTime") String adelTime);

    // 查询当前文章绑定的 类型
    public Article checkArticleByCid(Integer cid);

}

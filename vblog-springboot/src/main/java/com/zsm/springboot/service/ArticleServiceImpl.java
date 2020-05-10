package com.zsm.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsm.springboot.dao.ArticleDao;
import com.zsm.springboot.entity.Article;
import com.zsm.springboot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private TagService tagService;

    /**
     * 添加文章 || 草稿箱
     * @param article
     * @return
     */
    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public Integer addArticle(Article article) {
        // 处理文章摘要
        if(article.getSummary() == null || "".equals(article.getSummary())){
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        if(article.getaState() != null || !article.getaState().equals("")){
            article.setCreateDate(nowTime()); // 文章第一次发布时间
            article.setPublishDate(nowTime());// 文章最后一次发布或修改时间
            article.setOrderTime(System.currentTimeMillis()); // 根据文章发布的时间进行排序
            article.setAdel(1);
            article.setAdelTime("未删除"); // 文章未删除
            Integer aid = articleDao.addArticle(article); // 添加文章
            System.out.println("aid ==== " + article.getAid());
            // 添加标签
            Integer tagid = tagService.addTagsByAid(article.getTagList(), article.getAid());
            if(tagid != -1 && tagid != null){
                return aid;
            }else {
                return -1;
            }
        }else { // 表示状态为空，无法插入数据
            return -1;
        }
    }


    @Override
    public Article getArticleById(Integer aid) {
        Article article = articleDao.getArticleById(aid);
        return article;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageInfo<Article> getAllArticles(User user, Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage , pageSize);
        List<Article> allArticles = articleDao.getAllArticles(user);
        PageInfo<Article> pageInfo = new PageInfo<>(allArticles);
        return pageInfo;
    }

    @Transactional
    @Override
    @SuppressWarnings("unchecked")
    public Integer updateArticleById(Article article) {
        // 处理文章摘要
        if(article.getSummary() == null || "".equals(article.getSummary())){
            String stripHtml = stripHtml(article.getHtmlContent());
            article.setSummary(stripHtml.substring(0, stripHtml.length() > 50 ? 50 : stripHtml.length()));
        }
        if(article.getaState() != null || !article.getaState().equals("")){
            article.setPublishDate(nowTime());// 文章最后一次发布或修改时间
            article.setOrderTime(System.currentTimeMillis()); // 根据文章发布的时间进行排序
            // 先清除原来与之绑定的标签
            Integer delTagsByAid = tagService.delTagsByAid(article.getAid());
            // 添加标签
            Integer tagid = tagService.addTagsByAid(article.getTagList(), article.getAid());
            Integer aid = articleDao.updateArticleById(article); // 修改文章
            System.out.println("aid + upArticle ==== " + article.getAid());
            if(tagid != -1 && tagid != null && delTagsByAid != null){
                return aid;
            }else {
                return -1;
            }
        }else { // 表示状态为空，无法插入数据
            return -1;
        }
    }

    @Override
    public Integer delUpArticleStateById(Article article) {
        Integer integer = articleDao.delUpArticleStateById(article);
        return integer;
    }

    @Override
    public Integer getAllArticlesCount(Integer uid) {
        Integer integer = articleDao.getAllArticlesCount(uid);
        return integer;
    }

    @Override
    public Integer getPriArticlesCount(Integer uid) {
        Integer integer = articleDao.getPriArticlesCount(uid);
        return integer;
    }

    @Override
    public Integer getDraArticlesCount(Integer uid) {
        Integer integer = articleDao.getDraArticlesCount(uid);
        return integer;
    }

    @Override
    public Integer getRecArticlesCount(Integer uid) {
        Integer integer = articleDao.getRecArticlesCount(uid);
        return integer;
    }

    @Override
    public Integer delArticleById(Integer aid) {
        Integer integer = articleDao.delArticleById(aid, nowTime());
        return integer;
    }

    // 查询当前文章绑定的 类型
    @Override
    public Article checkArticleByCid(Integer cid) {
        Article article = articleDao.checkArticleByCid(cid);
        return article;
    }

    private String stripHtml(String content) {
        content = content.replaceAll("<p .*?>", "");
        content = content.replaceAll("<br\\s*/?>", "");
        content = content.replaceAll("\\<.*?>", "");
        return content;
    }

    private String nowTime(){
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return nowTime;
    }

}

package com.zsm.springboot.entity;

import java.util.List;

public class Category {

    private Integer cid;
    private String category;

    private List<Article> articles;

    public Category() {
    }

    public Category(Integer cid, String category, List<Article> articles) {
        this.cid = cid;
        this.category = category;
        this.articles = articles;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "Category{" +
                "cid=" + cid +
                ", category='" + category + '\'' +
                ", articles=" + articles +
                '}';
    }
}

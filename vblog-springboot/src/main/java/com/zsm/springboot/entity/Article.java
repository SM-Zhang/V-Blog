package com.zsm.springboot.entity;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {
    private Integer aid;
    private String title;
    private String summary;
    private String mdContent;
    private String htmlContent;
    private Integer aState;
    private Integer aprivate;
    private String createDate;
    private String publishDate;
    private Long orderTime;
    private String adelTime;
    private Integer adel;
    private Integer aUid;
    private Integer aCategory;

    private List tagList; // 前端 TAGS 参数传递

    private User user;
    private List<Tag> tags;
    private Category category;

    public Article() {
    }

    public Article(Integer aid, String title, String summary, String mdContent, String htmlContent, Integer aState, Integer aprivate, String createDate, String publishDate, Long orderTime, String adelTime, Integer adel, Integer aUid, Integer aCategory, List tagList, User user, List<Tag> tags, Category category) {
        this.aid = aid;
        this.title = title;
        this.summary = summary;
        this.mdContent = mdContent;
        this.htmlContent = htmlContent;
        this.aState = aState;
        this.aprivate = aprivate;
        this.createDate = createDate;
        this.publishDate = publishDate;
        this.orderTime = orderTime;
        this.adelTime = adelTime;
        this.adel = adel;
        this.aUid = aUid;
        this.aCategory = aCategory;
        this.tagList = tagList;
        this.user = user;
        this.tags = tags;
        this.category = category;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getMdContent() {
        return mdContent;
    }

    public void setMdContent(String mdContent) {
        this.mdContent = mdContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Integer getaState() {
        return aState;
    }

    public void setaState(Integer aState) {
        this.aState = aState;
    }

    public Integer getAprivate() {
        return aprivate;
    }

    public void setAprivate(Integer aprivate) {
        this.aprivate = aprivate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public String getAdelTime() {
        return adelTime;
    }

    public void setAdelTime(String adelTime) {
        this.adelTime = adelTime;
    }

    public Integer getAdel() {
        return adel;
    }

    public void setAdel(Integer adel) {
        this.adel = adel;
    }

    public Integer getaUid() {
        return aUid;
    }

    public void setaUid(Integer aUid) {
        this.aUid = aUid;
    }

    public Integer getaCategory() {
        return aCategory;
    }

    public void setaCategory(Integer aCategory) {
        this.aCategory = aCategory;
    }

    public List getTagList() {
        return tagList;
    }

    public void setTagList(List tagList) {
        this.tagList = tagList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Article{" +
                "aid=" + aid +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", mdContent='" + mdContent + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", aState=" + aState +
                ", aprivate=" + aprivate +
                ", createDate='" + createDate + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", orderTime=" + orderTime +
                ", adelTime='" + adelTime + '\'' +
                ", adel=" + adel +
                ", aUid=" + aUid +
                ", aCategory=" + aCategory +
                ", tagList=" + tagList +
                ", user=" + user +
                ", tags=" + tags +
                ", category=" + category +
                '}';
    }
}

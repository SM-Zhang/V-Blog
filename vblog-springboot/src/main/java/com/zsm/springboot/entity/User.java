package com.zsm.springboot.entity;

import java.util.List;

public class User {

    private Integer uid;
    private String umail;
    private String upassword;
    private String uname;
    private String unickname; // 根据昵称搜索文章
    private String uavatar;
    private String uphone;
    private String uintroduce;
    private String ucretime;
    private Integer uroles;
    private String udeltime;
    private Integer udel;
    private Integer ustatus;

    private List<Article> articles;

    // 搜索文章标题,防止重复
    private String title;
    // 搜索文章类型
    private String category;
    // 文章的状态
    private Integer aState;

    public User() {
    }

    public User(Integer uid, String umail, String upassword, String uname, String unickname, String uavatar, String uphone, String uintroduce, String ucretime, Integer uroles, String udeltime, Integer udel, Integer ustatus, List<Article> articles, String title, String category, Integer aState) {
        this.uid = uid;
        this.umail = umail;
        this.upassword = upassword;
        this.uname = uname;
        this.unickname = unickname;
        this.uavatar = uavatar;
        this.uphone = uphone;
        this.uintroduce = uintroduce;
        this.ucretime = ucretime;
        this.uroles = uroles;
        this.udeltime = udeltime;
        this.udel = udel;
        this.ustatus = ustatus;
        this.articles = articles;
        this.title = title;
        this.category = category;
        this.aState = aState;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUmail() {
        return umail;
    }

    public void setUmail(String umail) {
        this.umail = umail;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUnickname() {
        return unickname;
    }

    public void setUnickname(String unickname) {
        this.unickname = unickname;
    }

    public String getUavatar() {
        return uavatar;
    }

    public void setUavatar(String uavatar) {
        this.uavatar = uavatar;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public String getUintroduce() {
        return uintroduce;
    }

    public void setUintroduce(String uintroduce) {
        this.uintroduce = uintroduce;
    }

    public String getUcretime() {
        return ucretime;
    }

    public void setUcretime(String ucretime) {
        this.ucretime = ucretime;
    }

    public Integer getUroles() {
        return uroles;
    }

    public void setUroles(Integer uroles) {
        this.uroles = uroles;
    }

    public String getUdeltime() {
        return udeltime;
    }

    public void setUdeltime(String udeltime) {
        this.udeltime = udeltime;
    }

    public Integer getUdel() {
        return udel;
    }

    public void setUdel(Integer udel) {
        this.udel = udel;
    }

    public Integer getUstatus() {
        return ustatus;
    }

    public void setUstatus(Integer ustatus) {
        this.ustatus = ustatus;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getaState() {
        return aState;
    }

    public void setaState(Integer aState) {
        this.aState = aState;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", umail='" + umail + '\'' +
                ", upassword='" + upassword + '\'' +
                ", uname='" + uname + '\'' +
                ", unickname='" + unickname + '\'' +
                ", uavatar='" + uavatar + '\'' +
                ", uphone='" + uphone + '\'' +
                ", uintroduce='" + uintroduce + '\'' +
                ", ucretime='" + ucretime + '\'' +
                ", uroles=" + uroles +
                ", udeltime='" + udeltime + '\'' +
                ", udel=" + udel +
                ", ustatus=" + ustatus +
                ", articles=" + articles +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", aState=" + aState +
                '}';
    }
}

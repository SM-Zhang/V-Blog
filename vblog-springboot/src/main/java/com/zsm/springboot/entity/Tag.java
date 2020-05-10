package com.zsm.springboot.entity;

public class Tag {

    private Integer tid;
    private String tag;
    private Integer tDel;
    private Integer aTag;

    private Article article;

    public Tag() {
    }

    public Tag(Integer tid, String tag, Integer tDel, Integer aTag, Article article) {
        this.tid = tid;
        this.tag = tag;
        this.tDel = tDel;
        this.aTag = aTag;
        this.article = article;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer gettDel() {
        return tDel;
    }

    public void settDel(Integer tDel) {
        this.tDel = tDel;
    }

    public Integer getaTag() {
        return aTag;
    }

    public void setaTag(Integer aTag) {
        this.aTag = aTag;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tid=" + tid +
                ", tag='" + tag + '\'' +
                ", tDel=" + tDel +
                ", aTag=" + aTag +
                ", article=" + article +
                '}';
    }
}

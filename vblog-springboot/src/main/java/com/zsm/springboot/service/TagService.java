package com.zsm.springboot.service;

import com.github.pagehelper.PageInfo;
import com.zsm.springboot.entity.Tag;

import java.util.List;

public interface TagService {

    // 根据文章 aid 添加标签
    public Integer addTagsByAid(List<String> tagList, Integer aid);

    // 根据 aid 删除标签
    public Integer delTagsByAid(Integer aid);

    // 管理员
    // 1. 根据 tid 删除标签
    public Integer delTagsByTid(Integer tid);

    // 2. 根据 tid 修改标签
    public Integer updateTag(Tag tag);

    // 3. 根据 tid 查询当前标签
    public Tag getTagByTid(Integer tid);

    // 4. 查询所有标签
    public PageInfo<Tag> getTags(Integer currentPage, Integer pageSize, String search);

}

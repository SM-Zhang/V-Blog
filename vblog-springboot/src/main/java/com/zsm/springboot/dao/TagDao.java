package com.zsm.springboot.dao;

import com.zsm.springboot.entity.Tag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TagDao {

    // 根据文章 aid 添加标签
    public Integer addTagsByAid(@Param("tags") List<Object> tags);

    // 根据 aid 删除标签
    public Integer delTagsByAid(@Param("aid") Integer aid);

    // 管理员
    // 1. 根据 tid 删除标签
    public Integer delTagsByTid(Integer tid);

    // 2. 根据 tid 修改标签
    public Integer updateTag(Tag tag);

    // 3. 根据 tid 查询当前标签
    public Tag getTagByTid(Integer tid);

    // 4. 查询所有标签
    public List<Tag> getTags(String search);

}

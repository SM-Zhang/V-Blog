package com.zsm.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsm.springboot.dao.TagDao;
import com.zsm.springboot.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDao tagDao;

    @Override
    public Integer addTagsByAid(List<String> tagList, Integer aid) {
        List<Object> tags = new ArrayList<>();
        System.out.println("tags + aid === " + aid);
        for(int i = 0; i < tagList.size(); i++){
            Tag tag = new Tag();
            tag.setTag(tagList.get(i));
            tag.setaTag(aid);
            tag.settDel(1);
            tags.add(tag);
            System.out.println("tags ======= " + tags);
        }
//        for (String t: tagList) {
//            tag.setTag(t);
//            tag.setaTag(aid);
//            tags.add(tag);
//
//        }
        System.out.println("tags ======= " + tags);
        Integer i = tagDao.addTagsByAid(tags);
        return i;
    }

    @Override
    public Integer delTagsByAid(Integer aid) {
        Integer tagByTid = tagDao.delTagsByAid(aid);
        return tagByTid;
    }

    @Override
    public Integer updateTag(Tag tag) {
        Integer integer = tagDao.updateTag(tag);
        return integer;
    }

    @Override
    public Tag getTagByTid(Integer tid) {
        Tag tagByTid = tagDao.getTagByTid(tid);
        return tagByTid;
    }

    @Override
    public PageInfo<Tag> getTags(Integer currentPage, Integer pageSize, String search) {
        PageHelper.startPage(currentPage, pageSize);
        List<Tag> tags = tagDao.getTags(search);
        PageInfo<Tag> tagPageInfo = new PageInfo<>(tags);
        return tagPageInfo;
    }

    @Override
    public Integer delTagsByTid(Integer tid) {
        Integer integer = tagDao.delTagsByTid(tid);
        return integer;
    }
}

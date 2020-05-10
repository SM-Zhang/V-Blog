package com.zsm.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.zsm.springboot.entity.PageResult;
import com.zsm.springboot.entity.Result;
import com.zsm.springboot.entity.ResultCode;
import com.zsm.springboot.entity.Tag;
import com.zsm.springboot.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/tag")
public class TagController {

    @Autowired
    private TagService tagService;

    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/delTagsByAid/{aid}")
    public Result delTagsByAid(@PathVariable("aid") Integer aid){
        logger.info("删除文章绑定标签");
        Integer tagByTid = tagService.delTagsByAid(aid);
        return tagByTid != 0 ? new Result(ResultCode.DELSUCCESS) : new Result(ResultCode.DELFAIL);
    }

    // 1. 根据 tid 删除标签
    @PostMapping("/delTagsByTid/{tid}")
    public Result delTagsByTid(@PathVariable("tid") Integer tid){
        logger.info("删除标签");
        Integer integer = tagService.delTagsByTid(tid);
        return integer != 0 ? new Result(ResultCode.DELSUCCESS) : new Result(ResultCode.DELFAIL);
    }

    // 2. 根据 tid 修改标签
    @PostMapping("/updateTag")
    public Result updateTag(@RequestBody Tag tag){
        logger.info("修改标签");
        Integer integer = tagService.updateTag(tag);
        return integer != 0 ? new Result(ResultCode.UPDATESUCCESS) : new Result(ResultCode.UPDATEFAIL);
    }

    // 3. 根据 tid 查询当前标签
    @GetMapping("/getTagByTid/{tid}")
    public Result getTagByTid(@PathVariable("tid") Integer tid){
        logger.info("查询当前标签");
        Tag tagByTid = tagService.getTagByTid(tid);
        return tagByTid != null ? new Result(ResultCode.QUERYSUCCESS, tagByTid) : new Result(ResultCode.QUERYFAIL);
    }

    // 4. 查询所有标签
    @PostMapping("/getTags/{currentPage}/{pageSize}")
    public Result getTags(@PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize") Integer pageSize, @RequestBody(required = false) Tag search){
        logger.info("查询分页标签");
        PageInfo<Tag> tags = tagService.getTags(currentPage, pageSize, search.getTag());
        return tags != null ? new Result(ResultCode.QUERYSUCCESS, new PageResult<Tag>(tags.getTotal(), tags.getList())) : new Result(ResultCode.QUERYFAIL);
    }
}

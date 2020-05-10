package com.zsm.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.zsm.springboot.entity.Category;
import com.zsm.springboot.entity.PageResult;
import com.zsm.springboot.entity.Result;
import com.zsm.springboot.entity.ResultCode;
import com.zsm.springboot.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    Logger logger = LoggerFactory.getLogger(getClass());

    // 1. 查询所有文章分类+分页
    @PostMapping("/getCategories/{currentPage}/{pageSize}")
    public Result getCategories(@PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize") Integer pageSize, @RequestBody(required = false) Category search){
        PageInfo<Category> categories = categoryService.getCategories(currentPage, pageSize, search.getCategory());
        logger.info("查询所有分页分类信息");
        return categories != null ? new Result(ResultCode.QUERYSUCCESS, new PageResult<Category>(categories.getTotal(), categories.getList())) : new Result(ResultCode.QUERYFAIL);
    }

    // 2. 根据id查询当前分类
    @GetMapping("/getCategoryById/{cid}")
    public Result getCategoryById(@PathVariable("cid") Integer cid){
        Category categoryById = categoryService.getCategoryById(cid);
        logger.info("查询当前分类");
        return categoryById != null ? new Result(ResultCode.QUERYSUCCESS, categoryById) : new Result(ResultCode.QUERYFAIL);
    }

    // 3. 添加文章分类
    @PostMapping("/addCategory")
    public Result addCategory(@RequestBody Category category){
        Integer addCategory = categoryService.addCategory(category);
        logger.info("添加分类");
        return addCategory != 0 ? new Result(ResultCode.ADDSUCCESS) : new Result(ResultCode.ADDFAIL);
    }

    // 4. 修改文章分类
    @PostMapping("/updateCategoryById")
    public Result updateCategoryById(@RequestBody Category category){
        Integer updateCategoryById = categoryService.updateCategoryById(category);
        logger.info("修改分类");
        return updateCategoryById != 0 ? new Result(ResultCode.UPDATESUCCESS) : new Result(ResultCode.UPDATEFAIL);
    }

    // 5. 删除文章分类
    @PostMapping("/delCategoryById/{cid}")
    public Result delCategoryById(@PathVariable("cid") Integer cid){
        Integer delCategoryById = categoryService.delCategoryById(cid);
        logger.info("删除分类");
        if(delCategoryById == 99){
            return new Result(ResultCode.DELFAILBIND);
        }else {
            return delCategoryById != 0 ? new Result(ResultCode.DELSUCCESS) : new Result(ResultCode.DELFAIL);
        }
    }
}

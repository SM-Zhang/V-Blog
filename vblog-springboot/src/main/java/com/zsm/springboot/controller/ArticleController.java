package com.zsm.springboot.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.zsm.springboot.entity.*;
import com.zsm.springboot.service.ArticleService;
import com.zsm.springboot.utils.UploadImgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UploadImgUtil uploadImgUtil;

    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping("/addUpArticle")
    public Result addUpArticle(@RequestBody Article article) {
        int i = 0;
        if(article.getAid() == null || article.getAid().equals("")){
            // 1. 添加文章
            i = articleService.addArticle(article);
            // 3. i 不等于0，则证明插入数据库成功
            logger.info("添加文章 >>> " + article);
            return i == 1 ? new Result(ResultCode.ADDSUCCESS) : new Result(ResultCode.ADDFAIL);
        }else {
            // 如果 aid 不为空的话则是修改文章
            i = articleService.updateArticleById(article);
            logger.info("编辑文章 >>> " + article);
            // 3. i 不等于0，则证明插入数据库成功
            return i == 1 ? new Result(ResultCode.UPDATESUCCESS) : new Result(ResultCode.UPDATEFAIL);
        }
    }

    @GetMapping("/getArticleById/{aid}")
    public Result getArticleById(@PathVariable("aid") Integer aid){
        // 1. 查询当前文章
        Article article = articleService.getArticleById(aid);
        logger.info("查询文章 >>> " + aid);
        return article != null ? new Result(ResultCode.QUERYSUCCESS, article) : new Result(ResultCode.QUERYFAIL);
    }

    /**
     * 获取当前用户的文章列表 || 所有文章列表（用户参数为空）
     * @param searchMap
     * @return
     */
    @PostMapping("/getAllArticles/{currentPage}/{pageSize}")
    public Result getAllArticles(@RequestBody(required = false) User searchMap, @PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize") Integer pageSize){
        PageInfo<Article> articles = articleService.getAllArticles(searchMap, currentPage, pageSize);
        PageResult<Article> result = new PageResult<>(articles.getTotal(), articles.getList());
        logger.info("查询分页文章信息 >>> " + searchMap);
        return result != null ? new Result(ResultCode.QUERYSUCCESS, result) : new Result(ResultCode.QUERYFAIL, result);
    }

    /**
     * 1. 图片一张一张上传，但是删除时需要查询数据库，感觉数据库消耗较大(暂时采用方案二)
     * @param image
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadImg")
    public Result uploadImg(HttpServletRequest request, @RequestParam("image") MultipartFile image) throws IOException {
        String url = uploadImgUtil.uploadImg(request, image, null);
        logger.info("文章图片上传 >>> " + url);
        return url != null ? new Result(ResultCode.BASESUCCESS, url) : new Result(ResultCode.UPLOADFAIL);
    }

    /**
     * 1. 根据文章的 aid 将当前文章删除至回收站
     * 2. 根据文章的 aid 将文章从回收站恢复至草稿箱
     * 3. 从草稿箱中直接发布
     * @param article
     * @return
     */
    @PostMapping("/delUpArticleStateById")
    public Result delUpArticleStateById(@RequestBody Article article){
        Integer delArticleByIdToRecycle = articleService.delUpArticleStateById(article);
        logger.info("删除或恢复文章");
        return delArticleByIdToRecycle != 0 ? new Result(ResultCode.BASESUCCESS) : new Result(ResultCode.BASEFAIL);
    }

    /**
     * 将文章从回收站中删除
     * @param aid
     * @return
     */
    @PostMapping("/delArticleById/{aid}")
    public Result delArticleById(@PathVariable("aid") Integer aid){
        Integer integer = articleService.delArticleById(aid);
        logger.info("从回收站中删除文章");
        return integer != 0 ? new Result(ResultCode.DELSUCCESS) : new Result(ResultCode.DELFAIL);
    }

    /**
     * 博客数据统计
     * @param uid
     * @return
     */
    @PostMapping("/blogData/{uid}")
    public Result blogData(@PathVariable("uid") Integer uid){
        Integer all = articleService.getAllArticlesCount(uid);
        Integer pri = articleService.getPriArticlesCount(uid);
        Integer dra = articleService.getDraArticlesCount(uid);
        Integer rec = articleService.getRecArticlesCount(uid);
        Map<String, Object> rows = new HashMap<>();
        rows.put("0", all);
        rows.put("1", pri);
        rows.put("2", dra);
        rows.put("3", rec);
        logger.info("博客数据统计");
        return new Result(ResultCode.BASESUCCESS, rows);
    }

}

/**
 *  2. 将图片统一上传
 *
 * @return
 */
    /*@PostMapping("/uploadImgs")
    public Result uploadImgs(@RequestBody MultipartFile[] _img, HttpServletRequest request) throws IOException {
        // 1. 请求上传路径
        String path = request.getSession().getServletContext().getRealPath("/images/");
        List<String> list = new ArrayList<>();

        System.out.println("path ===== " + path);
        System.out.println("img.length ===== " + _img.length);

        // 2. 是否需要创建文件夹
        if(_img.length != 0){
            File fileDir = new File(path);
            if(!fileDir.exists()){
                fileDir.mkdir();
            }
            // ArticleImgs articleImgs = new ArticleImgs();
            for( int i = 0; i < _img.length; i++ ){
                MultipartFile file = _img[i];
                String filename = file.getOriginalFilename();
                String sub = filename.substring(filename.lastIndexOf("."));
                // 网页可访问的全路径，访问时不需要进行拼接
                filename = path + UUID.randomUUID().toString().replace("-","") + sub;

                System.out.println("filenameList ===== " + filename);

                // 将图片上传到该文件夹下
                file.transferTo(new File(path, filename));
                // 将路径存储在图片实体类中，这种方式没有意义
                // articleImgs.setImgPath(filename);
                // 将其添加到图片实体对应的数据库中
                // int imgs = imgService.addImgs(articleImgs);
                 list.add(filename);
            }
            return new Result(ResultCode.SUCCESS, list);
        }else {
            return new Result(ResultCode.FAIL);
        }
    }
    // 将路径存储在图片实体类中，这种方式没有意义
    // articleImgs.setImgPath(filename);
    // 将其添加到图片实体对应的数据库中
    // int imgs = imgService.addImgs(articleImgs);
    // 但是这种方式会产生很多无效图片，增加服务器压力

    */

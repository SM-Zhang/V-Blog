package com.zsm.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.zsm.springboot.entity.PageResult;
import com.zsm.springboot.entity.Result;
import com.zsm.springboot.entity.ResultCode;
import com.zsm.springboot.entity.User;
import com.zsm.springboot.service.UserService;
import com.zsm.springboot.utils.JwtUtils;
import com.zsm.springboot.utils.SendMailUtil;
import com.zsm.springboot.utils.UploadImgUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SendMailUtil sendMailUtil;

    @Autowired
    private UploadImgUtil uploadImgUtil;

    @Autowired
    TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 根据 token(umail) 获取当前用户信息
     * @param token
     * @return
     */
    @GetMapping("/userInfo/{token}")
    public Result userInfo(@PathVariable("token") String token){
        if(StringUtils.isEmpty(token)){
            return new Result(ResultCode.UNAUTHENTICATED);
        }
        Claims claims = jwtUtils.parseJwt(token);
        String umail = claims.getSubject();
        User user = userService.userByUmail(umail);
        user.setUpassword("");
        logger.info("用户登录 >>> " + user);
        return user != null ? new Result(ResultCode.QUERYSUCCESS, user) : new Result(ResultCode.QUERYFAIL);
    }

    @GetMapping("/userInfoByUmail/{umail}")
    public Result userInfoByUmail(@PathVariable("umail") String umail){
        User userByUmail = userService.userByUmail(umail);
        logger.info("查询用户 >>> " + userByUmail);
        return userByUmail != null ? new Result(ResultCode.QUERYSUCCESS, userByUmail) : new Result(ResultCode.QUERYFAIL);
    }

    /**
     * 管理员可查看所有用户信息
     * @return
     */
    @PostMapping("/getAllUsers/{currentPage}/{pageSize}")
    public Result allUser(@PathVariable("currentPage") Integer currentPage, @PathVariable("pageSize") Integer pageSize, @RequestBody User user){
        PageInfo<User> allUser = userService.getAllUser(currentPage, pageSize, user);
        PageResult<User> userPageResult = new PageResult<>(allUser.getTotal(), allUser.getList());
        logger.info("查询所有用户分页信息 >>> " + user);
        return allUser != null ? new Result(ResultCode.QUERYSUCCESS, userPageResult) : new Result(ResultCode.QUERYFAIL);
    }

    /**
     * 用户头像上传
     * @param uid
     * @param uavatar
     * @return
     */
    @PostMapping("/uploadAvatar/{uid}")
    public Result uploadAvatar(HttpServletRequest request, @PathVariable("uid") Integer uid, @RequestParam("uavatar") MultipartFile uavatar) throws IOException {
        if(uid == null){
            return new Result(ResultCode.UPLOADFAIL);
        }else {
            String url = uploadImgUtil.uploadImg(request, uavatar, uid);
            User user = userService.uploadAvatar(uid, url);
            logger.info("用户头像上传 >>> " + user);
            return user != null ? new Result(ResultCode.UPLOADSUCCESS, user) : new Result(ResultCode.UPLOADFAIL);
        }
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody User user){
        System.out.println("user === " + user);
        userService.updateUser(user);
        User userById = userService.getUserById(user.getUid());
        logger.info("用户修改 >>> " + userById);
        if(userById != null){
            return userById != null ? new Result(ResultCode.UPDATESUCCESS, userById) : new Result(ResultCode.UPDATEFAIL);
        }else
            return new Result(ResultCode.UPDATEFAIL);
    }

    @PostMapping("/delUserByUmail/{umail}")
    public Result delUserByUid(@PathVariable("umail") String umail){
        Integer integer = userService.delUserByUmail(umail);
        logger.info("删除用户 >>> " + umail);
        return integer != 0 ? new Result(ResultCode.DELSUCCESS) : new Result(ResultCode.DELFAIL);
    }

    /**
     * 添加用户 超级管理员 / 管理员 / 用户注册
     * @param user
     * @return
     */
    @PostMapping("/addUser")
    @Transactional
    public Result addUser(@RequestBody User user, HttpServletRequest request) {
        Integer ustatus = user.getUstatus();
        // 用户添加成功后，返回添加用户的 id
        Integer integer = userService.addUser(user);
        if(integer == 99){
            return new Result(ResultCode.INOPERABLE);
        }else if(integer == 88){
            return new Result(ResultCode.EXISTFAIL);
        }else {
            // 用户添加成功后，前往邮箱进行激活认证
            if(ustatus == 9){ // 管理员添加的用户则不需要进行激活认证
                // 发送邮件，用户点击进行激活
                logger.info("用户注册 >>> " + user.getUmail());
                Map<String, Object> map = new HashMap<>();
                map.put("register", "用户注册");
                System.out.println("user.getUid() ============ " + user.getUid());
                String token = jwtUtils.createJwt(String.valueOf(user.getUid()), user.getUmail(), map);
                String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
                String url = webPath + "/validate/regVerify?verify=" + token;
                String content = "您好: " + user.getUmail() + "<br /> " + "<a href=" + url + " \">激活请点击:"+ token + "</a>";
                try {
                    sendMailUtil.sendMail(from, user.getUmail(), "注册激活认证", content);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return new Result(ResultCode.BASEFAIL);
                }
            }
            return new Result(ResultCode.ADDSUCCESS);
        }
    }

    /**
     * 恢复用户
     * @param umail
     * @return
     */
    @GetMapping("/recoverUser/{umail}")
    public Result recoverUser(@PathVariable("umail") String umail){
        Integer integer = userService.recoverUser(umail);
        logger.info("恢复用户 >>> " + umail);
        return integer != 0 ? new Result(ResultCode.RECOVERSUCCESS) : new Result(ResultCode.RECOVERFAIL);
    }



}

package com.zsm.springboot.controller;

import com.zsm.springboot.entity.Result;
import com.zsm.springboot.entity.ResultCode;
import com.zsm.springboot.entity.User;
import com.zsm.springboot.service.UserService;
import com.zsm.springboot.utils.JwtUtils;
import com.zsm.springboot.utils.SendMailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册
 *    1. 根据用户邮箱发送验证码，防止用户错误
 *    2. 注册需要
 *          1. 邮箱
 *          2. 昵称
 *          3. 密码 / 确认密码
 *          4. 验证码
 */

@CrossOrigin
@RestController
@RequestMapping("/resetPasswd")
public class ResetController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private SendMailUtil sendMailUtil;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/resetPassword/{umail}")
    @Transactional
    public Result forgetPassword(@PathVariable("umail") String umail, HttpServletRequest request){
        User user = userService.userByUmail(umail);
        logger.info("密码重置");
        if(user == null){
            return new Result(ResultCode.NOTEXISTFAIL);
        }else {
            // 发送邮件，用户点击进行重置密码
            Map<String, Object> map = new HashMap<>();
            map.put("register", "密码重置");
            System.out.println("user.getUid() ============ " + user.getUid());
            String token = jwtUtils.createJwt(String.valueOf(user.getUid()), user.getUmail(), map);
            String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String url = webPath + "/validate/resetPassword?verify=" + token;
            String content = "您好: " + user.getUmail() + "<br />" + "<a href=" + url + " \">密码重置请点击:"+ token + "</a>";
            try {
                sendMailUtil.sendMail(from, user.getUmail(), "密码重置认证", content);
                return new Result(ResultCode.PASSWDRESETSUCCESS);
            } catch (MessagingException e) {
                e.printStackTrace();
                return new Result(ResultCode.BASEFAIL);
            }
        }
    }

}

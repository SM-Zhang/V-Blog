package com.zsm.springboot.controller;

import com.zsm.springboot.entity.User;
import com.zsm.springboot.service.UserService;
import com.zsm.springboot.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 用户点击激活
 */
@CrossOrigin
@RestController
@RequestMapping("/validate")
public class ValidateMailController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils; // 生成 token

    Logger logger = LoggerFactory.getLogger(getClass());

    // 用户注册认证，点击激活
    @GetMapping("/regVerify")
    public String regVerify(@RequestParam("verify") String token){
        if(StringUtils.isEmpty(token)){
            logger.info("用户激活失败");
            return "激活失败, 请联系管理员-1097646307！";
        }
        Claims claims = jwtUtils.parseJwt(token);
        if(claims != null){
            User userById = userService.getUserById(Integer.valueOf(claims.getId()));
            if(userById != null){
                logger.info("用户重复激活");
                return "您已激活，请勿重复激活！";
            }
            Integer integer = userService.activeUser(Integer.valueOf(claims.getId()));
            if(integer != 0){
                logger.info("用户激活成功");
                return "激活成功, 请登录！";
            }
        }
        logger.info("用户激活失败");
        return "激活失败, 请联系管理员-1097646307！";
    }

    // 忘记密码认证，重置密码
    @GetMapping("/resetPassword")
    public String resetPassword(@RequestParam("verify") String token){
        if(StringUtils.isEmpty(token)){
            logger.info("密码重置失败");
            return "重置失败, 请联系管理员-1097646307！";
        }
        Claims claims = jwtUtils.parseJwt(token);
        if(claims != null){
            Integer resCode = (int) (100000 + Math.random() * 90000);
            Integer integer = userService.resetPassword(claims.getSubject(), String.valueOf(resCode));
            if(integer != 0){
                logger.info("密码重置成功");
                return "您的密码重置为：" + resCode + " 请尽快修改！";
            }
        }
        logger.info("密码重置失败");
        return "重置失败, 请联系管理员-1097646307！";
    }

}

package com.zsm.springboot.controller;

import com.zsm.springboot.entity.Result;
import com.zsm.springboot.entity.ResultCode;
import com.zsm.springboot.entity.User;
import com.zsm.springboot.service.UserService;
import com.zsm.springboot.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * 对用户的  1. 登录
 *          2. 密码校验
 *          3. 修改密码
 *          4. 退出登录
 */

@CrossOrigin
@RestController
@RequestMapping("/userLog")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用户登录，校验前端传递的 uamil, upassword
     * 登录成功后返回 token
     * "code": xxx
     * "flag": true|false
     * "data": {
     *     "token": "xxx"
     * }
     * @return
     */
    @PostMapping("/login")
    public Result loginByUmail(@RequestBody Map<String,String> loginForm){
        Map<String, Object> map1 = new HashMap<>();
        String token = "";
        String umail = loginForm.get("umail");
        String upassword = loginForm.get("upassword");
        User userByUmail = userService.userByUmail(umail);
        if(userByUmail == null){ // 当前用户不存在
            map1.put("token", token);
            logger.info("当前用户不存在");
            return new Result(ResultCode.NOTEXISTFAIL, map1);
        }else if(!userByUmail.getUpassword().equals(upassword)){ // 登录失败
            logger.info("用户登录失败");
            map1.put("token", token);
            return new Result(ResultCode.MAILORPASSWORDERROR, map1);
        }else{
            Map<String, Object> map = new HashMap<>();
            map.put("uname", userByUmail.getUname());
            map.put("unickname", userByUmail.getUnickname());
            token = jwtUtils.createJwt(String.valueOf(userByUmail.getUid()), userByUmail.getUmail(), map);
            map1.put("token", token);
            logger.info("用户登录成功 >>> " + umail);
            return new Result(ResultCode.LOGINSUCCESS, map1);
        }
    }

    /**
     * 密码校验
     * @param checkPwd
     * @return
     */
    @PostMapping("/checkPassword")
    public Result checkPassword(@RequestBody Map<String,String> checkPwd){
        String umail = checkPwd.get("umail");
        String oldPwd = checkPwd.get("oldPwd");
        User userByUmail = userService.userByUmail(umail);
        logger.info("密码校验");
        if(userByUmail.getUpassword() != null && userByUmail.getUpassword().equals(oldPwd)){
            // 密码校验成功，返回状态信息，可以修改密码
            return new Result(ResultCode.PASSWORDCHECKSUCCESS);
        }else{
            // 密码校验失败，返回状态信息，不可以修改密码
           return new Result(ResultCode.PASSWORDCHECKERROR);
        }
    }

    /**
     *忘记密码
     * @return
     */
    @PostMapping("/forgetPassword/{umail}")
    public Result forgetPassword(@PathVariable("umail") String umail){
        User userByUmail = userService.userByUmail(umail);
        logger.info("忘记密码");
        if(userByUmail.getUstatus() == 9){ // 未激活
            return new Result(ResultCode.INACTIVITY);
        }else if(userByUmail == null){ // 验证失败，用户不存在
            return new Result(ResultCode.NOTEXISTFAIL);
        }else{
            // 重置密码，发送邮件，点击进行重置密码
            return new Result(ResultCode.PASSWDRESETSUCCESS);
        }
    }

    /**
     * 修改密码
     * @param pwd
     * @return
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String,String> pwd){
        String umail = pwd.get("umail");
        String newPwd = pwd.get("newPwd");
        logger.info("密码修改");
        Integer flag = userService.upPasswordByUmail(umail, newPwd);
        return flag != 0 ? new Result(ResultCode.PASSWORDUPSUCCESS) : new Result(ResultCode.PASSWORDUPSUCCESS);
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @PostMapping("/logout")
    public Result logout(@RequestBody String token){
        logger.info("退出登录");
        token = null;
        System.out.println("loginController2222222 === " + token);
        return token == null ? new Result(ResultCode.BASESUCCESS) : new Result(ResultCode.BASEFAIL);
    }

}

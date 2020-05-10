package com.zsm.springboot.service;

import com.github.pagehelper.PageInfo;
import com.zsm.springboot.entity.User;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface UserService {

    // 用户登录，根据用户账号进行登录
    // 根据邮箱查询用户密码(校验密码)
    public User userByUmail(String umail);

    // 根据用户id查询当前用户
    public User getUserById(Integer uid);

    // 根据邮箱修改用户密码
    public Integer upPasswordByUmail(String umail, String newPwd);

    // 获取所有用户信息
    public PageInfo<User> getAllUser(Integer cp, Integer ps, User user);

    // 上传用户头像
    public User uploadAvatar(Integer uid, String url) throws IOException;

    // 更新用户信息
    public Integer updateUser(User user);

    // 添加用户 超级管理员 / 管理员
    public Integer addUser(User user);

    // 激活用户
    public Integer activeUser(Integer uid);

    // 用户重置密码
    public Integer resetPassword(String umail, String upassword);

    // 删除用户
    public Integer delUserByUmail(String umail);

    // 恢复用户
    public Integer recoverUser(String umail);

}

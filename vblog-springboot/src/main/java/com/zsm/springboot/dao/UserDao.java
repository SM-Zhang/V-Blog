package com.zsm.springboot.dao;

import com.zsm.springboot.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {

    // 用户登录，根据用户账号进行登录
    // 根据邮箱查询用户密码(校验密码)
    public User userByUmail(String umail);

    // 根据邮箱修改用户密码
    public Integer upPasswordByUmail(String umail, String newPwd);

    // 获取所有用户信息
    public List<User> getAllUsers(User user);

    // 上传用户头像
    public Integer uploadAvatar(@Param("uid") Integer uid, @Param("imgUrl") String imgUrl);

    // 根据用户id查询当前用户
    public User getUserById(Integer uid);

    // 更新用户信息
    public Integer updateUser(User user);

    // 添加 / 注册 用户之前查看是否有该用户信息
    public User getUserBeforeAddByUmail(String umail);

    // 添加用户 超级管理员 / 管理员
    public Integer addUser(User user);

    // 激活用户
    public Integer activeUser(Integer uid);

    // 用户重置密码
    public Integer resetPassword(@Param("umail") String umail, @Param("upassword") String upassword);

    // 删除用户
    public Integer delUserByUmail(User user);

    // 恢复用户
    public Integer recoverUser(User user);

}

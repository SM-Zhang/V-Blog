package com.zsm.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zsm.springboot.dao.UserDao;
import com.zsm.springboot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User userByUmail(String umail) {
        User userByUmail = userDao.userByUmail(umail);
        return userByUmail;
    }

    @Override
    public User getUserById(Integer uid){
        User userById = userDao.getUserById(uid);
        return userById;
    }

    @Override
    public Integer upPasswordByUmail(String umail, String newPwd) {
        Integer flag = userDao.upPasswordByUmail(umail, newPwd);
        return flag;
    }

    @Override
    public PageInfo<User> getAllUser(Integer currentPage, Integer pageSize, User user) {
        if(user.getUroles() == 1 || user.getUroles().equals("") || user.getUroles() == null){
            return null;
        }
        PageHelper.startPage(currentPage, pageSize);
        List<User> userList = userDao.getAllUsers(user);

        System.out.println("UserServiceImpl + userList ====== " + "\n" + userList.size());

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        return pageInfo;
    }

    /**
     * 浏览器支持受限，放弃base64存储头像
     *         String url = Base64.encode(uavater.getBytes());
     *         String imgUrl = "data:image/jpg;base64," + url;
     *         System.out.println("imgUrl ==== " + imgUrl);
     *         Integer a = userDao.uploadAvatar(uid, imgUrl);
     *         User userById = userDao.getUserById(uid);
     *         System.out.println("userById ==== " + userById);
     * @return
     * @throws IOException
     */
    @Override
    public User uploadAvatar(Integer uid, String url) throws IOException {
        if(url == null){
            return null;
        }else{
            Integer a = userDao.uploadAvatar(uid, url);
            User userById = userDao.getUserById(uid);
            return a == 1 ? userById : null;
        }
    }

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    @Transactional
    @Override
    public Integer updateUser(User user) {
        /*if(user.getUpassword() != null) {
            userDao.upPasswordByUmail(user.getUmail(), user.getUpassword());
        }*/
        Integer updateUser = userDao.updateUser(user);
        return updateUser;
    }

    @Override
    public Integer addUser(User user) {
        User userBeforeAddByUmail = userDao.getUserBeforeAddByUmail(user.getUmail());
        if(userBeforeAddByUmail != null){
            if(userBeforeAddByUmail.getUdel() == 9){
                // 该用户不可被操作
                return 99;
            }else{
                // 该用户已被注册
                return 88;
            }
        }else {
            // 可以添加用户
            if(user.getUpassword() == null){
                user.setUpassword("666666");
            }
            user.setUname("请修改");
            if(user.getUnickname() == null){
                user.setUnickname("请修改");
            }
            user.setUavatar("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png");
            user.setUphone("请修改");
            user.setUintroduce("请修改");
            user.setUcretime(nowTime());
            user.setUdeltime("未删除");
            user.setUroles(1);
            user.setUdel(1);
            Integer integer = userDao.addUser(user);
            return integer;
        }
    }

    @Override
    public Integer activeUser(Integer uid) {
        Integer integer = userDao.activeUser(uid);
        return integer;
    }

    @Override
    public Integer resetPassword(String umail, String upassword) {
        Integer integer = userDao.resetPassword(umail, upassword);
        return integer;
    }

    @Override
    public Integer delUserByUmail(String umail) {
        User user = new User();
        user.setUmail(umail);
        user.setUdeltime(nowTime());
        Integer integer = userDao.delUserByUmail(user);
        System.out.println("delUserByUmail === " + integer);
        return integer;
    }

    @Override
    public Integer recoverUser(String umail) {
        User user = new User();
        user.setUmail(umail);
        user.setUpassword("666666");
        user.setUname("请修改");
        user.setUnickname("请修改");
        user.setUintroduce("请修改");
        user.setUroles(1);
        user.setUdeltime(nowTime());
        user.setUdel(1);
        Integer integer = userDao.recoverUser(user);
        return integer;
    }

    private String nowTime(){
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return nowTime;
    }


}

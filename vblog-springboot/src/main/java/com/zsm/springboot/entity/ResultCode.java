package com.zsm.springboot.entity;

/**
 * 公共返回码
 *     返回码 code:
 *          成功： 10000
 *          失败： 10001
 *          未登录：10002
 *          未授权：10003
 *          抛出异常：99999
 */
public enum ResultCode {

    BASESUCCESS(true, 10000, "操作成功"),
    ADDSUCCESS(true, 10001, "新增成功"),
    DELSUCCESS(true, 10002, "删除成功"),
    UPDATESUCCESS(true, 10003, "更新成功"),
    QUERYSUCCESS(true, 10004, "查询成功"),
    LOGINSUCCESS(true, 10005, "登录成功"),
    PASSWORDCHECKSUCCESS(true, 10006, "密码校验正确"),
    PASSWORDUPSUCCESS(true, 10007, "密码修改成功, 请重新登录"),
    UPLOADSUCCESS(true, 10008, "上传成功"),
    RECOVERSUCCESS(true, 10009, "恢复成功"),
    PASSWDRESETSUCCESS(true, 10010, "请前往邮箱重置密码！"),

    BASEFAIL(false,10050,"操作失败"),
    ADDFAIL(false,10051,"新增失败"),
    DELFAIL(false,10052,"删除失败"),
    DELFAILBIND(false,10053,"删除失败,当前类型已绑定文章！"),
    UPDATEFAIL(false,10054,"更新失败"),
    QUERYFAIL(false,10055,"查询失败"),
    MAILORPASSWORDERROR(false, 10056, "邮箱或密码错误"),
    PASSWORDCHECKERROR(false, 10057, "密码校验错误"),
    PASSWORDUPFAIL(false,10058,"密码修改失败"),
    UPLOADFAIL(false,10059,"上传失败"),
    RECOVERFAIL(false, 10060, "恢复失败"),

    UNAUTHENTICATED(false,10100,"您还未登录"),
    UNAUTHORISED(false,10101,"权限不足"),
    EXISTFAIL(false,10102,"当前用户已存在"),
    NOTEXISTFAIL(false,10103,"当前用户不存在"),
    INACTIVITY(false,10104,"您未激活，请前往邮箱激活！"),
    INOPERABLE(false,10105,"此账户不可被操作"),


    SERVER_ERROR(false,99999,"抱歉，系统繁忙，请稍后重试！");

    //操作是否成功
    boolean flag;
    //操作代码
    int code;
    //提示信息
    String message;

    ResultCode(boolean flag,int code, String message){
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public boolean flag() {
        return flag;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

}

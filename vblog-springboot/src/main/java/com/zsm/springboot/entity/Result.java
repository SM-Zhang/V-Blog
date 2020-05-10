package com.zsm.springboot.entity;

public class Result {

    private Integer code; // 返回码
    private boolean flag; // 是否成功
    private String message; // 返回信息
    private Object data; // 返回数据

    public Result(ResultCode code) {
        this.flag = code.flag;
        this.code = code.code;
        this.message = code.message;
    }

    public Result(ResultCode code,Object data) {
        this.flag = code.flag;
        this.code = code.code;
        this.message = code.message;
        this.data = data;
    }

    public Result(Integer code,String message,boolean flag) {
        this.code = code;
        this.message = message;
        this.flag = flag;
    }

    // 自定义无数据返回时返回的信息
    public static Result SUCCESS(){
        return new Result(ResultCode.BASESUCCESS);
    }

    public static Result ERROR(){
        return new Result(ResultCode.SERVER_ERROR);
    }

    public static Result FAIL(){
        return new Result(ResultCode.BASEFAIL);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

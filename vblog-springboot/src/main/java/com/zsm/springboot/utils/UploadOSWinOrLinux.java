package com.zsm.springboot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 图片上传至服务器
 * 测试使用windows
 * 正式使用的服务器为linux
 */
@Component
@ConfigurationProperties(prefix = "uploadpath.location")
public class UploadOSWinOrLinux {

    private String windows;

    private String linux;

    public String getWindows() {
        return windows;
    }

    public void setWindows(String windows) {
        this.windows = windows;
    }

    public String getLinux() {
        return linux;
    }

    public void setLinux(String linux) {
        this.linux = linux;
    }

    public String getOSPath(){
        String path = "";
        String OS = System.getProperty("os.name");
        if (OS.toLowerCase().startsWith("win")){
            // 匹配 Windows
            path = windows;
        }else {
            // 匹配 Linux
            path = linux;
        }
        return path;
    }
}

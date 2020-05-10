package com.zsm.springboot.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 图片上传工具类
 */
@Component
public class UploadImgUtil {

    @Autowired
    private UploadOSWinOrLinux uploadOSWinOrLinux;

    public String uploadImg(HttpServletRequest request, MultipartFile file, @RequestParam(required = false) Integer uid) throws IOException {
        // 1. 获取系统类型，返回存储路径
        String osPath = uploadOSWinOrLinux.getOSPath();
        // 2. 请求上传路径
        String path = "";
        // 2. 图片获取文件夹路径
        String webPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        if(uid == null){
            path = osPath + "articleImgs/";
            webPath = webPath + "/images/articleImgs/";
        }else {
            path = osPath + "avatar/";
            webPath = webPath + "/images/avatar/";
        }
        String filename = file.getOriginalFilename();
        if(!filename.equals("") || filename != null){
            // 3. 是否需要创建文件夹
            File fileDir = new File(path);
            if(!fileDir.exists()){
                fileDir.mkdirs();
            }
            // 4. 截取图片字符串后缀
            String subSuffix = filename.substring(filename.lastIndexOf("."));
            // 5. 使用 UUID 生成图片名
            filename = UUID.randomUUID().toString().replace("-", "") + subSuffix;
            // 6. 上传文件
            file.transferTo(new File(path, filename));
            String url = webPath + filename;
            return url != null ? url : null;
        }else {
            return null;
        }
    }

}

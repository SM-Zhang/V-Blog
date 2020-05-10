package com.zsm.springboot;

import com.zsm.springboot.utils.JwtUtils;
import com.zsm.springboot.utils.UploadImgUtil;
import com.zsm.springboot.utils.UploadOSWinOrLinux;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Properties;

@SpringBootApplication
@MapperScan("com.zsm.springboot.dao")
@EnableAsync
public class SpringbootDemo02Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDemo02Application.class, args);
    }

//    @Bean
//    public JwtUtils jwtUtils() {
//        return new JwtUtils();
//    }
//
//    @Bean
//    public UploadImgUtil uploadImgUtil(){
//        return new UploadImgUtil();
//    }
//
//    @Bean
//    public UploadOSWinOrLinux uploadOSWinOrLinux(){
//        return new UploadOSWinOrLinux();
//    }

}

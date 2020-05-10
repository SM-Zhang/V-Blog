package com.zsm.springboot.config;

import com.zsm.springboot.interceptor.AuthorisedInterceptor;
import com.zsm.springboot.utils.UploadOSWinOrLinux;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Component
public class MySpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    private UploadOSWinOrLinux uploadOSWinOrLinux;

    // 因为添加了拦截器，不能直接new,需手动添加到容器中
    @Bean
    public AuthorisedInterceptor getAuthorisedInterceptor(){
        return new AuthorisedInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String osPath = uploadOSWinOrLinux.getOSPath();
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file://" + osPath);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("public void addInterceptors(InterceptorRegistry registry) {");
        registry.addInterceptor(getAuthorisedInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/userLog/login", "/user/addUser", "/resetPasswd/resetPassword/**", "/validate/regVerify", "/validate/resetPassword", "/user/uploadAvatar/**")
                .excludePathPatterns("/images/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("PUT", "DELETE", "GET", "POST")
                .allowedHeaders("*")
                .exposedHeaders("access-control-allow-headers",
                        "access-control-allow-methods",
                        "access-control-allow-origin",
                        "access-control-max-age",
                        "X-Frame-Options",
                        "Authorization")
                .allowCredentials(false).maxAge(3600);
    }
}

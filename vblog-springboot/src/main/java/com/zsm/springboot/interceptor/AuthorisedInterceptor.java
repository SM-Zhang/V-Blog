package com.zsm.springboot.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.zsm.springboot.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthorisedInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        System.out.println("request.getRequestURI() === " + request.getRequestURI());
        // 请求头部 token
        String authorization = request.getHeader("Authorization");
        System.out.println("authorization === " + authorization);
        if(authorization != null){
            String[] arrToken = authorization.split("\\s+");
            System.out.println(arrToken[1]);
            Claims claims = jwtUtils.parseJwt(arrToken[1]);
            if(claims != null){
                return true;
            }else
                return false;
        }else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            try{
                JSONObject json = new JSONObject();
                json.put("flag", false);
                json.put("code","10100");
                json.put("message","您还未登录");
                response.getWriter().append(json.toString());
                System.out.println("FAIL ============= ");
                return false;
            }catch (Exception e){
                e.printStackTrace();
                response.sendError(500);
                return false;
            }
        }
    }


}

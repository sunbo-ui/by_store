package com.mysb.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CustomizeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    LoginSerivce loginSerivce;

    public void setLoginSerivce(LoginSerivce loginSerivce) {
        this.loginSerivce = loginSerivce;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", 200);
        map.put("message", "登录成功");
        map.put("Authorization", uuid);
        map.put("username", authentication.getName());
        loginSerivce.saveToken(uuid, authentication.getName());
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.write(objectMapper.writeValueAsString(map));
        out.flush();
        out.close();
    }
}
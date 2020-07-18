package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysb.core.service.LoginSerivce;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {
    @Reference
    private LoginSerivce loginSerivce;

    @RequestMapping("/user/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {//清除认证
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        try {
            loginSerivce.deleteToken(request.getHeader("Authorization"));
            System.out.println("logout");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 200);
            map.put("message", "用户名注销成功");
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            out.write(objectMapper.writeValueAsString(map));
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

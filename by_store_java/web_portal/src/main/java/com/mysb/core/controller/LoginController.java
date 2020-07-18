package com.mysb.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mysb.core.pojo.entry.Result;
import com.mysb.core.service.LoginSerivce;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
    @Reference
    private LoginSerivce loginSerivce;
    @RequestMapping("/username")
    public String Username(HttpServletRequest request, HttpServletResponse response){
        String jsessionid = request.getSession().getId();
        // 前端地址
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(name+jsessionid);
        loginSerivce.saveToken(jsessionid,name);
        String url = "http://localhost:9002/login";
        url = url + "?jsessionid=" + jsessionid;
        return "redirect:"+url;
    }
    @RequestMapping("/user/login")
    @ResponseBody
    public Result index(HttpServletRequest request) {
        try {
            String jsessionid = request.getHeader("jsessionid");
            String username = loginSerivce.selectUsernameByToken(jsessionid);
            System.out.println(username);
            return new Result(true, username);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "用户名或密码错误");
        }
    }
    @RequestMapping("/logout")
    @ResponseBody
    public Result logout(HttpServletRequest request) {
        try {
            String jsessionid = request.getHeader("jsessionid");
            loginSerivce.deleteToken(jsessionid);
            request.getSession().invalidate();
            return new Result(true,"成功退出");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"退出失败");
        }
    }
}

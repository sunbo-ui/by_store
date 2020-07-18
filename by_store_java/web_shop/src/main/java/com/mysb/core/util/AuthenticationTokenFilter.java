package com.mysb.core.util;

import com.mysb.core.service.LoginSerivce;
import com.mysb.core.service.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationTokenFilter extends OncePerRequestFilter {


    LoginSerivce loginSerivce;

    public void setLoginSerivce(LoginSerivce loginSerivce) {
        this.loginSerivce = loginSerivce;
    }

    UserDetailServiceImpl userDetailService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");//获取header中的验证信息
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            ServletContext sc = request.getSession().getServletContext();//通过HttpServletRequestServletContext
            XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);
            if (cxt != null && cxt.getBean("loginSerivce") != null && loginSerivce == null)
                loginSerivce = (LoginSerivce) cxt.getBean("loginSerivce");

            if (cxt != null && cxt.getBean("userDetailService") != null && userDetailService == null)
                userDetailService = (UserDetailServiceImpl) cxt.getBean("userDetailService");

            String username = loginSerivce.selectUsernameByToken(token);
            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
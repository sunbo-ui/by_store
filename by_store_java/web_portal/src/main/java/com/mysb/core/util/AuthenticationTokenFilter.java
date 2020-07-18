package com.mysb.core.util;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String jsessionid = request.getHeader("jsessionid");
        if(jsessionid != null){
            String url = request.getRequestURI();
            if(url.indexOf("?") == -1){
               url = url +"?ticket="+jsessionid;
            }else{
                url = url +"&ticket="+jsessionid;
            }
            System.out.println(url);
            RequestDispatcher rd = request.getRequestDispatcher(url);
            try {
                rd.forward(request, response);
                return;
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        chain.doFilter(request, response);
    }

}
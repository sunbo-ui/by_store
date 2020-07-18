package com.mysb.core.util;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SimpleSCORSFilter implements Filter {
    private boolean isCross = false;

    @Override
    public void destroy() {
        isCross = false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (isCross) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            System.out.println("拦截请求: " + httpServletRequest.getServletPath());
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:9003");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT, OPTIONS");
            httpServletResponse.setHeader("Access-Control-Max-Age", "86400");
            httpServletResponse.setHeader("Access-Control-Allow-Headers",
                    "Origin, No-Cache, X-Requested-With, If-Modified-Since,x_requested_with," +
                            " Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,jsessionid");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            if(httpServletRequest.getMethod().toUpperCase().equals("OPTIONS")){
                return;
            }

        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String isCrossStr = filterConfig.getInitParameter("IsCross");
        isCross = isCrossStr.equals("true") ? true : false;
        System.out.println(isCrossStr);

    }
}
package com.itheima.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class AJAXFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        //添加设置浏览器的响应头
        //允许AJAX跨域访问,允许浏览器保存cookie
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;
        request.setCharacterEncoding("utf-8");
        //允许AJAX跨域访问
        response.setHeader("Access-Control-Allow-Origin", "http://www.itheima326.com:8020");
        //AJAX访问允许客户端保存cookie
        response.setHeader("Access-Control-Allow-Credentials","true");
        chain.doFilter(request, response);
    }

    public void init(FilterConfig config) throws ServletException {

    }

}

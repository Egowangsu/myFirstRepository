package com.wyx.crm.settings.web;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        //监听器，使每个do文件（每个命名.do的Servlet文件）的请求和响应都重新写入utf-8的格式，然后放行
        req.setCharacterEncoding("utf-8");  //写过来的
        resp.setContentType("application/json;charset=utf-8");  //写回去的
        chain.doFilter(req, resp);  //放行
    }

    @Override
    public void destroy() {

    }
}

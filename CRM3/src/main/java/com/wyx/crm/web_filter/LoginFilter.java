package com.wyx.crm.web_filter;

import com.wyx.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登陆过的监听器");
        //监听器中的req和resp没有获取session和重定向和请求转发的功能，需要强转成儿子（HttpServlet）
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response=(HttpServletResponse) resp;
        //因为访问jsp，jsp会自动生成一个session对象，也就有了sessionID，如果getSession(flase)
        //虽然有sessionId，但没有找到，直接返回个null，也就拦截下来了
        //如果是getSession（true）,虽然传过来的sessionID没有找到，但会为他创建一个session空间
        //此时session返回的就不是一个null，就成功进入到了页面
        HttpSession session=request.getSession(true);
        User user= (User) session.getAttribute("user"); //若session为null，出现空指针异常
        String path=request.getServletPath();
         //登陆页和登陆验证需要放行，因为（getSession是在login.do里进行的）
        if("/login.jsp".equals(path)||"/settings/user/login.do".equals(path)){
            chain.doFilter(req, resp);
        }else{
            if(user==null){
                //请求失败，利用重定向，为什么不用请求转发，请求转发直接是内部请求直接改变地址，
                //而当前网页的url地址框的中地址还是原来的，如果一刷新，就回到了原来的地址
                //下面代码其中getContextPath是获取项目名称，站点根路径，也就是myWeb
                String resource = request.getContextPath() + "/login.jsp";
                response.sendRedirect(request.getContextPath());
            }else{
                //放行，得到了合法的session
                chain.doFilter(req, resp);
            }
        }
   }

    @Override
    public void destroy() {

    }

}

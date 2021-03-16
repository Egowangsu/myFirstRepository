package com.wyx.Servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class oneServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            PrintWriter pw=response.getWriter();
            String path=request.getServletPath();
            if("/Student/select.do".equals(path)){
                System.out.println("查询");
            }else if("/Student/update.do".equals(path)){
                System.out.println("更新");
            }else if("/Student/delete.do".equals(path)){
                System.out.println("删除");
            }else if("/Student/save.do".equals(path)){
                System.out.println("增加");
            }

    }
}

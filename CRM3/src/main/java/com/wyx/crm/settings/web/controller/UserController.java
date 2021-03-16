package com.wyx.crm.settings.web.controller;

import com.wyx.crm.settings.domain.User;
import com.wyx.crm.settings.service.UserService;
import com.wyx.crm.settings.service.impl.UserServiceImpl;
import com.wyx.crm.utils.MD5Util;
import com.wyx.crm.utils.PrintJson;
import com.wyx.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path = request.getServletPath();  //拿到url_patten，来判断进行的功能
        if ("/settings/user/login.do".equals(path)) {
            login(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到页面登录操作");
        //接收参数，账户密码
        String loginAct =request.getParameter("loginAct");
        String loginPwd =request.getParameter("loginPwd");
        //把明文的账号密码转换为md5加密格式，在进行比较
        //loginAct = MD5Util.getMD5(loginAct);
        loginPwd= MD5Util.getMD5(loginPwd);
        //获取浏览器端ip地址，检测是否合法
        String ip = request.getRemoteAddr();
        System.out.println("------------------------------------------ip"+ip);
        //创建代理对象，以后业务层开发统一用代理形态的接口对象
        //UserService us= new UserServiceImpl();  这个创建的是目标类，没有commit提交事务的机制
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        try {
              //这个login调用的是李四（代理类）的方法，在invocation中执行
            User user=us.login(loginAct,loginPwd,ip); //传入到业务层，业务层进行判断
            //如果进行到这一步，说明业务层没有为controller抛出异常
            //表示登陆成功，可以执行到下一行代码
            request.getSession().setAttribute("user", user);
            /*
            * {success:true}
            * */
            PrintJson.printJsonFlag(response, true);
        } catch (Exception e) {
            e.printStackTrace();
            //如果进行到这一步，说明业务层向controller抛出了异常
            //表示登陆失败
            /*
            * {
            * success:false , msg:?
            * }
            *
            * */
            //如果传入多个参数可以有两种方法
            //1.利用map集合
            //2.带有success 和 msg 属性的vo类
            //选择情况，若是大量重复在其他地方使用可以用vo类，而这里只有在判断时才采用，所以用map
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap();
            map.put("success", false);
            map.put("msg", msg);
            PrintJson.printJsonObj(response, map);

        }
    }
}
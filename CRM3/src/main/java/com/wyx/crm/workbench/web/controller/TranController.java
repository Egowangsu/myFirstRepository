package com.wyx.crm.workbench.web.controller;

import com.wyx.crm.settings.domain.User;
import com.wyx.crm.settings.service.UserService;
import com.wyx.crm.settings.service.impl.UserServiceImpl;
import com.wyx.crm.utils.DateTimeUtil;
import com.wyx.crm.utils.PrintJson;
import com.wyx.crm.utils.ServiceFactory;
import com.wyx.crm.utils.UUIDUtil;
import com.wyx.crm.workbench.dao.CustomerDao;
import com.wyx.crm.workbench.dao.TranHistoryDao;
import com.wyx.crm.workbench.domain.Tran;
import com.wyx.crm.workbench.domain.TranHistory;
import com.wyx.crm.workbench.service.CustomerService;
import com.wyx.crm.workbench.service.TranService;
import com.wyx.crm.workbench.service.impl.CustomerServiceImpl;
import com.wyx.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场交易控制器");
        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)) {
            add(request, response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        } else if ("/workbench/transaction/save.do".equals(path)) {
            save(request, response);
        }else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        }else if ("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {
            getHistoryListByTranId(request, response);
        }else if ("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request, response);
        }else if ("/workbench/chart/transaction/getChart.do".equals(path)) {
            getChart(request, response);
        }

    }

    private void getChart(HttpServletRequest request, HttpServletResponse response) {
    TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map=ts.getChar();  //map里装有total和dataList
        PrintJson.printJsonObj(response, map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
            Map<String,String> pMap= (Map<String, String>) request.getServletContext().getAttribute("pMap");
            String tranId=request.getParameter("id");
            String money=request.getParameter("money");
            String expectedDate=request.getParameter("expectedDate");
            String stage=request.getParameter("stage");
            String createTime= DateTimeUtil.getSysTime();
            String createBy =((User)request.getSession().getAttribute("user")).getName();
            Tran t =new Tran();
            t.setId(tranId);
            String possibility=pMap.get(stage);
            t.setStage(stage);
            t.setEditTime(createTime);
            t.setEditBy(createBy);
            t.setPossibility(possibility);
            t.setExpectedDate(expectedDate);
            t.setMoney(money);
            TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
            boolean flag=ts.changeStage(t);
            Map<String,Object> map = new HashMap<>();
            map.put("success",flag);
            map.put("t",t);
            PrintJson.printJsonObj(response, map);

    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {
            String tranId =request.getParameter("tranId");
        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,String> map= (Map<String, String>) request.getServletContext().getAttribute("pMap");
        List<TranHistory> list=ts.getHistoryListByTranId(tranId,map);
        PrintJson.printJsonObj(response, list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入查询交详细页操作");
    String id = request.getParameter("id");
    TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
    Tran t=ts.detail(id);
    //这里用请求转发，因为使用到了request域
    //真正的原因是每次刷新，刷新的是detail.do界面，重新获取数据转发到detail.jsp，更新详细数据
    //重定向刷新的是detail.jsp，不过后台，数据不会更新
    request.setAttribute("t",t);
    Map<String,String> map= (Map<String, String>) request.getServletContext().getAttribute("pMap");
    String stage=t.getStage();
    String possibility=map.get(stage);
    request.getServletContext().setAttribute("possibility", possibility);
    request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);;
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //接收参数
        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String money=request.getParameter("money");
        String name=request.getParameter("name");
        String expectedDate=request.getParameter("expectedDate");
        String customerName=request.getParameter("customerName");
        String stage=request.getParameter("stage");
        String type=request.getParameter("type");
        String source =request.getParameter("source");
        String activityId=request.getParameter("activityId");
        String contactsId=request.getParameter("contactsId");
        String createTime= DateTimeUtil.getSysTime();
        String createBy =((User)request.getSession().getAttribute("user")).getName();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        boolean flag=ts.save(t,customerName);
        if(flag) { //因为是传统请求，所以可以有重定向和请求转发两种方式
            //1.请求转发
            //request.getRequestDispatcher("/workbench/transaction/detail.jsp");
            //2.重定向,requset.getContextPath 得到的是/项目名  也就是我的网站名 /myWeb
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/detail.jsp");
            //为什么不选择请求转发，因为这里没有用到request域，且用请求转发当前地址栏还是save.do，一刷新就会再一次进入save.do，执行相关方法
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得客户名称列表（进行模糊查询）");
        String name = request.getParameter("name");
        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        String[] names = cs.getUserName(name);
        PrintJson.printJsonObj(response, names);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入转到信息页的操作（同时下载下拉框信息）");
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        //请求域加转发的方式，将用户信息存入请求域，在save页面上用jstl接收用户名
        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
    }
}

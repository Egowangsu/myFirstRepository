package com.wyx.crm.workbench.web.controller;

import com.wyx.crm.settings.domain.User;
import com.wyx.crm.settings.service.UserService;
import com.wyx.crm.settings.service.impl.UserServiceImpl;
import com.wyx.crm.utils.*;
import com.wyx.crm.workbench.domain.Activity;
import com.wyx.crm.workbench.domain.ActivityRemark;
import com.wyx.crm.workbench.domain.Pagination;
import com.wyx.crm.workbench.service.ActivityService;
import com.wyx.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到市场活动制器");
        String path = request.getServletPath();  //拿到url_patten，来判断进行的功能
        if ("/workbench/activity/getUserList.do".equals(path)) {
                    getUserList(request,response);
        }else if("/workbench/activity/save.do".equals(path)){
           getSave(request,response);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(request,response);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }else if("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/activity/getRemarkMss.do".equals(path)){
            getRemarkMss(request,response);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        String id = request.getParameter("id");
        String noteContent=request.getParameter("noteContent");
        String editTime=DateTimeUtil.getSysTime();
        String editBy=((User)request.getSession().getAttribute("user")).getName();
        String editFlag="1";
        Map<String,Object> map = new HashMap<>();
        map.put("id", id);
        map.put("noteContent",noteContent );
        map.put("editTime", editTime);
        map.put("editBy", editBy);
        map.put("editFlag",editFlag );
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.updateRemark(map);
        PrintJson.printJsonFlag(response, flag);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到保存备注的操作界面");
        String noteContent=request.getParameter("noteContent");
        String activityId=request.getParameter("activityId");
        Map<String,Object> map = new HashMap<>();
        map.put("noteContent", noteContent);
        map.put("activityId", activityId);
        map.put("id",UUIDUtil.getUUID());
        map.put("createTime",DateTimeUtil.getSysTime());
        map.put("createBy",((User)request.getSession().getAttribute("user")).getName()); //当前的登陆的用户就是创建人
        map.put("editFlag",0);
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.saveRemark(map);
        PrintJson.printJsonFlag(response, flag);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入删除备注操作界面");
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag =as.deleteRemark(id);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getRemarkMss(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到获得备注操作");
        String id = request.getParameter("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> list=as.getRemarkMss(id);
        PrintJson.printJsonObj(response, list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到执行获得detail信息操作");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity=as.detail(id);
        //将返回结果存入request域中，然后请求转发给jsp
        request.setAttribute("activity", activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的修改操作");
        String id= request.getParameter("id");
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        //修改时间为当前系统时间
        String editTime= DateTimeUtil.getSysTime();
        //修改人人为当前登录的用户
        String editBy =((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setCost(cost);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setDescription(description);
        activity.setCreateTime(editTime);
        activity.setCreateBy(editBy);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.update(activity);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询信息列表根据市场活动id查询单条记录的操作");
        //先拿数据
        String  id = request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        //我需要从后端拿到一个uLisy和一个Activity,因为这种不常用，所以返回一个map就可以，不用vo
        Map<String,Object> map = as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response, map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        //先拿数据
        System.out.println("执行市场活动的删除操作");
        String[] id=request.getParameterValues("id");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag=as.delete(id);
        PrintJson.printJsonFlag(response, flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {
        //vo是底层的数据传给前端模型
        //这里用map打包传给dao层
        System.out.println("执行查询/刷新市场活动操作");
        String pageNoStr =request.getParameter("pageNo");

        int pageNo=Integer.valueOf(pageNoStr);
        String pageSizeStr=request.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);
        //Limit（0,5）  略过0，取五个记录  ，现在计算略过数skipCount
        int skipCount=(pageNo-1)*pageSize;

        String name =request.getParameter("name");
        String owner =request.getParameter("owner");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        Map<String,Object> map = new HashMap();
        map.put("skipCount", skipCount);
        map.put("pageSize",pageSize );
        map.put("name",name );
        map.put("owner",owner );
        map.put("startDate",startDate );
        map.put("endDate",endDate );
        //获取代理对象
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        //这里返回值用vo,后端向前端返回数据，将数据放入vo对象
        //选择vo是因为查询不仅只用在市场模块，其他模块也频繁使用，所以利用vo来复用
        //前端需要的返回数据为  {"total":总条数  ，"pageList":{市场活动1，市场活动2，..}}
        //所以vo的属性有两个值 total 和 dataList,(data不写死，可以让其他模块用)
        Pagination<Activity> vo=as.pageList(map);  //这里添加了泛型Activity，vo里的T会自动添加泛型
        PrintJson.printJsonObj(response, vo);
    }

    private void getSave(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行市场活动的添加操作");
       String id= UUIDUtil.getUUID();
       String owner=request.getParameter("owner");
       String name=request.getParameter("name");
       String startDate=request.getParameter("startDate");
       String endDate=request.getParameter("endDate");
       String cost=request.getParameter("cost");
       String description=request.getParameter("description");
       //创建时间为当前系统时间
       String createTime= DateTimeUtil.getSysTime();
       //创建人为当前登录的用户
       String createBy =((User)request.getSession().getAttribute("user")).getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setCost(cost);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);
        System.out.println(activity);
       ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
       boolean flag=as.save(activity);
       PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("取得用户信息列表");
        //创建代理类对象
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=us.getUserList();
        //将返回的集合转成json数组形式传回ajax
        PrintJson.printJsonObj(response, uList);
    }


}
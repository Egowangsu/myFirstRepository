package com.wyx.crm.workbench.web.controller;

import com.wyx.crm.settings.domain.DicValue;
import com.wyx.crm.settings.domain.User;
import com.wyx.crm.settings.service.UserService;
import com.wyx.crm.settings.service.impl.UserServiceImpl;
import com.wyx.crm.utils.DateTimeUtil;
import com.wyx.crm.utils.PrintJson;
import com.wyx.crm.utils.ServiceFactory;
import com.wyx.crm.utils.UUIDUtil;
import com.wyx.crm.workbench.domain.*;
import com.wyx.crm.workbench.service.ActivityService;
import com.wyx.crm.workbench.service.ClueService;
import com.wyx.crm.workbench.service.impl.ActivityServiceImpl;
import com.wyx.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.api.scripting.ScriptUtils.convert;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到线索控制器");
        String path = request.getServletPath();  //拿到url_patten，来判断进行的功能
        if ("/workbench/clue/getUser.do".equals(path)) {
                getUser(request,response);
        }else if("/workbench/clue/saveClue.do".equals(path)){
                saveClue(request,response);
        }else if("/workbench/clue/pageList.do".equals(path)){
                pageList(request,response);
        }else if("/workbench/clue/detail.do".equals(path)){
                detail(request,response);
        }else if("/workbench/clue/getActivityListByCondition.do".equals(path)){
            getActivityListByCondition(request,response);
        }else if("/workbench/clue/unBurn.do".equals(path)){
            unBurn(request,response);
        }else if("/workbench/clue/getActivity.do".equals(path)){
            getActivity(request,response);
        }else if("/workbench/clue/bund.do".equals(path)){
           bund(request,response);
        }else if("/workbench/clue/searchText.do".equals(path)){
          searchText(request,response);
        }else if("/workbench/clue/convert.do".equals(path)){
          getConvert(request,response);
        }
    }

    private void getConvert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入到页面转换操作");
        String clueId= request.getParameter("clueId");
        String flag = request.getParameter("flag");
        Tran t=null;  //交易实体类，用于传给业务层判断是否需要进行交易操作
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        if("a".equals(flag)){
           t=new Tran();    //t被赋予了值，业务层判断t是否为null来决定是否进行交易操作
            //接收参数
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id=UUIDUtil.getUUID();
            String createTime=DateTimeUtil.getSysTime();
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setId(id);
            t.setCreateTime(createTime);
            t.setCreateBy(createBy);
        }
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag1=cs.getConvert(clueId,t,createBy);
        //因为我们用的是传统请求方式，返回信息不能用jackson了
        //用重定向的方式
        if(flag1){   //如果返回的是true，代表成功，使用重定向
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }

    }

    private void searchText(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到查询活动操作界面");
        String value = request.getParameter("value");
        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> list=as.searchText(value);
        PrintJson.printJsonObj(response, list);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到绑定操作界面");
        String clueId = request.getParameter("clueId");
        String[] activityIds=request.getParameterValues("activityId");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.bund(clueId,activityIds);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivity(HttpServletRequest request, HttpServletResponse response) {
    String value = request.getParameter("value");
    String clueId = request.getParameter("clueId");
    ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
    List<Activity> list =as.getActivity(value,clueId);
    PrintJson.printJsonObj(response, list);

    }

    private void unBurn(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入市场解除关联操作界面");
        String id = request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.unBurn(id);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityListByCondition(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入获取市场关联界面");
    String clueId=request.getParameter("clueId");
    //因为最终需要返回的是一个activity集合,所以在市场活动服务层上做
    ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
    List<Activity> list=as.getActivityListByCondition(clueId);
    PrintJson.printJsonObj(response, list);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入获取详细信息界面");
        //先将信息存入request域中，然后请求转发，在detail.jsp中获取
        System.out.println("进入到执行获得cluedetail信息操作");
        String id=request.getParameter("id");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue=cs.detail(id);
        //将返回结果存入request域中，然后请求转发给jsp
        System.out.println(clue);
        request.setAttribute("clue", clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行查询/刷新线索操作");
        String pageNoStr =request.getParameter("pageNo");
        int pageNo=Integer.valueOf(pageNoStr);
        String pageSizeStr=request.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);
        //Limit（0,5）  略过0，取五个记录  ，现在计算略过数skipCount
        int skipCount=(pageNo-1)*pageSize;
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Pagination<Clue> vo=cs.pageList(skipCount,pageSize);
        PrintJson.printJsonObj(response, vo);
    }

    private void saveClue(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("执行线索保存界面");
        String id=UUIDUtil.getUUID();
        String fullname=request.getParameter("fullname");
        String appellation=request.getParameter("appellation");
        String owner=request.getParameter("owner");
        String company=request.getParameter("company");
        String job=request.getParameter("job");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String website=request.getParameter("website");
        String mphone=request.getParameter("mphone");
        String state=request.getParameter("state");
        String source=request.getParameter("source");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String createTime=DateTimeUtil.getSysTime();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String address=request.getParameter("address");
        Clue clue = new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        boolean flag=cs.saveClue(clue);
        PrintJson.printJsonFlag(response, flag);
    }


    private void getUser(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获得用户信息");
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<User> userList=cs.getUser();
        PrintJson.printJsonObj(response, userList);
    }


}
package com.wyx.crm.settings.service.impl;

import com.wyx.crm.exception.LoginException;
import com.wyx.crm.settings.dao.UserDao;
import com.wyx.crm.settings.domain.User;
import com.wyx.crm.settings.service.UserService;
import com.wyx.crm.utils.DateTimeUtil;
import com.wyx.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
        //会用到服务类，可以看做是一个目标类，通过目标类获取代理类
    //dao层以成员变量的形式呈现
    private UserDao dao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override   //这个是张三（目标类）的login，异常也是在这里抛出的
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        //将账号密码打包成map,传给dao层，返回一个User对象
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user =dao.login(map);
        if(user == null){
            throw new LoginException("账号密码错误！");  //抛出自定异常
        }
        //若能执行到该行，则账号密码正确，继续验证三项信息

        //验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();  //封装时间工具，已设定好格式  19位
        if(currentTime.compareTo(expireTime)>0){
            throw new LoginException("账号已失效！");
        }

        //判定锁定状态
        String state = user.getLockState();
        if("0".equals(state)){
            throw new LoginException("账号被锁定，请联系管理员");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){
            throw new LoginException("ip地址受限");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> uList=dao.getUserList();
        return uList;
    }


}

package com.wyx.crm.workbench.service.impl;

import com.wyx.crm.settings.dao.UserDao;
import com.wyx.crm.settings.domain.User;
import com.wyx.crm.utils.SqlSessionUtil;
import com.wyx.crm.workbench.dao.ActivityDao;
import com.wyx.crm.workbench.dao.ActivityRemarkDao;
import com.wyx.crm.workbench.domain.Activity;
import com.wyx.crm.workbench.domain.ActivityRemark;
import com.wyx.crm.workbench.domain.Pagination;
import com.wyx.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    //mybatis支持为dao层提供动态代理，
    //动态获得dao层的实现类对象，不用自己一一建立dao层的实现类对象了
    private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    @Override
    public boolean save(Activity activity) {
        boolean flag=true;
        int count= activityDao.save(activity);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Pagination<Activity> pageList(Map<String, Object> map) {
        //获取total，总条数随着条件变化
        int total =activityDao.getTotalByCondition(map);
        //获取市场活动，市场活动可能也有条件
        List<Activity> dataList=activityDao.getActivityListByCondition(map);
        Pagination<Activity> vo =new Pagination<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public boolean delete(String[] id) {
        //删除市场活动前，需要将关联的市场活动备注删除，表tbl_activityRemark中的数据
        //所以要用到另有一个dao对象了
        boolean flag=true;
        //查出需要删除的备注的数量
        int count1 =activityRemarkDao.getCountById(id);
        //删除备注，返回受到影响的数量
        int count2=activityRemarkDao.deleteById(id);
        if(count1!=count2){
            flag=false;
        }

        //删除市场活动
        int count3=activityDao.delete(id);
        //传过来的数组的长度就是需要删除的数量
        //如果受到影响的数量等于数组的长度说明删除成功
        if(count3!=id.length){
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        //因为需要uList(用户信息表)，所以要用到userDao
            List<User> uList=userDao.getUserList();
            Activity activity=activityDao.getById(id);
            Map<String,Object> map = new HashMap<>();
            map.put("uList", uList);
            map.put("activity", activity);
            return map;

    }

    @Override
    public boolean update(Activity activity) {
        boolean flag=true;
        int count= activityDao.update(activity);
        if(count!=1){


            flag=false;
        }
        return flag;
    }

    @Override
    public Activity detail(String id) {
        Activity activity=activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark> getRemarkMss(String id) {
        List<ActivityRemark> list=activityRemarkDao.getRemarkMss(id);
        return list;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag=true;
        int count=activityRemarkDao.deleteRemark(id);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean saveRemark(Map<String, Object> map) {
        boolean flag=true;
        int count=activityRemarkDao.saveRemark(map);
        if(count!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public boolean updateRemark(Map<String, Object> map) {
        boolean flag=true;
        int count=activityRemarkDao.updateRemark(map);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByCondition(String clueId) {
        List<Activity> list= activityDao.getActivityListByClueId(clueId);
        return list;
    }

    @Override
    public List<Activity> getActivity(String value,String clueId) {
        List<Activity> list=activityDao.getActivity(value,clueId);
        return list;
    }

    @Override
    public List<Activity> searchText(String value) {
        List<Activity> list=activityDao.searchText(value);
        return list;
    }
}

package com.wyx.crm.workbench.service.impl;

import com.wyx.crm.utils.DateTimeUtil;
import com.wyx.crm.utils.SqlSessionUtil;
import com.wyx.crm.utils.UUIDUtil;
import com.wyx.crm.workbench.dao.CustomerDao;
import com.wyx.crm.workbench.dao.TranDao;
import com.wyx.crm.workbench.dao.TranHistoryDao;
import com.wyx.crm.workbench.domain.Customer;
import com.wyx.crm.workbench.domain.Tran;
import com.wyx.crm.workbench.domain.TranHistory;
import com.wyx.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {
    private TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao= SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Override
    public boolean save(Tran t, String customerName) {
        boolean flag=true;
        //用客户名去精确匹配，匹配成功返回customertId
        //没有匹配成功，就创建该新的客户，返回创建的客户id ，customerId
        //将客户id添加到交易对象t中
            Customer customer=customerDao.getCustomerByName(customerName);
            if(customer==null){ //没有找到就新建
                Customer c = new Customer();
                c.setId(UUIDUtil.getUUID());
                c.setName(customerName);
                c.setCreateBy(t.getCreateBy());
                c.setCreateTime(DateTimeUtil.getSysTime());
                c.setNextContactTime(t.getNextContactTime());
                c.setDescription(t.getDescription());
                c.setOwner(t.getOwner());
                c.setContactSummary(t.getContactSummary());
                int count=customerDao.save(c);
                if(count!=1){
                    flag=false;
                }
                t.setCustomerId(c.getId());
            }else{
                t.setCustomerId(customer.getId());
            }
            //经过以上操作，添加一条交易
        int count=tranDao.save(t);
            if(count!=1){
                flag=false;
            }


        //添加一条交易记录

        TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setCreateBy(t.getCreateBy());
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            tranHistory.setMoney(t.getMoney());
            int count2 =tranHistoryDao.save(tranHistory);
            if(count2!=1){
                flag=false;
            }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t=tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId, Map<String,String> map) {
        List<TranHistory> list=tranHistoryDao.getHistoryListByTranId(tranId);
        for(TranHistory th:list){
            String stage=th.getStage();
            String possibility=map.get(stage);
            th.setPossibility(possibility);
        }
        return list;
    }


    @Override
    public boolean changeStage(Tran t) {
        boolean flag=true;
        int count=tranDao.changeStage(t);
        if(count!=1){
         flag=false;
        }
        //创建一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setCreateTime(t.getEditTime());
        th.setCreateBy(t.getCreateBy());
        th.setExpectedDate(t.getExpectedDate());
        int count2=tranHistoryDao.save(th);
        if(count2!=1){
            flag=false;
        }

        return flag;

    }

    @Override
    public Map<String, Object> getChar() {
        //获取一个total
        int total=tranDao.total();

        //获取dataList 集合里是许多map，每个map装一条数据，数据key是  name  和 value，图表才会识别
        List<Map<String,Object>> dataList=tranDao.getChar();
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        System.out.println(map);
        return map;
    }
}

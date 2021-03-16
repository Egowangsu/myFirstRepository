package com.wyx.crm.workbench.service.impl;

import com.wyx.crm.settings.dao.UserDao;
import com.wyx.crm.settings.domain.User;
import com.wyx.crm.utils.DateTimeUtil;
import com.wyx.crm.utils.SqlSessionUtil;
import com.wyx.crm.utils.UUIDUtil;
import com.wyx.crm.workbench.dao.*;
import com.wyx.crm.workbench.domain.*;
import com.wyx.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {
    //要用到代理类的方法，先得到代理类对象
    //线索相关表
    private ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //客户相关表
    private CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao= SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系人相关表
    private ContactsDao contactsDao= SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao= SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //交易相关表
    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    //用户表
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);







    @Override
    public List<User> getUser() {
        List<User> user=userDao.getUserList();
        return user;
    }

    @Override
    public boolean saveClue(Clue clue) {
        boolean flag=true;
        int count=clueDao.saveClue(clue);
        if(count!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public Pagination<Clue> pageList(int skipCount, int pageSize) {
        int total =clueDao.getTotalByCondition();
        List<Clue> dataList = clueDao.getActivityListByCondition(skipCount,pageSize);
        Pagination<Clue> vo =new Pagination<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public Clue detail(String id) {
        Clue clue=clueDao.detail(id);
        return clue;
    }

    @Override
    public boolean unBurn(String id) {
        boolean flag=true;
        int count = clueDao.unBurn(id);
        if(count!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean bund(String clueId,String[] activityIds) {
        boolean flag=true;

        for(int i=0;i<activityIds.length;i++){
            ClueActivityRelation car= new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(activityIds[0]);
            car.setClueId(clueId);
            int count=clueActivityRelationDao.bund(car);
            if(count!=1){
                flag=false;
            }
        }
        return flag;
    }

    @Override
    public boolean getConvert(String clueId, Tran t, String createBy) {
        //因为会用到大量的插入操作，所以需要用到
        //id，createTime，createBy等，但是前面两个可以用工具生成，createBy是从Session域中拿的
        //需要用到request，这个操作应该在controller层完成，要分工明确，虽然可以传过来一个request来获取
        //但是违背了mvc原则
        String createTime= DateTimeUtil.getSysTime();
        boolean flag=true;
        //(1)，根据clueId来查线索的详细信息，
        Clue c=clueDao.getById(clueId);
        //(2)，提取线索中的客户信息，当客户不存在时，新建客户（根据客户名称精确匹配），客户是以公司为单位
        //判断客户表中是否有该客户
        String company = c.getCompany();
        Customer customer=customerDao.getCustomerByName(company);  //用名字精确查询，查到了就返回客户对象
        //如果cus=null 说明没有这个客户，需要新建一个
        if(customer==null){
          //UUid每次都要新建一个，不能所有都共用一个(放在外面，当做成员变量)，这样全部的id都一样了
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(c.getAddress());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
            customer.setName(c.getCompany());
            customer.setContactSummary(c.getContactSummary());
            customer.setWebsite(c.getWebsite());
            customer.setOwner(c.getOwner());
            customer.setPhone(c.getPhone());
            customer.setNextContactTime(c.getNextContactTime());
            customer.setDescription(c.getDescription());
            //添加客户
            int count=customerDao.save(customer);
            if(count!=1){
                flag=false;
            }

        }
        //(3) 通过线索对象提取联系人信息，保存联系人(3) 通过线索对象提取联系人信息，保存联系人
        Contacts con  = new Contacts();
        con.setSource(c.getSource());
        con.setOwner(c.getOwner());
        con.setNextContactTime(c.getNextContactTime());
        con.setMphone(c.getMphone());
        con.setJob(c.getJob());
        con.setId(UUIDUtil.getUUID());
        con.setFullname(c.getFullname());
        con.setEmail(c.getEmail());
        con.setDescription(c.getDescription());
        con.setCustomerId(c.getId());
        con.setCreateTime(createTime);
        con.setCreateBy(createBy);
        con.setContactSummary(c.getContactSummary());
        con.setAppellation(c.getAppellation());
        con.setAddress(c.getAddress());
        int count=contactsDao.save(con);
        if(count!=1){
            flag= false;
        }
        //(4) 线索备注转换到客户备注以及联系人备注
           //查询出与该线索关联的备注
        List<ClueRemark> list=clueRemarkDao.getMessByClueId(clueId);
        if(list!=null){
            for(ClueRemark cr:list){  //这里只需要原来备注中的notecontent
               String noteContent=cr.getNoteContent();
               //创建客户备注对象，添加客户备注
                CustomerRemark ctr=new CustomerRemark();
                ctr.setId(UUIDUtil.getUUID());
                ctr.setCreateBy(createBy);
                ctr.setNoteContent(noteContent);
                ctr.setCustomerId(customer.getId());
                ctr.setCreateTime(createTime);
                ctr.setEditFlag("0");
                int count1=customerRemarkDao.save(ctr);
               //创建联系人备注，添加联系人备注
                ContactsRemark cts=new ContactsRemark();
                cts.setId(UUIDUtil.getUUID());
                cts.setCreateBy(createBy);
                ctr.setNoteContent(noteContent);
                cts.setContactsId(con.getId());
                cts.setCreateTime(createTime);
                cts.setEditFlag("0");
                int count2=contactsRemarkDao.save(cts);
                if(count1!=1||count2!=1){
                    flag=false;
                }
            }
        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
                //先通过线索id查 “线索和市场活动”表中的关联数据，拿其中的市场活动id和联系人id写入到“联系人和市场活动”
            List<ClueActivityRelation> clueActivityRelationList=clueActivityRelationDao.getListByClueId(clueId);
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            for(ClueActivityRelation car:clueActivityRelationList){
                String activityId=car.getActivityId();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setActivityId(activityId);
                contactsActivityRelation.setContactsId(con.getId());
                int count3=contactsActivityRelationDao.save(contactsActivityRelation);
                if(count!=1){
                    flag=false;
                }
            }

        //(6) 如果有创建交易需求，创建一条交易
        if(t!=null){   //这里就是有创建交易的需求
            //t对象中已经存入了一些信息，若是还需要其他信息，可以自行加入
            t.setSource(con.getSource());
            t.setOwner(con.getOwner());
            t.setDescription(con.getDescription());
            t.setCustomerId(customer.getId());
            t.setContactSummary(con.getContactSummary());
            t.setContactsId(con.getId());
            t.setNextContactTime(con.getNextContactTime());
            int count4=tranDao.save(t);
            if(count4!=1){
                flag=false;
            }

            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setTranId(t.getId());
            tranHistory.setStage(t.getStage());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            //添加交易历史
           int count5=tranHistoryDao.save(tranHistory);
           if(count5!=1){
               flag=false;
           }
        }
            //(8)删除线索备注
        for(ClueRemark cr:list){  //可以直接用clueId来删除，也可以用这种传递被柱对象来删除
            int count6=clueRemarkDao.delete(cr);
            if(count6!=1){
                flag=false;
            }
        }


        //(9) 删除线索和市场活动的关系
        for(ClueActivityRelation car:clueActivityRelationList){
            int count7=clueActivityRelationDao.delete(car);
            if(count7!=1){
                flag=false;
            }
        }


        //(10) 删除线索
        int count8=clueDao.delete(clueId);
        if(count8!=1){
            flag=false;
        }
        return flag;
    }

}

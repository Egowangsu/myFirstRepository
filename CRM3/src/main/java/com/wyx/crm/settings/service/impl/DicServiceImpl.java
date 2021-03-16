package com.wyx.crm.settings.service.impl;

import com.wyx.crm.settings.dao.DicTypeDao;
import com.wyx.crm.settings.dao.DicValueDao;
import com.wyx.crm.settings.domain.DicType;
import com.wyx.crm.settings.domain.DicValue;
import com.wyx.crm.settings.service.DicService;
import com.wyx.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {
    private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    @Override
    public Map<String, List<DicValue>> getAll() {
        //先接收全部的对象，然后循环获取其code
        List<DicType> list=dicTypeDao.getAllCode();
        Map<String,List<DicValue>> map = new HashMap<>();
        for(DicType dic:list){
            //获取code
            String typeCode=dic.getCode();
            //根据typeCode去找value,返回DicValue对象集合
            List<DicValue> list1=dicValueDao.getAllValue(typeCode);
                //直接把整个对象装入map
            map.put(typeCode, list1);
        }
        return map;
    }
}

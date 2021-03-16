package com.wyx.crm.workbench.service.impl;

import com.wyx.crm.utils.SqlSessionUtil;
import com.wyx.crm.workbench.dao.CustomerDao;
import com.wyx.crm.workbench.domain.Customer;
import com.wyx.crm.workbench.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {
    private CustomerDao cd= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public String[] getUserName(String name) {
        String[] names  =cd.getUserName(name);
        return names;
    }
}

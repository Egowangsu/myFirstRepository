package com.wyx.crm.workbench.dao;

import com.wyx.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

    String[] getUserName(String name);
}

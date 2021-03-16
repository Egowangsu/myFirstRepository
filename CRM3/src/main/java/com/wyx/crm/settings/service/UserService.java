package com.wyx.crm.settings.service;

import  com.wyx.crm.exception.LoginException;
import com.wyx.crm.settings.domain.User;
import com.wyx.crm.utils.PrintJson;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;


    List<User> getUserList();


}

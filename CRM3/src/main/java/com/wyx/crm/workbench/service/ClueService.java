package com.wyx.crm.workbench.service;

import com.wyx.crm.settings.domain.User;
import com.wyx.crm.workbench.domain.Clue;
import com.wyx.crm.workbench.domain.Pagination;
import com.wyx.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    List<User> getUser();

    boolean saveClue(Clue clue);

    Pagination<Clue> pageList(int skipCount, int pageSize);

    Clue detail(String id);

    boolean unBurn(String id);

    boolean bund(String clueId,String[] activityIds);


    boolean getConvert(String clueId, Tran t, String createBy);
}

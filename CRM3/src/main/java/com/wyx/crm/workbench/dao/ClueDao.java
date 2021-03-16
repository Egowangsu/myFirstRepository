package com.wyx.crm.workbench.dao;


import com.wyx.crm.workbench.domain.Clue;
import com.wyx.crm.workbench.domain.ClueRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int saveClue(Clue clue);

    List<Clue> getActivityListByCondition(@Param("skipCount") int skipCount, @Param("pageSize") int pageSize);

    int getTotalByCondition();

    Clue detail(String id);

    int unBurn(String id);


    Clue getById(String clueId);

    List<ClueRemark> getMessById(String clueId);

    int delete(String clueId);
}

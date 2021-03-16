package com.wyx.crm.workbench.dao;

import com.wyx.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getMessByClueId(String clueId);

    int delete(ClueRemark cr);
}

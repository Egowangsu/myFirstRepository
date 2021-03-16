package com.wyx.crm.workbench.dao;

import com.wyx.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityRemarkDao {
    int getCountById(String[] id);

    int deleteById(String[] id);

    List<ActivityRemark> getRemarkMss(String id);

    int deleteRemark(String id);

    int saveRemark(Map<String, Object> map);

    int updateRemark(Map<String, Object> map);
}

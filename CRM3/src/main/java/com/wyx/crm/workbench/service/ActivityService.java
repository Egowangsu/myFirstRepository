package com.wyx.crm.workbench.service;

import com.wyx.crm.workbench.domain.Activity;
import com.wyx.crm.workbench.domain.ActivityRemark;
import com.wyx.crm.workbench.domain.Pagination;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    boolean save(Activity activity);

    Pagination<Activity> pageList(Map<String, Object> map);

    boolean delete(String[] id);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkMss(String id);

    boolean deleteRemark(String id);

    boolean saveRemark(Map<String, Object> map);

    boolean updateRemark(Map<String, Object> map);

    List<Activity> getActivityListByCondition(String clueId);

    List<Activity> getActivity(String value,String clueId);

    List<Activity> searchText(String value);
}

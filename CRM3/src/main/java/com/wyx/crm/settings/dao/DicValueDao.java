package com.wyx.crm.settings.dao;

import com.wyx.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getAllValue(String typeCode);
}

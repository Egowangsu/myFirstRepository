package com.wyx.dao;

import com.wyx.domain.Student;
import org.apache.ibatis.annotations.Param;

public interface StudentDao {
    public abstract Student selectById(@Param("id") Integer id);
    public abstract void insertStudent(Student student);
}

package com.wyx.service;

import com.wyx.domain.Student;
import org.apache.ibatis.annotations.Param;

public interface StudentService {
    public abstract Student selectById(@Param("id") Integer id);
    public abstract void insertStudent(Student student);
}

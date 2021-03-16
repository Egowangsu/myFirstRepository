package com.wyx.service.Imp;

import com.wyx.dao.Imp.StudentDaoImp;
import com.wyx.dao.StudentDao;
import com.wyx.domain.Student;
import com.wyx.service.StudentService;

public class StudentServiceImp implements StudentService {
    private StudentDao stu = new StudentDaoImp(); //多态
    @Override
    public Student selectById(Integer id) {   //dao层(持久层)中的业务全部在业务层中进行
        Student s=stu.selectById(id);
        return s;
    }

    @Override
    public void insertStudent(Student student) {
        stu.insertStudent(student);
    }
}

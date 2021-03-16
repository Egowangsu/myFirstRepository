package com.wyx.dao.Imp;

import com.wyx.dao.StudentDao;
import com.wyx.domain.Student;
import com.wyx.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class StudentDaoImp implements StudentDao {
    @Override
    public Student selectById(Integer id) {
        SqlSession sqlSession=MyBatisUtil.getSqlSession();
        //sqlId=接口全限定名称+id名
        String sqlId="com.wyx.dao.StudentDao.selectById";
        Student stu=sqlSession.selectOne(sqlId, id);
        return stu;
    }

    @Override
    public void insertStudent(Student stu) {
        SqlSession sqlSession=MyBatisUtil.getSqlSession();
        //sqlId=接口全限定名称+id名
        String sqlId="com.wyx.dao.StudentDao.insertStudent";
        sqlSession.insert(sqlId,stu);

    }
}

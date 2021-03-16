package com.wyx.utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {
    //mybatis用来执行连接数据库De
    private static SqlSessionFactory sqlSessionFactory;
    private static SqlSession sqlSession;

    static{
        String resource="MyBatis.xml";
        InputStream inputStream=null;
        try {
            inputStream= Resources.getResourceAsStream(resource);

        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
    }
        //线程临时存储
    private static ThreadLocal<SqlSession> t = new ThreadLocal();
    public static SqlSession getSession(){
        SqlSession sqlSession=t.get();
        if(sqlSession==null){
            sqlSession=sqlSessionFactory.openSession();
            t.set(sqlSession);
        }
        return  sqlSession;
    }

    public static void myClose(){
        if(sqlSession!=null){
            sqlSession.close();
            t.remove();
        }
    }
}

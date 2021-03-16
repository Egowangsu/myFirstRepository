package com.wyx.util;

import com.wyx.domain.Student;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtil {
    private static SqlSessionFactory factory = null;
    private static SqlSession sqlSession=null;
    private static ThreadLocal<SqlSession> t = new ThreadLocal<>();
    static{
        String config="MyBatis.xml";
        try {
            InputStream in = Resources.getResourceAsStream(config);
            factory=new SqlSessionFactoryBuilder().build(in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
        public static SqlSession getSqlSession(){
          sqlSession=t.get();       //如果当前线程中没有值，就会赋值null给sqlSession
            if(sqlSession==null){
                sqlSession=factory.openSession();
                t.set(sqlSession);
                }
            return sqlSession;
            }
            public static void myClose(){
                if(sqlSession!=null){
                   sqlSession.close();
                   t.remove();
                }

            }
}

package com.wyx;

import com.wyx.dao.StudentDao;
import com.wyx.domain.Student;
import com.wyx.service.Imp.StudentServiceImp;
import com.wyx.service.StudentService;
import com.wyx.util.MyBatisUtil;
import com.wyx.util.ServiceFactory;
import org.apache.ibatis.session.SqlSession;

public class Test {
    @org.junit.Test
    public void selectById(){
        StudentService ss= new StudentServiceImp();
        Student stu=ss.selectById(25);
        System.out.println(stu);
    }
    @org.junit.Test
    public void insertStudent(){
        //下面的ss对象是目标类对象，没有提交事务的功能，需要得到代理对象
       // StudentService ss= new StudentServiceImp();
        //传入目标对象，得到代理对象
        StudentService ss= (StudentService) ServiceFactory.getService(new StudentServiceImp());
        Student stu= new Student(1006,"cxk",17);
        ss.insertStudent(stu);  //这里会调用invoke方法，从而执行目标类的方法，而代理类有目标类的所有方法
    }

    @org.junit.Test
    public void Test(){
        for(int i=2;i<=100;i++){
            boolean ss=true;
            for(int j=2;j<i;j++){
                if(i%j==0){
                    ss=false;
                    break;  
                }
            }
            if(ss){
                System.out.println(i);
            }
        }
    }
}

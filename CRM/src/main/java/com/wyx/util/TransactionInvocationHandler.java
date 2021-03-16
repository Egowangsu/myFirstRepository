package com.wyx.util;

import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionInvocationHandler implements InvocationHandler {
    private Object target;

    public TransactionInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        SqlSession sqlSession=null;
        Object obj=null;
        try{
           sqlSession=MyBatisUtil.getSqlSession();   //这是代理类中的获取sqlSession
            //处理业务逻辑，代理类调用的是目标类的方法
            obj=method.invoke(target, args);
            //业务逻辑处理完毕后，提交事务
            sqlSession.commit();
        }catch(Exception e){
           sqlSession.rollback();
            e.printStackTrace();
        }finally {
            MyBatisUtil.myClose();
        }
            return obj;
    }

    //取得代理类对象，一定是通过下面的代码
    public Object getProxy(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(), this);
        //第三个参数的this就是InvocationHandler接口实现类的对象，也就是该类本身
    }
}

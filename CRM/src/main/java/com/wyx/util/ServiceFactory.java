package com.wyx.util;
//这个工厂类可以不写，直接new TransactionInvocationHandler(target).getProxy();

public class ServiceFactory {
    //传递张三对象，得到李四对象的过程。也就是传递目标类对象得到代理类对象过程。
    public static Object getService(Object target){
        //取得代理对象
        return new TransactionInvocationHandler(target).getProxy();
    }
}

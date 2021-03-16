package com.wyx.crm.web_listener;

import com.wyx.crm.settings.domain.DicValue;
import com.wyx.crm.settings.service.DicService;
import com.wyx.crm.settings.service.impl.DicServiceImpl;
import com.wyx.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class ConTextInitialized implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("进入到服务器缓存加载数据字典中方法中");
        //当服务器启动时，为自动创建一个全局作用域application
        //当这个listener监听到全局作用域创建时，会自动进入该方法
        //参数event，实现了什么域就可以根据他得到响应的域对象
        ServletContext application=event.getServletContext();
        //得到数据字典
        DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map =ds.getAll(); //返回七个集合打包成一个的map
       // application.setAttribute("map",map);
        //此时map的数据是{
        //                  数据类型：相应的对象集合
        //
        //               }
        //将map拆分，装入全局作用域中
        //将map转成set得到全部的key
        Set<String > set= map.keySet();
        //遍历set，拿到key来获取相应的value，也就是相应的集合
        for(String key:set){
            application.setAttribute(key, map.get(key));  //这里的map.get(key)是一个集合
        }
        System.out.println("服务器缓存加载数据字典方法结束");

        System.out.println("------------------------------------------------------");
        System.out.println("服务器缓存加载配置文件(阶段-可能性)方法开始");
        //配置文件格式 {阶段名=可能性}
        //利用资源绑定器
        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e=rb.getKeys();  //得到虽有的key，返回枚举类型
        Map<String,String> pMap = new HashMap<>();
        while(e.hasMoreElements()){
            String key=e.nextElement();
            String value=rb.getString(key); //通过key来获取value值，也就是可能性
            pMap.put(key, value);
        }
        application.setAttribute("pMap",pMap);
        System.out.println("服务器缓存加载配置文件(阶段-可能性)方法结束");



    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
            //全局作用域销毁时，进入该方法
    }
}

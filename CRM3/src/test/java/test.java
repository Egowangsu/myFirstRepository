import com.sun.org.apache.xpath.internal.operations.String;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class test {
    @Test
    public void test01() {
        File file=new File("D:\\CRM3\\src\\main\\resources\\test.txt");
        FileInputStream is = null; //输入流
        FileOutputStream os=null;   //输出流
        try {  //执行文件复制操作
            byte[] bytes = new byte[1024*1024];  //每次可以存1M大小
            is = new FileInputStream(file);
            os=new FileOutputStream("D:/test.txt");
            int readCount;
            //这里返回的是读取到的字节数量，放入到数组中
            while((readCount=is.read(bytes))!=-1){
                //将bytes数组转为String输出
               os.write(bytes);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is!= null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    @Test
    public void mytest(){
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                for(int i=0;i<1000;i++)
                    System.out.println("分支线程抢到时间片------->"+i);

            }
        });
        t.start();  //开辟新的栈空间（分支栈）
        //执行主线程的方法
        for(int i=0;i<1000;i++){
            System.out.println("主支线程抢到时间片------->"+i);
        }
    }
    @Test
    public void mytest02(){
        for(int i=0;i<10;i++){
            System.out.println(Thread.currentThread().getName()+"--->"+i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void myTest03(){
        System.out.println("第一次修改");
    }
}

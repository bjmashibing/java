package com.mashibing;

/**
 * @author: 马士兵教育
 * @create: 2019-08-24 21:33
 */
/*
* 在java中明确定义了访问权限：
*          限制访问，以下分类按照从大到小进行排列
*       public:公共的
*           当前项目的所有的类都可以进行访问
*       protected:受保护的
*           可以被当前类访问，可以被当前包访问，也可以被子类访问
*       default:默认权限
*           可以被当前类访问，可以被当前包访问，
*       private:私有权限
*           只能被当前类访问
*
*       注意：四种访问修饰符可以修饰属性和方法
*       类的访问修饰符只有两个  public default
*
* */
public class AccessControlDemo {


    public AccessControlDemo(){

    }

    protected AccessControlDemo(int a,int b){

    }
    private String str = "test";
}

class Test{

}
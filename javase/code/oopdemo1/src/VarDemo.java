/**
 * @author: 马士兵教育
 * @create: 2019-08-24 15:02
 */
/*
* 变量：
*   局部变量：
*       定义在方法中的变量称之为局部变量
*           作用域：从定义的位置开始到整个方法结束
*           局部变量不包含默认值，如果没有使用当前变量的话，可以不赋值
*           注意：局部变量只能在当前方法中使用，其他地方无法使用
*   成员变量：
*       定义在方法外，类内的变量叫做成员变量（全局变量）
*       成员变量包含初始值：int 0 String null boolean false
*           作用域：整个类体内
*
* */
public class VarDemo {
    int age = 20;

   public void test(){
       System.out.println(age);
       age = 10;
       System.out.println(age);
       int age = 30;
       System.out.println(age);
   }
    public void show(){
        //局部变量
        int a ;
        String name = "zhangsan";
        System.out.println(age);
//        System.out.println(a);
    }


    public static void main(String[] args) {
       VarDemo vd = new VarDemo();
        System.out.println(vd.age);
        vd.test();
        System.out.println(vd.age);


    }
}

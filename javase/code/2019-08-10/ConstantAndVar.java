/*
常量和变量
	常量：在程序运行过程中，值不会发生改变的量叫做常量
	变量：在程序运行过程中，值会发生改变的量叫做变量
	
	变量：变量名称，变量类型，作用域
		1、先声明，后赋值
			int a;声明
			a =10;赋值
		2、声明+赋值
	常量：
		使用final关键字修饰的变量称之为常量或者叫做最终常量，表示不可修改
	注意：
		在类内，方法外定义的变量叫成员变量,会存在默认值
		在方法内，定义的变量必须要进行初始化操作，不会存在默认值
		在一行中可以定义多个变量，但是不推荐，每个变量最好单独一行
		给变量赋值过程中，变量的值成为常量
*/
public class ConstantAndVar{
	
	static int d ;
	public static void main(String[] args){
		//声明
		int a;
		//赋值
		a =10;
		//声明+赋值
		//int b = 20;
		//int c;
		//System.out.println(d);
		//int x=10,y=20;
		
		System.out.println(a);
		a=30;
		System.out.println(a);
		final int c = 20;
		System.out.println(c);
		c=30;
		System.out.println(c);



		
		
	}
	
}
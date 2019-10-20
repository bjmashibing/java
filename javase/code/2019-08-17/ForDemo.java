/*
第三种循环结构是for循环，使用最多

	语法规则：
		for(初始化1;条件表达式2;步进器4){
			代码逻辑3
		}
	使用for循环的好处：
		1、代码简洁
		2、变量初始化的时候，for循环的作用域仅仅是当前for循环结构
			while循环的作用域是从变量的定义开始到整个方法结束

*/

public class ForDemo{
	public static void main(String[] args){
		/*
		for(int i = 1;i<=100;i++){
			System.out.println("第"+i+"遍输出");
		}
		
		//100以内的偶数和
		int sum = 0;
		for(int i = 1;i<=100;i++){
			if(i%2==0){
				sum+=i;
			}
		}
		System.out.println("100以内的偶数和是："+sum);
		*/
		for(int i =1;;){
			System.out.println(i);
		}
	}
}
/*
循环结构：
	1、while	先进行判断，再进行逻辑执行
		需要四部分组成
			初始化：变量的初始化
			条件判断：必须要求返回true或者false的值
			循环体：具体的要执行的逻辑代码
			迭代变量：促使此循环结束
	2、do while		先执行代码逻辑，再执行判断
		

*/

public class WhileDemo{
	
	public static void main(String [] args){
		
		
		//while循环样例
		/*
		int i = 1;
		while(i<=100){
			System.out.println("第"+i+"遍输出");
			i++;
		}
		*/
		
		//求100内的偶数和
		/*
		int i = 1;
		//求和最终的存储变量
		int sum = 0;
		while(i<=100){
			if(i % 2 == 0){
				sum+=i;
			}
			i++;
		}
		System.out.println("100以内的偶数和是："+sum);
		*/
		
		// do while
		/*
		int i = 1;
		do{
			System.out.println("第"+i+"遍输出");
			i++;
		}while(i<=100);
		*/
		int i = 1;
		int sum = 0;
		do{
			if(i % 2 == 0){
				sum+=i;
			}
			i++;
		}while(i<=100);
		System.out.println("100以内的偶数和是："+sum);
		
	}
}
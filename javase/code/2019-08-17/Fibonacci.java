import java.util.Scanner;
/*
斐波那契数列，可以选择打印多少个值
*/
public class Fibonacci{
	
	public static void main(String[] args){
		/*
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入要打印的斐波那契数列的个数");
		int count = sc.nextInt();
		int x = 1;
		int y = 1;
		int z = 0;
		//前两位是1
		for(int i = 1;i<=count;i++){
			if(i==1 || i==2){
				System.out.print(1+"\t");
			}else{
				z=x+y;
				x=y;
				y=z;
				System.out.print(z+"\t");
			}
		}
		*/
		for(int i = 1;i<=10;i++){
			System.out.print(getNumber(i)+"\t");
		}
	}
	
	/*
	递归函数：
		再程序运行过程中，有时需要调用程序本身，此时可以使用递归
		注意：
			再程序中，能不使用递归就不要使用递归
				使用递归的时候会加大资源的消耗
				如果递归的层次比较深，会造成栈溢出。
			如果不使用递归无法解决问题的话，就必须要使用递归
				比如：输出某个磁盘目录下的所有文件名称
	
	*/
	
	public static int getNumber(int number){
		if(number==1||number==2){
			return 1;
		}else{
			return getNumber(number-1)+getNumber(number-2);
		}
	}
	
}
import java.util.Scanner;
public class TenToTwo{
	
	public static void main(String[] args){
		
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入一个十进制数：");
		int number = sc.nextInt();
		String str = "";
	
		while(number!=0){	
			int i = number % 2;
			str = i+str;
			number = number/2;
		}
		
		System.out.println(str);
	}
}
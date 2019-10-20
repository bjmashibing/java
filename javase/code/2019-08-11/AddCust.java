import java.util.Scanner;
public class AddCust{
	
	public static void main(String[] args){
		
		System.out.println("欢迎光临马士兵教育会员系统");
		System.out.println("添加用户信息");
		//创建Scanner对象
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入会员号码<4位整数>");
		String number = sc.nextLine();
		System.out.println("请输入会员生日<日/月>");
		String birthday = sc.nextLine();
		System.out.println("请输入会员积分");
		String score = sc.nextLine();
		
		if(number.length()==4){
			System.out.println("会员信息如下");
			System.out.println(number+"\t"+birthday+"\t"+score);
		}else{
			System.out.println("会员号码输入错误，请输入4位整数");
			
		}
	}
}
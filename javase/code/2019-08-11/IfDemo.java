import java.util.Scanner;
/*
分支结构：
	单分支结构：
		只做单一条件的判断，如果符合，做某些事情
	双分支结构：
		当做条件判断的时候，只有两种选择
	多分支结构：
		可以进行多个条件的判断，每个匹配项可以选择不同的执行结果
	嵌套分支结构：
		在分支结构中嵌套分支结构
	switch多分支结构：
		一般用作等值判断
*/
public class IfDemo{
	
	public static void main(String[] args){
		
		//单分支判断,Math.random()产生数据的范围是[0,1)
		//得到0-5之间的随机数
		//int i = (int)(Math.random()*6);
		//if(i>3){
		//	System.out.println("值大于3");
		//}
		//System.out.println("number:"+i);
		
		/*
		double i = 6 * Math.random();
		double j = 6 * Math.random();
		double k = 6 * Math.random();
		int count = (int) (i + j + k);
		if(count > 15) {
			System.out.println("今天手气不错");
		}
		if(count >= 10 && count <= 15) {   //错误写法：10<count<15
			System.out.println("今天手气很一般");
		}
		if(count < 10) {
			System.out.println("今天手气不怎么样");
		}
		System.out.println("得了" + count + "分");*/
		
		
		//双分支结构
		/*
		int r = 1;
		double PI = 3.14;
		double area = PI * r * r;
		double length = 2 * PI * r;
		if(area >= length){
			System.out.println("面积大于等于周长");
		}else{
			System.out.println("周长大于面积");
		}
		*/
		//Scanner
		//创建文件扫描器对象，System.in表示的是标准输入，可以从控制台读取数据(装饰者模式)
		//注意:每次读取回来的值都是字符串类型，需要进行类型转换
		//Scanner sc = new Scanner(System.in);
		//System.out.println("请输入数据");
		//String str = sc.nextLine();
		//System.out.println(str);
		
		
		//多分支结构
		/*
		int age = (int)(Math.random()*100);
		if(age<10){
			System.out.println("儿童");
		}else if(age<20){
			System.out.println("少年");
		}else if(age<30){
			System.out.println("青年");
		}else if(age<50){
			System.out.println("中年");
		}else if(age<70){
			System.out.println("老年");
		}else{
			System.out.println("耄耋");
		}
		*/
		
		//嵌套分支结构
		/*
		int time = (int)(Math.random()*40);
		if(time<20){
			System.out.println("恭喜进入决赛");
			String sex = ((int)(Math.random()*2))==0?"girl":"boy";
			if(sex=="girl"){
				System.out.println("欢迎进入女子组");
			}else{
				System.out.println("欢迎进入男子组");
			}
		}else{
			System.out.println("成绩太差，被淘汰");
		}
		*/
		
		//switch多分支结构
		/*注意：
			1、每个case模块中要添加break，防止多次匹配
			2、如果多个case中处理的逻辑代码块的功能一致，可以考虑只在最后添加一次处理
			3、default表示默认选项，当所有的case不匹配的时候，会执行此选项
			4、defult可以有，也可以没有
		*/
		int random = (int)(Math.random()*26);
		char ch = (char)('a'+random);
		switch(ch){
			/*
			case 'a':
			System.out.println("元音："+ch);
			break;
			case 'e':
			System.out.println("元音："+ch);
			break;
			case 'i':
			System.out.println("元音："+ch);
			break;
			case 'o':
			System.out.println("元音："+ch);
			break;
			case 'u':
			System.out.println("元音："+ch);
			break;
			case 'y':
			System.out.println("半元音："+ch);
			break;
			case 'w':
			System.out.println("半元音："+ch);
			break;
			default:
			System.out.println("辅音："+ch);*/
			case 'a':
			case 'e':
			case 'i':	
			case 'o':
			case 'u':
			System.out.println("元音："+ch);
			break;
			case 'y':
			case 'w':
			System.out.println("半元音："+ch);
			break;
			default:
			System.out.println("辅音："+ch);
		}
		
		
		
		
		
		
		
		
		
		
		
	}
}
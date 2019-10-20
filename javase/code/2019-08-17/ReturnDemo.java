/*

return有两基本用途
	1、返回方法的返回值
	2、终止当前程序
		
*/

public class ReturnDemo{
	
	public static void main(String[] args){
		
		System.out.println(get());
		for(int i = 0;i<10;i++){
			System.out.println(i);
			if(i==5){
				return;
				//System.exit(100);
			}
			System.out.println("接着执行");
		}
		
	}
	
	public static int get(){
		return 100;
	}
}
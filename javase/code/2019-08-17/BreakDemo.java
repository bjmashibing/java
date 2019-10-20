/*
生成0-100随机数，直到生成88为止，停止循环！
	break:跳出本层循环,当包含多层循环的时候，break只能跳出内层循环，无法跳出外层循环
*/

public class BreakDemo{
	
	public static void main(String[] args){
		/*
		int count = 0;
		while(true){
			int i = (int)(Math.random()*101);
			
			if(i==88){
				break;
			}
			count++;
			System.out.println(count+"--"+i);
		}
		*/
		
		//请打印输出(1,1)(1,2)(1,3)...直到输出(6,6)停止
		for(int i = 1;i<10;i++){
			for(int j =1;j<10;j++){
				System.out.println("("+i+","+j+")");
				if(i==6&&j==6){
					return;
				}
			}
		}
		
	}
}
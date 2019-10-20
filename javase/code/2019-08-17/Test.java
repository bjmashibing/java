public class Test{
	
	public static void main(String[] args){
		int count = 0;
		for(int i = 0;i<1000;i++){
			if(i%5==0){
				System.out.print(i+"\t");
				count++;
			}
			if(count>=3){
				System.out.println();
				count = 0;
			}
		}
	}
}
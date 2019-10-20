
/*
运算符：
	算术运算符:  +，-，*，/，%，++，--
		
	赋值运算符 =  
		java中=表示赋值运算符，==表示相等的操作
	扩展赋值运算符:+=，-=，*=，/= 
		
	关系运算符:  >，<，>=，<=，==，!=
		用来比较两值的关系，
	逻辑运算符:  &&，||，!
		逻辑运算符一般两边的值不是具体的值，而是表达式
	位运算符:  &，|，^，~ ， >>，<<，>>> (了解！！！)
	条件运算符 ？： 

*/
public class OperatorDemo{
	
	public static void main(String[] args){
		int a = 1;
		int b = 2;
		
		//算数运算符
		System.out.println(a+b);
		System.out.println(a-b);
		System.out.println(a*b);
		//取整除，或者取商
		System.out.println(a/b);
		//取余数，或者取模
		System.out.println(a%b);
		
		//单目运算符
		//++，表示在变量原有的基础上+1，谁在前，先运算谁
		System.out.println(a++);
		System.out.println(++a);
		//--，表示在变量原有的基础上-1，谁在前，先运算谁
		System.out.println(a--);
		System.out.println(--a);
		//6 ????
		System.out.println(++b+b++);
		
		//扩赋值运算符
		int c = 1;
		//两写法意义一样，表示加2的基本操作
		c = c + 2;
		c += 2;
		//类型转换问题，d是byte，d+1整体变成int类型，需要将int类型的值转换位byte，会有精度算是，因此需要强制转换
		//建议在进行操作的时候使用扩赋值运算符
		byte d = 10;
		//d = d+1;
		d+=1;
		
		//关系运算符:返回的值是布尔类型，也就是说只有true和false两情况
		System.out.println(1>2);
		System.out.println(1<2);
		System.out.println(1>=2);
		System.out.println(1<=2);
		System.out.println(1==2);
		System.out.println(1!=2);
		System.out.println("-----");
		//逻辑运算符
		/*
			&&:表示短路与，两边表达式中只要有一个是false，整体结果就是false
				两边表达式从左向右开始对比，如果左边的表达式是false，右边不需要进行判断
			||:表示短路或，两边表达式中只要有一个是true，整体结果就是true
				两边表达式从左向右开始对比，如果左边的表达式是true，右边不需要进行判断
			!:取反，如果是true，取反是false，如果是false，取反就是true
			&:与运算符，但是两都会参与运算
			|:或运算符，两边都会参与运算
		*/
		System.out.println(3>5 & 3<4);
		System.out.println(3>5 | 3<4);
		System.out.println(!true);
		System.out.println(!false);
		
		//位运算符：只能操作数值，操作的时候会转成二进制进行运算
		System.out.println(4 & 5);
		System.out.println(4 | 5);
		System.out.println(4 ^ 5);
		//移码，补码，原码，反码  ？？？？
		System.out.println(~4);
		//左移表示乘以2，右移表示除以2
		System.out.println(2<<3);
		System.out.println(16>>2);
		
		//条件运算符或者三目运算符
		//使用的时候需要跟一个表达式，表达式如果是true，则返回？后的结果，
		//如果是false，则返回：后的结果
		System.out.println(3>2?3:2);
		System.out.println(false?false:true?false:true);
		
		
		/*
		
		基本数据类型之间的转换
			自动转换(隐形转换)：
			强制转换
			
			注意:
				1、在进行算术运算操作的时候，必须要求数据类型一致，否则无法操作
				2、在运算过程中，如果两个值得类型不一致，会自动将小的类型转换为大的类型
				3、在运算过程中，可以手动强制转换，将大的类型转换为小的类型
					实现方式，(datatype)
				4、强制转换，会发生精度损失，结果可能不准确
		*/
		byte bb = 10;
		int aa = 200;
		byte cc;
		cc = (byte)(aa+bb);
		System.out.println(cc);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
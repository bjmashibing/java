# lambda表达式的使用

### 1、Lambda表达式概念

lambda表达式可以替代只有一个抽象函数的接口实现，告别匿名内部类，代码看起来更简洁易懂

lambda表达式同时还提升了对集合、框架的迭代、遍历、过滤数据的操作

lambda可以极大的减少代码冗余，同时代码的可读性要好过冗长的内部类，匿名类

### 2、lambda表达式的语法

​		LambdaParameters -> LambdaBody

![1570942180646](C:\Users\seanzhou\AppData\Roaming\Typora\typora-user-images\1570942180646.png)

​		args->expr或者（object ... args）->{函数式接口抽象方法实现逻辑}

​		1、（）参数的个数，根据函数式接口里面抽象的参数个数来决定，当参数只有一个的时候，（）可以省略

​		2、当expr逻辑非常简单的时候，{}和return可以省略

### 3、表达式案例

​		()->{}

​		()->{System.out.println(1);}

​		()->System.out.println(1)

​		()->{return 100;}

​		()->100

​		()->null

​		(int x)->{return x+1;}

​		(int x)->x+1

​		(x)->x+1

​		x->x+1
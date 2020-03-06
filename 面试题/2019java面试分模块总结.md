# 2019java面试分模块总结

### 设计模式

1、用到哪些设计模式？怎么用的

2、单例模式的不同写法

### JDK

1、聊一下java的集合类

2、HashMap底层数据结构，以及解决hash碰撞的方法

3、Hashmap为什么要使用红黑树

4、集合类怎么解决高并发问题

5、队列的使用问题

6、自定义异常的应用场景

7、Object类中的方法

8、1.8的新特性

9、Java中的静态方法只有一个实例，如果想用多个实例怎么办？

10、Java面向对象的基本特征，继承、封装与多态

11、重写和重载是什么意思

12、怎样声明一个类不会被继承

13、HashMap中jdk1.7与jdk1.8的区别

14、concurrenthashMap 的底层实现原理，是如何实现线程安全的？

15、Java中的自增是线程安全的吗，如何实现线程安全的自增

16、Jdk1.8种的stream有用过吗，stream的并行操作原理，stream并行的线程池是从哪里来的

17、Jdk1.8的completableFuture有用过吗？

18、Java种的代理有几种实现方式

### JVM

1、jvm内存模型，以及这些空间的存放内容 

2、堆内存划分的空间，如何回收这些内存对象，有哪些回收算法

3、jvm调优，如何解决线上gc问题

4、class初始化过程

5、内存溢出的原因，如何排查线上问题

6、jvm有哪些垃圾回收器，

7、类加载模型

8、JVM为什么要增加元空间？

9、堆G1垃圾收集器有了解么，有什么特点

### 多线程

1、多线程之间是如何通信的

2、synchronized底层实现，和lock的区别

3、synchronized关键字加在静态方法和实例方法的区别

4、countdownlatch的用法

5、线程池

​		（1）Executor提供了几种线程池

​		（2）线程池的参数

​		（3）拒绝策略

​		（4）任务放置的顺序过程

​		（5）任务结束后会不会回收线程

​		（6）未使用的线程池中的线程放在哪里

​		（7）线程池线程存在哪

​		（8）cache线程池会不会销毁核心线程

6、Java多线程的几种状态及线程各个状态之间是如何切换的

7、Java中的wait和sleep的区别与联系

8、如何在方法栈中进行数据传递？

9、ThreadLocal的底层实现形式及实现的数据结构？

10、Sychornized是否是公平锁

11、Sychronized和ReentryLock的区别

12、服务器CPU数量及线程池数量的关系

### mysql

1、Mysql的索引类型，底层索引数据结构，叶子节点存储的是什么，索引失效的原因

2、如何优化sql，查询计划的结果中看哪些些关键数据

3、innodb和myisam的区别

4、mysql默认隔离级别，

5、mysql的乐观锁和悲观锁，锁的种类

6、如何用sql实现乐观锁和悲观锁

7、mysql如何分库分表

8、MySQL为什么选择B+树作为它的存储结构，为什么不选择Hash、二叉、红黑树

9、Mysql数据库的事务与锁的理解

10、数据库临时表有没有用过，是怎么用的？

11、多数据源情况下如何进行事物的管理

12、Union和union all有什么区别

13、dateTime和timestamp有什么区别

14、mysql主从模式的实现

15、如何解析sql语句；即explain关键字的使用

16、Mysql的主从同步原理，mysql主从复制主要有几种模式

### Spring

1、spring的底层代码，

2、bean的生命周期 

3、循环引用问题，以及spring中用到的设计模式

4、spring和springBoot的区别

5、spring的AOP的底层实现原理

6、spring的事务是如何回滚的

7、Spring 是如何解决循环依赖的问题的？

8、Spring IOC的理解，原理与实现

9、Bean Factory与FactoryBean有什么区别？@Bean这个注解时如何实现Bean的注入的？

### Mybatis

1、hibernate的区别

2、mybatis的缓存，都缓存些什么，session缓存存在哪

3、mybatis的执行流程，需要了解源码

4、mybatis如何防止sql注入

### redis

1、redis的数据结构类型，一般都用在什么场景下 

2、sortedSet的底层数据结构

3、利用redis实现分布式锁

4、redis使用单线程的好处

5、redis中如何控制多线程并发

6、redis删除key的策略

7、redis的主动缓存，被动缓存

8、如何保证数据一致性问题

9、集群环境下如何处理，解释一下一致性哈希

10、解释一下缓存击穿，缓存穿透，缓存雪崩，如何解决这些问题

11、排行榜功能的实现：使用redis的zset；zset的底层数据结构是什么样的；除了redis的zset还有什么其他的数据结构可以实现这个功能

12、Redis集群种类：主从模式、cluster模式及其应用

13、Redis种数据类型及应用场景

### zookeeper

1、zookeeper如何保证可用性？

2、Zookeeper的原理

3、什么情况下会使用zookeeper，zookeeper如何监听生成的节点，zk内部是如何实现的

4、Zookeeper0、zookeeper1、zookeeper2，三个节点的集群描述一下从zk启动，到zk对外提供服务的整个过程

5、有一个key，往zk写入，到写入成功它的大体过程是什么样的

6、Zookeeper监听器的原理

### MQ

1、mq的结构图

2、如何保证消费者是否消费

3、ack返回的种类

4、请求过程服务宕机如何处理

5、什么场景下会使用MQ；MQ的优势与劣势，什么时候不能用MQ

### springboot

1、springboot启动过程中做了哪些事情？

2、Springboot 启动类上的注解 @spring boot Application说明？

3、Springboot如何判断当前应用是否时web应用？

4、Spring boot整合jsp的流程，需要注意哪些点

### SpringCloud

1、SpringCloud和dubbo的区别

2、项目中用到了哪些组件

3、eureka的原理，如何保证高可用性，和Zookeeper有什么区别

4、feign如何调用的

5、处理生产环境上配置生效问题

6、hystrix的降级策略有哪些

7、Springcloud eureka是如何注册服务、如何监测心跳的，它注册的流程是怎么样的

8、在分布式环境中如何快速发现某一台服务有问题

9、分布式集群系统对外提供接口的时候如何验证对方的身份

10、Eureka和zookeeper作为注册中心有什么区别

### Linux服务器

1、Linux如何查看应用占用内存情况

2、Linux脚本编写会么

### 架构设计

1、如何做一个秒杀系统

2、如何实现高可用、高并发、高吞吐的技术方案

3、A给B发请求，B执行往数据库insert的操作；设计一个架构，保证能够提供最大的并发量

### 其他小知识点

1、Nginx如何做限流？令牌桶算法与漏桶算法

2、Tomcat调优

3、为什么使用RPC框架，什么时候会使用http请求

4、tomcat启动原理

5、Http协议

6、epoll模型

7、如何实现调用远程服务的接口
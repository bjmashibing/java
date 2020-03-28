# 01Mybatis的介绍和基本使用

### 0、数据库操作框架的历程

##### 		(1)	JDBC

​		JDBC(Java Data Base Connection,java数据库连接)是一种用于执行SQL语句的Java API,可以为多种关系数据库提供统一访问,它由一组用Java语言编写的类和接口组成.JDBC提供了一种基准,据此可以构建更高级的工具和接口,使数据库开发人员能够编写数据库应用程序

- 优点：运行期：快捷、高效
- 缺点：编辑期：代码量大、繁琐异常处理、不支持数据库跨平台

![img](images\jdbc.jpg)

##### 		(2) DBUtils

​		DBUtils是Java编程中的数据库操作实用工具，小巧简单实用。

​		DBUtils封装了对JDBC的操作，简化了JDBC操作，可以少写代码。

​		DBUtils三个核心功能介绍

​			1、QueryRunner中提供对sql语句操作的API

​			2、ResultSetHandler接口，用于定义select操作后，怎样封装结果集

​			3、DBUtils类，它就是一个工具类，定义了关闭资源与事务处理的方法

##### 		(3)Hibernate

​		Hibernate 是由 Gavin King 于 2001 年创建的开放源代码的对象关系框架。它强大且高效的构建具有关系对象持久性和查询服务的 Java 应用程序。

​		Hibernate 将 Java 类映射到数据库表中，从 Java 数据类型中映射到 SQL 数据类型中，并把开发人员从 95% 的公共数据持续性编程工作中解放出来。

​		Hibernate 是传统 Java 对象和数据库服务器之间的桥梁，用来处理基于 O/R 映射机制和模式的那些对象。

![image](images\hibernate.jpg)

​		**Hibernate 优势**

- Hibernate 使用 XML 文件来处理映射 Java 类别到数据库表格中，并且不用编写任何代码。

- 为在数据库中直接储存和检索 Java 对象提供简单的 APIs。

- 如果在数据库中或任何其它表格中出现变化，那么仅需要改变 XML 文件属性。

- 抽象不熟悉的 SQL 类型，并为我们提供工作中所熟悉的 Java 对象。

- Hibernate 不需要应用程序服务器来操作。

- 操控你数据库中对象复杂的关联。

- 最小化与访问数据库的智能提取策略。

- 提供简单的数据询问。

  **Hibernate劣势**

- hibernate的完全封装导致无法使用数据的一些功能。

- Hibernate的缓存问题。

- Hibernate对于代码的耦合度太高。

- Hibernate寻找bug困难。

- Hibernate批量数据操作需要大量的内存空间而且执行过程中需要的对象太多

  ##### (4) JDBCTemplate

​       JdbcTemplate针对数据查询提供了多个重载的模板方法,你可以根据需要选用不同的模板方法.如果你的查询很简单，仅仅是传入相应SQL或者相关参数，然后取得一个单一的结果，那么你可以选择如下一组便利的模板方法。

​		优点：运行期：高效、内嵌Spring框架中、支持基于AOP的声明式事务
​		缺点：必须于Spring框架结合在一起使用、不支持数据库跨平台、默认没有缓存

### 1、什么是Mybatis？

​		MyBatis 是一款优秀的持久层框架，它支持自定义 SQL、存储过程以及高级映射。MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。

​		**优点：**

​		1、与JDBC相比，减少了50%的代码量

​		2、 最简单的持久化框架，简单易学

​		3、SQL代码从程序代码中彻底分离出来，可以重用

​		4、提供XML标签，支持编写动态SQL

​		5、提供映射标签，支持对象与数据库的ORM字段关系映射

​		**缺点：**

​		1、SQL语句编写工作量大，熟练度要高

​		2、数据库移植性比较差，如果需要切换数据库的话，SQL语句会有很大的差异

### 2、第一个Mybatis项目

​		1、创建普通的maven项目

​		2、导入相关的依赖

​		pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.mashibing</groupId>
    <artifactId>mybatis_helloworld</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.5.4</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.16</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/log4j/log4j -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>

    </dependencies>

</project>
```

​		3、创建对应的数据表，数据表我们使用之前的demo数据库，脚本文件在群里，大家自行去下载安装

​		4、创建与表对应的实体类对象

emp.java

```java
package com.mashibing.bean;

import java.util.Date;

public class Emp {

    private Integer empno;
    private String ename;
    private String job;
    private Integer mgr;
    private Date hiredate;
    private Double sal;
    private Double common;
    private Integer deptno;

    public Emp() {
    }

    public Emp(Integer empno, String ename, String job, Integer mgr, Date hiredate, Double sal, Double common, Integer deptno) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.common = common;
        this.deptno = deptno;
    }

    public Integer getEmpno() {
        return empno;
    }

    public void setEmpno(Integer empno) {
        this.empno = empno;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Integer getMgr() {
        return mgr;
    }

    public void setMgr(Integer mgr) {
        this.mgr = mgr;
    }

    public Date getHiredate() {
        return hiredate;
    }

    public void setHiredate(Date hiredate) {
        this.hiredate = hiredate;
    }

    public Double getSal() {
        return sal;
    }

    public void setSal(Double sal) {
        this.sal = sal;
    }

    public Double getCommon() {
        return common;
    }

    public void setCommon(Double common) {
        this.common = common;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "empno=" + empno +
                ", ename='" + ename + '\'' +
                ", job='" + job + '\'' +
                ", mgr=" + mgr +
                ", hiredate=" + hiredate +
                ", sal=" + sal +
                ", common=" + common +
                ", deptno=" + deptno +
                '}';
    }
}
```

​		5、创建对应的dao类

EmpDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Emp;

public interface EmpDao {

    public Emp findEmpByEmpno(Integer empno);
    
}
```

​		6、编写配置文件

log4j.properties

mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <!--配置数据库连接-->
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/demo?serverTimezone=UTC"/>
                <property name="username" value="root"/>
                <property name="password" value="123456"/>
            </dataSource>
        </environment>
    </environments>
    <!--引入每一个接口对应点xml文件-->
    <mappers>
        <mapper resource="EmpDao.xml"/>
    </mappers>
</configuration>
```

EmpDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace:编写接口的全类名，就是告诉要实现该配置文件是哪个接口的具体实现-->
<mapper namespace="com.mashibing.dao.EmpDao">
    <!--
    select:表示这个操作是一个查询操作
    id表示的是要匹配的方法的名称
    resultType:表示返回值的类型，查询操作必须要包含返回值的类型
    #{属性名}：表示要传递的参数的名称
    -->
    <select id="findEmpByEmpno" resultType="com.mashibing.bean.Emp">
        select * from emp where empno = #{empno}
  </select>
</mapper>
```

​		7、编写测试类

MyTest.java

```java
package com.mashibing.test;

import com.mashibing.bean.Emp;
import com.mashibing.dao.EmpDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyTest {

    @Test
    public void test01() {
        // 根据全局配置文件创建出SqlSessionFactory
        // SqlSessionFactory:负责创建SqlSession对象的工厂
        // SqlSession:表示跟数据库建议的一次会话
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // 获取数据库的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Emp empByEmpno = null;
        try {
            // 获取要调用的接口类
            EmpDao mapper = sqlSession.getMapper(EmpDao.class);
            // 调用方法开始执行
            empByEmpno = mapper.findEmpByEmpno(7369);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        System.out.println(empByEmpno);
    }
}
```

### 3、增删改查的基本操作

EmpDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Emp;

public interface EmpDao {

    public Emp findEmpByEmpno(Integer empno);

    public int updateEmp(Emp emp);

    public int deleteEmp(Integer empno);

    public int insertEmp(Emp emp);

}
```

EmpDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace:编写接口的全类名，就是告诉要实现该配置文件是哪个接口的具体实现-->
<mapper namespace="com.mashibing.dao.EmpDao">
    <!--
    select:表示这个操作是一个查询操作
    id表示的是要匹配的方法的名称
    resultType:表示返回值的类型，查询操作必须要包含返回值的类型
    #{属性名}：表示要传递的参数的名称
    -->
    <select id="findEmpByEmpno" resultType="com.mashibing.bean.Emp">
        select * from emp where empno = #{empno}
    </select>
    <!--增删改查操作不需要返回值，增删改返回的是影响的行数，mybatis会自动做判断-->
    <insert id="insertEmp">
        insert into emp(empno,ename) values(#{empno},#{ename})
    </insert>
    <update id="updateEmp">
        update emp set ename=#{ename} where empno = #{empno}
    </update>
    <delete id="deleteEmp">
        delete from emp where empno = #{empno}
    </delete>
</mapper>
```

MyTest.java

```java
package com.mashibing.test;

import com.mashibing.bean.Emp;
import com.mashibing.dao.EmpDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyTest {
    SqlSessionFactory sqlSessionFactory = null;
    @Before
    public void init(){
        // 根据全局配置文件创建出SqlSessionFactory
        // SqlSessionFactory:负责创建SqlSession对象的工厂
        // SqlSession:表示跟数据库建议的一次会话
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory= new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void test01() {

        // 获取数据库的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Emp empByEmpno = null;
        try {
            // 获取要调用的接口类
            EmpDao mapper = sqlSession.getMapper(EmpDao.class);
            // 调用方法开始执行
            empByEmpno = mapper.findEmpByEmpno(7369);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
        System.out.println(empByEmpno);
    }

    @Test
    public void test02(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        int zhangsan = mapper.insertEmp(new Emp(1111, "zhangsan"));
        System.out.println(zhangsan);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void test03(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        int zhangsan = mapper.updateEmp(new Emp(1111, "lisi"));
        System.out.println(zhangsan);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void test04(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        int zhangsan = mapper.deleteEmp(1111);
        System.out.println(zhangsan);
        sqlSession.commit();
        sqlSession.close();
    }
}
```

### 4、配置文件详解

​		在mybatis的项目中，我们发现了有一个mybatis-config.xml的配置文件，这个配置文件是mybatis的全局配置文件，用来进行相关的全局配置，在任何操作下都生效的配置。下面我们要针对其中的属性做详细的解释，方便大家在后续使用的时候更加熟练。

mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--引入外部配置文件，类似于Spring中的property-placeholder
    resource:从类路径引入
    url:从磁盘路径或者网络路径引入
    -->
    <properties resource="db.properties"></properties>
    <!--用来控制mybatis运行时的行为，是mybatis中的重要配置-->
    <settings>
        <!--设置列名映射的时候是否是驼峰标识-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    <!--typeAliases表示为我们引用的实体类起别名，默认情况下我们需要写类的完全限定名
    如果在此处做了配置，那么可以直接写类的名称,在type中配置上类的完全限定名，在使用的时候可以忽略大小写
    还可以通过alias属性来表示类的别名
    -->
    <typeAliases>
<!--        <typeAlias type="com.mashibing.bean.Emp" alias="Emp"></typeAlias>-->
        <!--如果需要引用多个类，那么给每一个类起别名肯定会很麻烦，因此可以指定对应的包名，那么默认用的是类名-->
        <package name="com.mashibing.bean"/>
    </typeAliases>
    <!--
    在实际的开发过程中，我们可能分为开发环境，生产环境，测试环境等等，每个环境的配置可以是不一样的
    environment就用来表示不同环境的细节配置，每一个环境中都需要一个事务管理器以及数据源的配置
    我们在后续的项目开发中几乎都是使用spring中配置的数据源和事务管理器来配置，此处不需要研究
    -->
    <!--default:用来选择需要的环境-->
    <environments default="development">
        <!--id:表示不同环境的名称-->
        <environment id="development">
            <transactionManager type="JDBC"/>
            <!--配置数据库连接-->
            <dataSource type="POOLED">
                <!--使用${}来引入外部变量-->
                <property name="driver" value="${driverClassname}"/>
                <property name="url" value="${url}"/>
                <property name="username" value="${username}"/>
                <property name="password" value="${password}"/>
            </dataSource>
        </environment>
    </environments>
    <!--
    在不同的数据库中，可能sql语句的写法是不一样的，为了增强移植性，可以提供不同数据库的操作实现
    在编写不同的sql语句的时候，可以指定databaseId属性来标识当前sql语句可以运行在哪个数据库中
    -->
    <databaseIdProvider type="DB_VENDOR">
        <property name="MySQL" value="mysql"/>
        <property name="SQL Server" value="sqlserver"/>
        <property name="Oracle" value="orcl"/>
    </databaseIdProvider>
    
    <!--将sql的映射文件适用mappers进行映射-->
    <mappers>
        <!--
        指定具体的不同的配置文件
        class:直接引入接口的全类名，可以将xml文件放在dao的同级目录下，并且设置相同的文件名称，同时可以使用注解的方式来进行相关的配置
        url:可以从磁盘或者网络路径查找sql映射文件
        resource:在类路径下寻找sql映射文件
        -->
<!--        <mapper resource="EmpDao.xml"/>
        <mapper resource="UserDao.xml"/>
        <mapper class="com.mashibing.dao.EmpDaoAnnotation"></mapper>-->
        <!--
        当包含多个配置文件或者配置类的时候，可以使用批量注册的功能，也就是引入对应的包，而不是具体的配置文件或者类
        但是需要注意的是，
        1、如果使用的配置文件的形式，必须要将配置文件跟dao类放在一起，这样才能找到对应的配置文件.
            如果是maven的项目的话，还需要添加以下配置，原因是maven在编译的文件的时候只会编译java文件
                <build>
                    <resources>
                        <resource>
                            <directory>src/main/java</directory>
                        <includes>
                            <include>**/*.xml</include>
                        </includes>
                    </resource>
                    </resources>
                </build>

        2、将配置文件在resources资源路径下创建跟dao相同的包名
        -->
        <package name="com.mashibing.dao"/>
    </mappers>
</configuration>
```

EmpDaoAnnotation.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Emp;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface EmpDaoAnnotation {

    @Select("select * from emp where empno = #{empno}")
    public Emp findEmpByEmpno(Integer empno);

    @Update("update emp set ename=#{ename} where empno = #{empno}")
    public int updateEmp(Emp emp);

    @Delete("delete from emp where empno = #{empno}")
    public int deleteEmp(Integer empno);

    @Insert("insert into emp(empno,ename) values(#{empno},#{ename})")
    public int insertEmp(Emp emp);

}
```


# 03mybatis框架整合及逆向工厂

### 1、三大框架整合

​		在老期的项目中，一般都是使用ssm项目做开发的，虽然现在的主流开发是springboot来做开发，但是ssm的基本整合还是需要掌握的。

##### 1、导入pom文件

1、导入spring的pom依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.mashibing</groupId>
    <artifactId>ssm</artifactId>
    <version>1.0-SNAPSHOT</version>

    <dependencies>
        <!-- https://mvnrepository.com/artifact/junit/junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-context -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.2.3.RELEASE</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/cglib/cglib -->
        <dependency>
            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>3.3.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.aspectj/aspectjweaver -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.9.5</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/aopalliance/aopalliance -->
        <dependency>
            <groupId>aopalliance</groupId>
            <artifactId>aopalliance</artifactId>
            <version>1.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-aspects -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aspects</artifactId>
            <version>5.2.3.RELEASE</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.alibaba/druid -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.1.21</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.48</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-web -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
            <version>5.2.3.RELEASE</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework/spring-webmvc -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.2.3.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>servlet-api</artifactId>
            <version>2.5</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jsp-api</artifactId>
            <version>2.0</version>
            <scope>provided</scope>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.10.3</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.10.3</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations -->
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-annotations</artifactId>
            <version>2.10.3</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/commons-io/commons-io -->
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.6</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/commons-fileupload/commons-fileupload -->
        <dependency>
            <groupId>commons-fileupload</groupId>
            <artifactId>commons-fileupload</artifactId>
            <version>1.4</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/javax.servlet/jstl -->
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>jstl</artifactId>
            <version>1.2</version>
        </dependency>
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
        <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis-spring -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>2.0.4</version>
        </dependency>

    </dependencies>
</project>
```

##### 2、编写各个框架的配置文件

web.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring.xml</param-value>
    </context-param>
    <!--springmvc的核心配置类-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:springmvc.xml</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
    <!--字符编码过滤器-->
    <filter>
        <filter-name>CharacterEncodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>utf-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>CharacterEncodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    <!--支持rest风格的过滤器-->
    <filter>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>HiddenHttpMethodFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```

springmvc.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context https://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--springmvc只扫描控制器-->
    <context:component-scan base-package="com.mashibing" use-default-filters="false">
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
    <!--静态资源的扫描-->
    <mvc:default-servlet-handler></mvc:default-servlet-handler>
    <!--动态资源的扫描-->
    <mvc:annotation-driven></mvc:annotation-driven>
    <!--配置视图管理器-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/page/"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>
</beans>
```

spring.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       https://www.springframework.org/schema/tx/spring-tx.xsd
">

    <!--Spring除了控制器不扫描，其他的组件都扫描，包括service，component等-->
    <context:component-scan base-package="com.mashibing">
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>

    <!--引入外部配置文件-->
    <context:property-placeholder location="classpath:db.properties"></context:property-placeholder>
    <!--配置数据源-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="username" value="${jdbc.username}"></property>
        <property name="password" value="${jdbc.password}"></property>
        <property name="driverClassName" value="${jdbc.driverName}"></property>
        <property name="url" value="${jdbc.url}"></property>
    </bean>

    <!--配置事务管理器的bean对象-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <!--开启基于注解的事务管理器的配置-->
    <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>

    <!--添加mybatis的配置-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--指定配置文件的位置-->
        <property name="configLocation" value="classpath:mybatis-config.xml"></property>
        <property name="dataSource" ref="dataSource"></property>
        <property name="mapperLocations" value="classpath:com/mashibing/dao/*.xml"></property>
    </bean>
    <!--创建mybatis扫描器，批量配置映射文件-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.mashibing.dao"></property>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"></property>
    </bean>
</beans>
```

mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    <typeAliases>
        <package name="com.mashibing.bean"/>
    </typeAliases>
</configuration>
```

db.properties

```properties
jdbc.username=root
jdbc.password=123456
jdbc.url=jdbc:mysql://192.168.85.111:3306/demo?serverTimezone=UTC
jdbc.driverName=com.mysql.jdbc.Driver
```

拷贝映射文件，还是位于com.mashibing.dao目录

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace:编写接口的全类名，就是告诉要实现该配置文件是哪个接口的具体实现-->
<mapper namespace="com.mashibing.dao.EmpDao">
<!--    <cache></cache>-->
<!--    <cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>-->
    <!--
    select:表示这个操作是一个查询操作
    id表示的是要匹配的方法的名称
    resultType:表示返回值的类型，查询操作必须要包含返回值的类型
    #{属性名}：表示要传递的参数的名称
    -->
    <select id="findEmpByEmpno" resultType="emp">
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

    <!--
    当查询语句中包含多个参数的是，如果使用#{属性名称}就无法获取具体的值了，那么应该如何使用呢？
        下面就是mybatis的参数传递方式
        1、如果是单个参数，
            基本类型：使用#{随便写}
            引用类型：使用#{类的属性名称}
        2、多个参数：
            当查询的时候传入多个参数的时候，就无法简单的通过#{参数名}来获取值了，
            只能通过arg0,arg1...或者param1,param2等方式来获取值
            原因就在于，mybatis在传入多个参数的时候，会将这些参数封装到一个map中，此时map中的key就是
            arg0,arg1,param1,param2这些值，但是很明显，这样的传值方式不是很友好，没有办法根据参数的名称来
            获取具体的值，因此可以使用如下的方式来指定参数的key是什么
            Emp selectEmpByNoAndName(@Param("empno") Integer empno, @Param("ename") String ename);
                也就是通过@Param来指定存入map中的key值是什么
        3、使用map来传递参数：
                依然是直接使用#{key}来获取具体的属性值
    -->

    <!--
        当使用#{}来获取值的时候会发现打印的sql语句如下：
            select * from emp where empno=? and ename=?
        当使用${}来获取值的时候会发现打印的sql语句如下：
            select * from emp where empno=7369 and ename='SMITH'
        通过刚刚的案例大家已经发现了存在的问题了，
        使用#{}方式进行取值：采用的是参数预编译的方式，参数的位置使用？进行替代，不会出现sql注入的问题
        使用${}方式进行取值：采用的是直接跟sql语句进行拼接的方式
    -->
    <select id="selectEmpByNoAndName" resultType="com.mashibing.bean.Emp">
        select * from ${t} where empno=${empno} and ename=${ename}
    </select>

    <select id="selectEmpByNoAndName2" resultType="com.mashibing.bean.Emp">
        select * from emp where empno=#{empno} and ename=#{ename}
    </select>

    <!--当返回值的结果是集合的时候，返回值的类型依然写的是集合中具体的类型-->
    <select id="selectAllEmp" resultType="com.mashibing.bean.Emp">
        select  * from emp
    </select>

    <!--在查询的时候可以设置返回值的类型为map，当mybatis查询完成之后会把列的名称作为key
    列的值作为value，转换到map中
    -->
    <select id="selectEmpByEmpReturnMap" resultType="map">
        select * from emp where empno = #{empno}
    </select>

    <!--注意，当返回的结果是一个集合对象的是，返回值的类型一定要写集合具体value的类型
    同时在dao的方法上要添加@MapKey的注解，来设置key是什么结果
    @MapKey("empno")
    Map<Integer,Emp> getAllEmpReturnMap();-->
    <select id="getAllEmpReturnMap" resultType="com.mashibing.bean.Emp">
        select * from emp
    </select>

    <!--再做查询的时候，有时候需要关联其他对象，因此需要使用关联查询
    可以通过下面自定义结果集的方式实现
    -->
    <select id="selectEmpAndDept" resultMap="empDept">
        select * from emp left join dept on emp.deptno = dept.deptno where empno = #{empno};
    </select>
    <!--<resultMap id="empDept" type="com.mashibing.bean.Emp">
        <id column="empno" property="empno"></id>
        <result column="ename" property="ename"></result>
        <result column="job" property="job"></result>
        <result column="mgr" property="mgr"></result>
        <result column="hiredate" property="hiredate"></result>
        <result column="sal" property="sal"></result>
        <result column="comm" property="common"></result>
        <result column="deptno" property="dept.deptno"></result>
        <result column="dname" property="dept.dname"></result>
        <result column="loc" property="dept.loc"></result>
    </resultMap>-->

    <!--在mybatis中还提供了一种简单的形式，使用association标签可以搞定
    -->
    <resultMap id="empDept" type="com.mashibing.bean.Emp">
        <id column="empno" property="empno"></id>
        <result column="ename" property="ename"></result>
        <result column="job" property="job"></result>
        <result column="mgr" property="mgr"></result>
        <result column="hiredate" property="hiredate"></result>
        <result column="sal" property="sal"></result>
        <result column="comm" property="common"></result>
        <association property="dept" javaType="com.mashibing.bean.Dept">
            <id column="deptno" property="deptno"></id>
            <result column="dname" property="dname"></result>
            <result column="loc" property="loc"></result>
        </association>
    </resultMap>

    <select id="selectEmpAndDeptBySimple" resultMap="simpleEmpAndDept">
        select * from emp where empno = #{empno}
    </select>
    <resultMap id="simpleEmpAndDept" type="com.mashibing.bean.Emp">
        <id column="empno" property="empno"></id>
        <result column="ename" property="ename"></result>
        <result column="job" property="job"></result>
        <result column="mgr" property="mgr"></result>
        <result column="hiredate" property="hiredate"></result>
        <result column="sal" property="sal"></result>
        <result column="comm" property="common"></result>
        <association property="dept" select="com.mashibing.dao.DeptDao.getDeptAndEmpsBySimple" column="deptno" fetchType="eager">
        </association>
    </resultMap>

    <select id="selectEmpByStep" resultType="com.mashibing.bean.Emp">
        select * from emp where deptno = #{deptno}
    </select>
    
   <!-- <select id="getEmpByCondition" resultType="com.mashibing.bean.Emp">
        select * from emp
        <where>
            <if test="empno!=null">
                empno > #{empno}
            </if>
            <if test="ename!=null">
                and ename like #{ename}
            </if>
            <if test="sal!=null">
                and sal > #{sal}
            </if>
        </where>-->
    <!--
    trim截取字符串：
    prefix：前缀，为sql整体添加一个前缀
    prefixOverrides:去除整体字符串前面多余的字符
    suffixOverrides:去除后面多余的字符串
    -->
    <select id="getEmpByCondition" resultType="com.mashibing.bean.Emp">
        select * from emp

        <trim prefix="where" prefixOverrides="and" suffixOverrides="and">
            <if test="empno!=null">
                empno > #{empno} and
            </if>
            <if test="ename!=null">
                ename like #{ename} and
            </if>
            <if test="sal!=null">
                sal > #{sal} and
            </if>
        </trim>
    </select>

    <!--foreach是对集合进行遍历
    collection="deptnos"  指定要遍历的集合
    close="" 表示以什么结束
    index="" 给定一个索引值
    item=""  遍历的每一个元素的值
    open=""  表示以什么开始
    separator="" 表示多个元素的分隔符
    -->
    <select id="getEmpByDeptnos" resultType="Emp">
        select * from emp where deptno in 
        <foreach collection="deptnos" close=")" index="idx" item="deptno" open="(" separator=",">
            #{deptno}
        </foreach>
    </select>

    <select id="getEmpByConditionChoose" resultType="com.mashibing.bean.Emp">
        select * from emp
        <where>
            <choose>
                <when test="empno!=null">
                    empno > #{empno}
                </when>
                <when test="ename!=null">
                    ename like #{ename}
                </when>
                <when test="sal!=null">
                    sal > #{sal}
                </when>
                <otherwise>
                    1=1
                </otherwise>
            </choose>
        </where>
    </select>
    <update id="updateEmpByEmpno">
        update emp
        <set>
            <if test="empno!=null">
                empno=#{empno},
            </if>
            <if test="ename!=null">
                ename = #{ename},
            </if>
            <if test="sal!=null">@
                sal = #{sal}
            </if>
        </set>
        <where>
            empno = #{empno}
        </where>
    </update>

</mapper>
```

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mashibing.dao.DeptDao">
    <!--定义查询集合元素-->
    <select id="getDeptAndEmps" resultMap="deptEmp">
        select * from dept left join emp on dept.deptno = emp.deptno where dept.deptno=#{deptno}
    </select>
    <resultMap id="deptEmp" type="com.mashibing.bean.Dept">
        <id property="deptno" column="deptno"></id>
        <result property="dname" column="dname"></result>
        <result property="loc" column="loc"></result>
        <!--封装集合类的元素
            property：指定集合的属性
            ofType:指定集合中的元素类型
        -->
        <collection property="emps" ofType="com.mashibing.bean.Emp">
            <id property="empno" column="empno"></id>
            <result column="ename" property="ename"></result>
            <result column="job" property="job"></result>
            <result column="mgr" property="mgr"></result>
            <result column="hiredate" property="hiredate"></result>
            <result column="sal" property="sal"></result>
            <result column="comm" property="common"></result>
        </collection>
    </resultMap>

    <select id="getDeptAndEmpsBySimple" resultType="com.mashibing.bean.Dept">
        select * from dept where deptno = #{deptno}
    </select>

    <select id="getDeptAndEmpsByStep" resultMap="deptEmpByStep">
        select * from dept where deptno = #{deptno}
    </select>
    <resultMap id="deptEmpByStep" type="com.mashibing.bean.Dept">
        <id property="deptno" column="deptno"></id>
        <result property="dname" column="dname"></result>
        <result property="loc" column="loc"></result>
        <!--封装集合类的元素
            property：指定集合的属性
            ofType:指定集合中的元素类型
        -->
        <collection property="emps" ofType="com.mashibing.bean.Emp" select="com.mashibing.dao.EmpDao.selectEmpByStep" column="deptno" >
        </collection>
    </resultMap>
</mapper>
```

添加dao的接口

EmpDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Emp;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmpDao {

    public Emp findEmpByEmpno(Integer empno);

    public int updateEmp(Emp emp);

    public int deleteEmp(Integer empno);

    public int insertEmp(Emp emp);

    Emp selectEmpByNoAndName(@Param("empno") Integer empno, @Param("ename") String ename, @Param("t") String tablename);
    Emp selectEmpByNoAndName2(Map<String, Object> map);

    List<Emp> selectAllEmp();

    Map<String,Object> selectEmpByEmpReturnMap(Integer empno);

    @MapKey("empno")
    Map<Integer,Emp> getAllEmpReturnMap();

    Emp selectEmpAndDept(Integer empno);
    Emp selectEmpAndDeptBySimple(Integer empno);
    List<Emp> selectEmpByStep(Integer deptno);

    public List<Emp> getEmpByCondition(Emp emp);
    public List<Emp> getEmpByConditionChoose(Emp emp);
    public List<Emp> getEmpByDeptnos(@Param("deptnos") List<Integer> deptnos);

    public int updateEmpByEmpno(Emp emp);

}

```

##### 3、创建测试类

TestController.java

```java
package com.mashibing.controller;

import com.mashibing.bean.Emp;
import com.mashibing.dao.EmpDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @Autowired
    EmpDao empDao;

    @RequestMapping("/test")
    public String test(){
        System.out.println("test");
        Emp empByEmpno = empDao.findEmpByEmpno(7369);
        System.out.println(empByEmpno);
        return "success";
    }
}
```

### 2、mybatis逆向工程

引入pom依赖

```xml
  <!-- https://mvnrepository.com/artifact/org.mybatis.generator/mybatis-generator-core -->
        <dependency>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-core</artifactId>
            <version>1.4.0</version>
        </dependency>
```

编写配置文件：

```xml
<!DOCTYPE generatorConfiguration PUBLIC
        "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="simple" targetRuntime="MyBatis3">
        <!--指向数据库连接-->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://192.168.85.111:3306/demo?serverTimezone=UTC"
                        userId="root"
                        password="123456"
        />

        <!--生成对应的实体类
        targetPackage：指定生成java文件的目录
        targetProject：放在那个工程的哪个目录下
        -->
        <javaModelGenerator targetPackage="com.mashibing.bean" targetProject="src/main/java"/>

        <!--SQL映射文件生成器
        targetPackage：指定生成java文件的目录
        targetProject：放在那个工程的哪个目录下
        -->
        <sqlMapGenerator targetPackage="com.mashibing.dao" targetProject="src/main/resources"/>

        <!--dao接口生成器-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="com.mashibing.dao" targetProject="src/main/java"/>

        <!--指定要逆向生成的数据表
        tableName:表名
        domainObjectName:对象名
        -->
        <table tableName="emp" domainObjectName="Emp" enableCountByExample="false" enableDeleteByExample="false" enableUpdateByExample="false" selectByExampleQueryId="false" enableSelectByExample="false"/>
        <table tableName="dept" domainObjectName="Dept" enableCountByExample="false" enableDeleteByExample="false" enableUpdateByExample="false" selectByExampleQueryId="false" enableSelectByExample="false"/>
    </context>
</generatorConfiguration>
```

编写测试类

```java
package com.mashibing;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        File configFile = new File("mbg.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);

    }
}
```

### 3、分页插件 PageHelper（自学，项目中会使用）


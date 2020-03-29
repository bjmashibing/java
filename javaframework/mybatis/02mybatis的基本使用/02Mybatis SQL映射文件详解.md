# 02Mybatis SQL映射文件详解

​		在之前我们学习了mybatis的全局配置文件，下面我们开始学习mybatis的映射文件，在映射文件中，可以编写以下的顶级元素标签：

```
cache – 该命名空间的缓存配置。
cache-ref – 引用其它命名空间的缓存配置。
resultMap – 描述如何从数据库结果集中加载对象，是最复杂也是最强大的元素。
parameterMap – 老式风格的参数映射。此元素已被废弃，并可能在将来被移除！请使用行内参数映射。文档中不会介绍此元素。
sql – 可被其它语句引用的可重用语句块。
insert – 映射插入语句。
update – 映射更新语句。
delete – 映射删除语句。
select – 映射查询语句。
```

​		在每个顶级元素标签中可以添加很多个属性，下面我们开始详细了解下具体的配置。

### 1、insert、update、delete元素

| 属性               | 描述                                                         |
| :----------------- | :----------------------------------------------------------- |
| `id`               | 在命名空间中唯一的标识符，可以被用来引用这条语句。           |
| `parameterType`    | 将会传入这条语句的参数的类全限定名或别名。这个属性是可选的，因为 MyBatis 可以通过类型处理器（TypeHandler）推断出具体传入语句的参数，默认值为未设置（unset）。 |
| `parameterMap`     | 用于引用外部 parameterMap 的属性，目前已被废弃。请使用行内参数映射和 parameterType 属性。 |
| `flushCache`       | 将其设置为 true 后，只要语句被调用，都会导致本地缓存和二级缓存被清空，默认值：（对 insert、update 和 delete 语句）true。 |
| `timeout`          | 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为未设置（unset）（依赖数据库驱动）。 |
| `statementType`    | 可选 STATEMENT，PREPARED 或 CALLABLE。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。 |
| `useGeneratedKeys` | （仅适用于 insert 和 update）这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系型数据库管理系统的自动递增字段），默认值：false。 |
| `keyProperty`      | （仅适用于 insert 和 update）指定能够唯一识别对象的属性，MyBatis 会使用 getGeneratedKeys 的返回值或 insert 语句的 selectKey 子元素设置它的值，默认值：未设置（`unset`）。如果生成列不止一个，可以用逗号分隔多个属性名称。 |
| `keyColumn`        | （仅适用于 insert 和 update）设置生成键值在表中的列名，在某些数据库（像 PostgreSQL）中，当主键列不是表中的第一列的时候，是必须设置的。如果生成列不止一个，可以用逗号分隔多个属性名称。 |
| `databaseId`       | 如果配置了数据库厂商标识（databaseIdProvider），MyBatis 会加载所有不带 databaseId 或匹配当前 databaseId 的语句；如果带和不带的语句都有，则不带的会被忽略。 |

```xml
	<!--如果数据库支持自增可以使用这样的方式-->
    <insert id="insertUser" useGeneratedKeys="true" keyProperty="id">
        insert into user(user_name) values(#{userName})
    </insert>
    <!--如果数据库不支持自增的话，那么可以使用如下的方式进行赋值查询-->
    <insert id="insertUser2" >
        <selectKey order="BEFORE" keyProperty="id" resultType="integer">
            select max(id)+1 from user
        </selectKey>
        insert into user(id,user_name) values(#{id},#{userName})
    </insert>
```

### 2、select元素

##### 1、select的参数传递

```xml
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
    <select id="selectEmpByNoAndName" resultType="com.mashibing.bean.Emp">
        select * from emp where empno=#{empno} and ename=#{ename}
    </select>

    <select id="selectEmpByNoAndName2" resultType="com.mashibing.bean.Emp">
        select * from emp where empno=#{empno} and ename=#{ename}
    </select>
```

##### 2、参数的取值方式

​		在xml文件中编写sql语句的时候有两种取值的方式，分别是#{}和${}，下面来看一下他们之间的区别：

```xml
 <!--
        当使用#{}来获取值的时候会发现打印的sql语句如下：
            select * from emp where empno=? and ename=?
        当使用${}来获取值的时候会发现打印的sql语句如下：
            select * from emp where empno=7369 and ename='SMITH'
        通过刚刚的案例大家已经发现了存在的问题了，
        使用#{}方式进行取值：采用的是参数预编译的方式，参数的位置使用？进行替代，不会出现sql注入的问题
        使用${}方式进行取值：采用的是直接跟sql语句进行拼接的方式

		此处大家需要注意，如果我们的sql语句中的某些值不支持参数预编译，那么就必须要使用${}的方式来取值了
    -->
    <select id="selectEmpByNoAndName" resultType="com.mashibing.bean.Emp">
        select * from #{t} where empno=${empno} and ename=${ename}
    </select>
```

##### 3、处理集合返回结果

EmpDao.xml

```xml
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
```

UserDao.java

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

    Emp selectEmpByNoAndName(@Param("empno") Integer empno, @Param("ename") String ename,@Param("t") String tablename);
    Emp selectEmpByNoAndName2(Map<String,Object> map);

    List<Emp> selectAllEmp();

    Map<String,Object> selectEmpByEmpReturnMap(Integer empno);

    @MapKey("empno")
    Map<Integer,Emp> getAllEmpReturnMap();
}
```

##### 4、自定义结果集---resultMap

Dog.java

```java
package com.mashibing.bean;

public class Dog {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}

```

dog.sql

```sql
/*
Navicat MySQL Data Transfer

Source Server         : node01
Source Server Version : 50729
Source Host           : 192.168.85.111:3306
Source Database       : demo

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-03-24 23:54:22
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `dog`
-- ----------------------------
DROP TABLE IF EXISTS `dog`;
CREATE TABLE `dog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dname` varchar(255) DEFAULT NULL,
  `dage` int(11) DEFAULT NULL,
  `dgender` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dog
-- ----------------------------
INSERT INTO dog VALUES ('1', '大黄', '1', '雄');
INSERT INTO dog VALUES ('2', '二黄', '2', '雌');
INSERT INTO dog VALUES ('3', '三黄', '3', '雄');
```

DogDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Dog;

public interface DogDao {

    public Dog selectDogById(Integer id);
}
```

DogDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mashibing.dao.DogDao">
   <!--
   在使用mybatis进行查询的时候，mybatis默认会帮我们进行结果的封装，但是要求列名跟属性名称一一对应上
   在实际的使用过程中，我们会发现有时候数据库中的列名跟我们类中的属性名并不是一一对应的，此时就需要起别名
   起别名有两种实现方式：
      1、在编写sql语句的时候添加别名
      2、自定义封装结果集
   -->
   <!--根据查询的数据进行结果的封装要使用resultMap属性，表示使用自定义规则-->
   <select id="selectDogById" resultMap="myDog">
      select * from dog where id = #{id}
   </select>

   <!--自定义结果集，将每一个列的数据跟javaBean的对象属性对应起来
   type:表示为哪一个javaBean对象进行对应
   id:唯一标识，方便其他属性标签进行引用
   -->
   <resultMap id="myDog" type="com.mashibing.bean.Dog">
      <!--
      指定主键列的对应规则：
      column：表示表中的主键列
      property:指定javaBean的属性
      -->
      <id column="id" property="id"></id>
      <!--设置其他列的对应关系-->
      <result column="dname" property="name"></result>
      <result column="dage" property="age"></result>
      <result column="dgender" property="gender"></result>
   </resultMap>
   <!--可以在sql语句中写别名-->
 <!--  <select id="selectDogById" resultType="com.mashibing.bean.Dog">
      select id id,dname name,dage age,dgender gender from dog where id = #{id}
   </select>-->

   <!--这种方式是查询不到任何结果的，因为属性名跟列名并不是一一对应的-->
  <!-- <select id="selectDogById" resultType="com.mashibing.bean.Dog">
      select * from dog where id = #{id}
   </select>-->
</mapper>
```

##### 5、联合查询

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
    private Dept dept;

    public Emp() {
    }

    public Emp(Integer empno, String ename) {
        this.empno = empno;
        this.ename = ename;
    }

    public Emp(Integer empno, String ename, String job, Integer mgr, Date hiredate, Double sal, Double common, Dept dept) {
        this.empno = empno;
        this.ename = ename;
        this.job = job;
        this.mgr = mgr;
        this.hiredate = hiredate;
        this.sal = sal;
        this.common = common;
        this.dept = dept;
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

    public Dept getDept() {
        return dept;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
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
                ", dept=" + dept +
                '}';
    }
}

```

Dept.java

```java
package com.mashibing.bean;

public class Dept {
    private Integer deptno;
    private String dname;
    private String loc;

    public Dept() {
    }

    public Dept(Integer deptno, String dname, String loc) {
        this.deptno = deptno;
        this.dname = dname;
        this.loc = loc;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", loc='" + loc + '\'' +
                '}';
    }
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

    <!--再做查询的时候，有时候需要关联其他对象，因此需要使用关联查询
    可以通过下面自定义结果集的方式实现
    -->
    <select id="selectEmpAndDept" resultMap="empDept">
        select * from emp left join dept on emp.deptno = dept.deptno where empno = #{empno};
    </select>
    <resultMap id="empDept" type="com.mashibing.bean.Emp">
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
    </resultMap>
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
</mapper>
```

Test

```java
    @Test
    public void test08() {

        // 获取数据库的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmpDao mapper = sqlSession.getMapper(EmpDao.class);
            Emp emp = mapper.selectEmpAndDept(7369);
            System.out.println(emp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```

##### 6、获取集合元素

Dept.java

```java
package com.mashibing.bean;

import java.util.List;

public class Dept {
    private Integer deptno;
    private String dname;
    private String loc;

    private List<Emp> emps;

    public Dept() {
    }

    public Dept(Integer deptno, String dname, String loc) {
        this.deptno = deptno;
        this.dname = dname;
        this.loc = loc;
    }

    public Integer getDeptno() {
        return deptno;
    }

    public void setDeptno(Integer deptno) {
        this.deptno = deptno;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public List<Emp> getEmps() {
        return emps;
    }

    public void setEmps(List<Emp> emps) {
        this.emps = emps;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "deptno=" + deptno +
                ", dname='" + dname + '\'' +
                ", loc='" + loc + '\'' +
                ", emps=" + emps +
                '}';
    }
}

```

DeptDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Dept;
import com.mashibing.bean.Emp;

import java.util.List;

public interface DeptDao {

    public Dept getDeptAndEmps(Integer deptno);
}

```

DeptDao.xml

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
</mapper>
```

Test

```java
    @Test
    public void test09() {

        // 获取数据库的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            DeptDao mapper = sqlSession.getMapper(DeptDao.class);
            Dept deptAndEmps = mapper.getDeptAndEmps(10);
            System.out.println(deptAndEmps);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```

##### 7、分步查询

​		在上述逻辑的查询中，是由我们自己来完成sql语句的关联查询的，那么，我们能让mybatis帮我们实现自动的关联查询吗?

**关联查询的分步**

DeptDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Dept;
import com.mashibing.bean.Emp;

import java.util.List;

public interface DeptDao {

    public Dept getDeptAndEmps(Integer deptno);

    public Dept getDeptAndEmpsBySimple(Integer deptno);
}

```

EmpDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Emp;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmpDao {

    Emp selectEmpAndDept(Integer empno);
    Emp selectEmpAndDeptBySimple(Integer empno);
}

```

DeptDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mashibing.dao.DeptDao">
    <select id="getDeptAndEmpsBySimple" resultType="com.mashibing.bean.Dept">
        select * from dept where deptno = #{deptno}
    </select>
</mapper>
```

EmpDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mashibing.dao.EmpDao">

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
        <association property="dept" select="com.mashibing.dao.DeptDao.getDeptAndEmpsBySimple" column="deptno">
        </association>
    </resultMap>
</mapper>
```

Test

```java
@Test
    public void test08() {

        // 获取数据库的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmpDao mapper = sqlSession.getMapper(EmpDao.class);
//            Emp emp = mapper.selectEmpAndDept(7369);
            Emp emp = mapper.selectEmpAndDeptBySimple(7369);
            System.out.println(emp);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```

**集合的分步查询**

EmpDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Emp;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface EmpDao {
    Emp selectEmpAndDeptBySimple(Integer empno);
    Emp selectEmpByStep(Integer empno);
}

```

DeptDao.java

```java
package com.mashibing.dao;

import com.mashibing.bean.Dept;
import com.mashibing.bean.Emp;

import java.util.List;

public interface DeptDao {

    public Dept getDeptAndEmps(Integer deptno);

    public Dept getDeptAndEmpsBySimple(Integer deptno);

    public Dept getDeptAndEmpsByStep(Integer deptno);
}

```

EmpDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mashibing.dao.EmpDao">

    <select id="selectEmpByStep" resultType="com.mashibing.bean.Emp">
        select * from emp where deptno = #{deptno}
    </select>
</mapper>
```

DeptDao.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.mashibing.dao.DeptDao">

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
        <collection property="emps" ofType="com.mashibing.bean.Emp" select="com.mashibing.dao.EmpDao.selectEmpByStep" column="deptno">
        </collection>
    </resultMap>
</mapper>
```

Test

```java
    @Test
    public void test09() {

        // 获取数据库的会话
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            DeptDao mapper = sqlSession.getMapper(DeptDao.class);
//            Dept deptAndEmps = mapper.getDeptAndEmps(10);
            Dept deptAndEmpsByStep = mapper.getDeptAndEmpsByStep(10);
            System.out.println(deptAndEmpsByStep);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```

##### 8、延迟查询

​		当我们在进行表关联的时候，有可能在查询结果的时候不需要关联对象的属性值，那么此时可以通过延迟加载来实现功能。在全局配置文件中添加如下属性

mybatis-config.xml

```xml
 <settings>
        <!--开启延时加载-->
        <setting name="lazyLoadingEnabled" value="true"/>
    </settings>
```

如果设置了全局加载，但是希望在某一个sql语句查询的时候不适用延时策略，可以添加如下属性：

```xml
        <association property="dept" select="com.mashibing.dao.DeptDao.getDeptAndEmpsBySimple" column="deptno" fetchType="eager"/>

```

### 3、动态sql

​		动态 SQL 是 MyBatis 的强大特性之一。如果你使用过 JDBC 或其它类似的框架，你应该能理解根据不同条件拼接 SQL 语句有多痛苦，例如拼接时要确保不能忘记添加必要的空格，还要注意去掉列表最后一个列名的逗号。利用动态 SQL，可以彻底摆脱这种痛苦。

​		使用动态 SQL 并非一件易事，但借助可用于任何 SQL 映射语句中的强大的动态 SQL 语言，MyBatis 显著地提升了这一特性的易用性。

​		如果你之前用过 JSTL 或任何基于类 XML 语言的文本处理器，你对动态 SQL 元素可能会感觉似曾相识。在 MyBatis 之前的版本中，需要花时间了解大量的元素。借助功能强大的基于 OGNL 的表达式，MyBatis 3 替换了之前的大部分元素，大大精简了元素种类，现在要学习的元素种类比原来的一半还要少。

- if
- choose (when, otherwise)
- trim (where, set)
- foreach

##### 1、if	

EmpDao.xml

```xml
<select id="getEmpByCondition" resultType="com.mashibing.bean.Emp">
        select * from emp where 
        <if test="empno!=null">
            empno > #{empno} and
        </if>
        <if test="ename!=null">
            ename like #{ename} and
        </if>
        <if test="sal!=null">
            sal > #{sal}
        </if>
    </select>
```

EmpDao.java

```java
public List<Emp> getEmpByCondition(Emp emp);
```

Test.java

```java
 @Test
    public void test10() {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmpDao mapper = sqlSession.getMapper(EmpDao.class);
            Emp emp = new Emp();
            emp.setEmpno(6500);
            emp.setEname("%E%");
            emp.setSal(500.0);
            List<Emp> empByCondition = mapper.getEmpByCondition(emp);
            for (Emp emp1 : empByCondition) {
                System.out.println(emp1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }
```

​		看起来测试是比较正常的，但是大家需要注意的是如果我们传入的参数值有缺失会怎么呢？这个时候拼接的sql语句就会变得有问题，例如不传参数或者丢失最后一个参数，那么语句中就会多一个where或者and的关键字，因此在mybatis中也给出了具体的解决方案：

​		*where* 元素只会在子元素返回任何内容的情况下才插入 “WHERE” 子句。而且，若子句的开头为 “AND” 或 “OR”，*where* 元素也会将它们去除。

```xml
<select id="getEmpByCondition" resultType="com.mashibing.bean.Emp">
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
        </where>
    </select>
```

​		现在看起来没有什么问题了，但是我们的条件添加到了拼接sql语句的前后，那么我们该如何处理呢？

```xml
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
```

##### 2、foreach

​		动态 SQL 的另一个常见使用场景是对集合进行遍历（尤其是在构建 IN 条件语句的时候）。

```xml
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
```

##### 3、choose

​		有时候，我们不想使用所有的条件，而只是想从多个条件中选择一个使用。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

```xml
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
```

##### 4、set

​		用于动态更新语句的类似解决方案叫做 *set*。*set* 元素可以用于动态包含需要更新的列，忽略其它不更新的列。

```xml
<update id="updateEmpByEmpno">
    update emp
    <set>
        <if test="empno!=null">
            empno=#{empno},
        </if>
        <if test="ename!=null">
            ename = #{ename},
        </if>
        <if test="sal!=null">
            sal = #{sal}
        </if>
    </set>
    <where>
        empno = #{empno}
    </where>
</update>
```

### 4、缓存

​		MyBatis 内置了一个强大的事务性查询缓存机制，它可以非常方便地配置和定制。 为了使它更加强大而且易于配置，我们对 MyBatis 3 中的缓存实现进行了许多改进。

​		默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中添加一行：

```
<cache/>
```

当添加上该标签之后，会有如下效果：

- 映射语句文件中的所有 select 语句的结果将会被缓存。
- 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。
- 缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存。
- 缓存不会定时进行刷新（也就是说，没有刷新间隔）。
- 缓存会保存列表或对象（无论查询方法返回哪种）的 1024 个引用。
- 缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。

在进行配置的时候还会分为一级缓存和二级缓存：

一级缓存：线程级别的缓存，是本地缓存，sqlSession级别的缓存

二级缓存：全局范围的缓存，不止局限于当前会话

##### 1、一级缓存的使用

​		一级缓存是sqlsession级别的缓存，默认是存在的。在下面的案例中，大家发现我发送了两个相同的请求，但是sql语句仅仅执行了一次，那么就意味着第一次查询的时候已经将结果进行了缓存。

```java
 @Test
    public void test01() {

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            EmpDao mapper = sqlSession.getMapper(EmpDao.class);
            List<Emp> list = mapper.selectAllEmp();
            for (Emp emp : list) {
                System.out.println(emp);
            }
            System.out.println("--------------------------------");
            List<Emp> list2 = mapper.selectAllEmp();
            for (Emp emp : list2) {
                System.out.println(emp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sqlSession.close();
        }
    }

```

​		在大部分的情况下一级缓存是可以的，但是有几种特殊的情况会造成一级缓存失效：

1、一级缓存是sqlSession级别的缓存，如果在应用程序中只有开启了多个sqlsession，那么会造成缓存失效

```java
@Test
    public void test02(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        List<Emp> list = mapper.selectAllEmp();
        for (Emp emp : list) {
            System.out.println(emp);
        }
        System.out.println("================================");
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        EmpDao mapper2 = sqlSession2.getMapper(EmpDao.class);
        List<Emp> list2 = mapper2.selectAllEmp();
        for (Emp emp : list2) {
            System.out.println(emp);
        }
        sqlSession.close();
        sqlSession2.close();
    }
```

2、在编写查询的sql语句的时候，一定要注意传递的参数，如果参数不一致，那么也不会缓存结果

3、如果在发送过程中发生了数据的修改，那么结果就不会缓存

```java
 @Test
    public void test03(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp empByEmpno = mapper.findEmpByEmpno(1111);
        System.out.println(empByEmpno);
        System.out.println("================================");
        empByEmpno.setEname("zhangsan");
        int i = mapper.updateEmp(empByEmpno);
        System.out.println(i);
        System.out.println("================================");
        Emp empByEmpno1 = mapper.findEmpByEmpno(1111);
        System.out.println(empByEmpno1);
        sqlSession.close();
    }
```

4、在两次查询期间，手动去清空缓存，也会让缓存失效

```java
@Test
    public void test03(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp empByEmpno = mapper.findEmpByEmpno(1111);
        System.out.println(empByEmpno);
        System.out.println("================================");
        System.out.println("手动清空缓存");
        sqlSession.clearCache();
        System.out.println("================================");
        Emp empByEmpno1 = mapper.findEmpByEmpno(1111);
        System.out.println(empByEmpno1);
        sqlSession.close();
    }
```

##### 2、二级缓存

​		二级缓存是全局作用域缓存，默认是不开启的，需要手动进行配置。

​		Mybatis提供二级缓存的接口以及实现，缓存实现的时候要求实体类实现Serializable接口，二级缓存在sqlSession关闭或提交之后才会生效。

###### 1、缓存的使用

​		步骤：

​		1、全局配置文件中添加如下配置：

```xml
 <setting name="cacheEnabled" value="true"/>
```

​		2、需要在使用二级缓存的映射文件出使用<cache/>标签标注

​		3、实体类必须要实现Serializable接口

```java
@Test
    public void test04(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        EmpDao mapper2 = sqlSession2.getMapper(EmpDao.class);
        Emp empByEmpno = mapper.findEmpByEmpno(1111);
        System.out.println(empByEmpno);
        sqlSession.close();

        Emp empByEmpno1 = mapper2.findEmpByEmpno(1111);
        System.out.println(empByEmpno1);
        sqlSession2.close();
    }
```

###### 2、缓存的属性

​		eviction:表示缓存回收策略，默认是LRU

​				LRU：最近最少使用的，移除最长时间不被使用的对象

​				FIFO：先进先出，按照对象进入缓存的顺序来移除

​				SOFT：软引用，移除基于垃圾回收器状态和软引用规则的对象

​				WEAK：弱引用，更积极地移除基于垃圾收集器状态和弱引用规则的对象

​		flushInternal:刷新间隔，单位毫秒

​				默认情况是不设置，也就是没有刷新间隔，缓存仅仅调用语句时刷新

​		size：引用数目，正整数

​				代表缓存最多可以存储多少个对象，太大容易导致内存溢出

​		readonly：只读，true/false

​				true：只读缓存，会给所有调用这返回缓存对象的相同实例，因此这些对象不能被修改。

​				false：读写缓存，会返回缓存对象的拷贝（序列化实现），这种方式比较安全，默认值

```java
	//可以看到会去二级缓存中查找数据，而且二级缓存跟一级缓存中不会同时存在数据，因为二级缓存中的数据是在sqlsession 关闭之后才生效的
	@Test
    public void test05(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp empByEmpno = mapper.findEmpByEmpno(1111);
        System.out.println(empByEmpno);
        sqlSession.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        EmpDao mapper2 = sqlSession2.getMapper(EmpDao.class);
        Emp empByEmpno2 = mapper2.findEmpByEmpno(1111);
        System.out.println(empByEmpno2);
        Emp empByEmpno3 = mapper2.findEmpByEmpno(1111);
        System.out.println(empByEmpno3);
        sqlSession2.close();
    }

	// 缓存查询的顺序是先查询二级缓存再查询一级缓存
 	@Test
    public void test05(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        EmpDao mapper = sqlSession.getMapper(EmpDao.class);
        Emp empByEmpno = mapper.findEmpByEmpno(1111);
        System.out.println(empByEmpno);
        sqlSession.close();

        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        EmpDao mapper2 = sqlSession2.getMapper(EmpDao.class);
        Emp empByEmpno2 = mapper2.findEmpByEmpno(1111);
        System.out.println(empByEmpno2);
        Emp empByEmpno3 = mapper2.findEmpByEmpno(1111);
        System.out.println(empByEmpno3);

        Emp empByEmpno4 = mapper2.findEmpByEmpno(7369);
        System.out.println(empByEmpno4);
        Emp empByEmpno5 = mapper2.findEmpByEmpno(7369);
        System.out.println(empByEmpno5);
        sqlSession2.close();
    }
```

3、二级缓存的作用范围：

​		如果设置了全局的二级缓存配置，那么在使用的时候需要注意，在每一个单独的select语句中，可以设置将查询缓存关闭，以完成特殊的设置

​		1、在setting中设置，是配置二级缓存开启，一级缓存默认一直开启

```xml
 <setting name="cacheEnabled" value="true"/>
```

​		2、select标签的useCache属性：

​				在每一个select的查询中可以设置当前查询是否要使用二级缓存，只对二级缓存有效

​		3、sql标签的flushCache属性

​				增删改操作默认值为true，sql执行之后会清空一级缓存和二级缓存，而查询操作默认是false

​		4、sqlSession.clearCache()

​				只是用来清楚一级缓存

##### 3、整合第三方缓存

​		在某些情况下我们也可以自定义实现缓存，或为其他第三方缓存方案创建适配器，来完全覆盖缓存行为。

​		1、导入对应的maven依赖

```xml
 <!-- https://mvnrepository.com/artifact/org.ehcache/ehcache -->
        <dependency>
            <groupId>org.ehcache</groupId>
            <artifactId>ehcache</artifactId>
            <version>3.8.1</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mybatis.caches/mybatis-ehcache -->
        <dependency>
            <groupId>org.mybatis.caches</groupId>
            <artifactId>mybatis-ehcache</artifactId>
            <version>1.2.0</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>2.0.0-alpha1</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12 -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>2.0.0-alpha1</version>
            <scope>test</scope>
        </dependency>
```

​		2、导入ehcache配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd">
 <!-- 磁盘保存路径 -->
 <diskStore path="D:\ehcache" />
 
 <defaultCache 
   maxElementsInMemory="1" 
   maxElementsOnDisk="10000000"
   eternal="false" 
   overflowToDisk="true" 
   timeToIdleSeconds="120"
   timeToLiveSeconds="120" 
   diskExpiryThreadIntervalSeconds="120"
   memoryStoreEvictionPolicy="LRU">
 </defaultCache>
</ehcache>
 
<!-- 
属性说明：
l diskStore：指定数据在磁盘中的存储位置。
l defaultCache：当借助CacheManager.add("demoCache")创建Cache时，EhCache便会采用<defalutCache/>指定的的管理策略
 
以下属性是必须的：
l maxElementsInMemory - 在内存中缓存的element的最大数目 
l maxElementsOnDisk - 在磁盘上缓存的element的最大数目，若是0表示无穷大
l eternal - 设定缓存的elements是否永远不过期。如果为true，则缓存的数据始终有效，如果为false那么还要根据timeToIdleSeconds，timeToLiveSeconds判断
l overflowToDisk - 设定当内存缓存溢出的时候是否将过期的element缓存到磁盘上
 
以下属性是可选的：
l timeToIdleSeconds - 当缓存在EhCache中的数据前后两次访问的时间超过timeToIdleSeconds的属性取值时，这些数据便会删除，默认值是0,也就是可闲置时间无穷大
l timeToLiveSeconds - 缓存element的有效生命期，默认是0.,也就是element存活时间无穷大
 diskSpoolBufferSizeMB 这个参数设置DiskStore(磁盘缓存)的缓存区大小.默认是30MB.每个Cache都应该有自己的一个缓冲区.
l diskPersistent - 在VM重启的时候是否启用磁盘保存EhCache中的数据，默认是false。
l diskExpiryThreadIntervalSeconds - 磁盘缓存的清理线程运行间隔，默认是120秒。每个120s，相应的线程会进行一次EhCache中数据的清理工作
l memoryStoreEvictionPolicy - 当内存缓存达到最大，有新的element加入的时候， 移除缓存中element的策略。默认是LRU（最近最少使用），可选的有LFU（最不常使用）和FIFO（先进先出）
 -->
```

​		3、在mapper文件中添加自定义缓存

```xml
    <cache type="org.mybatis.caches.ehcache.EhcacheCache"></cache>
```


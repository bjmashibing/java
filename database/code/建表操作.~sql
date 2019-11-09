/*

CREATE TABLE [schema.]table
  (column datatype [DEFAULT expr] , …
	);

*/

--设计要求：建立一张用来存储学生信息的表，表中的字段包含了学生的学号、姓名、年龄、入学日期、年级、班级、email等信息，
--并且为grade指定了默认值为1，如果在插入数据时不指定grade得值，就代表是一年级的学生

create table student
(
stu_id number(10),
name varchar2(20),
age number(3),
hiredate date,
grade varchar2(10) default 1,
classes varchar2(10),
email varchar2(50)
);
insert into student values(20191109,'zhangsan',22,to_date('2019-11-09','YYYY-MM-DD'),'2','1','123@qq.com');
insert into student(stu_id,name,age,hiredate,classes,email) values(20191109,'zhangsan',22,to_date('2019-11-09','YYYY-MM-DD'),'1','123@qq.com');

select * from student;
--正规的表结构设计需要使用第三方工具 powerdesigner
--再添加表的列的时候，不能允许设置成not null
alter table student add address varchar2(100);
alter table student drop column address;
alter table student modify(email varchar2(100));
--重新命名表
rename student to stu;
--删除表
/*
在删除表的时候，经常会遇到多个表关联的情况，多个表关联的时候不能随意删除，需要使用级联删除
cascade:如果A,B,A中的某一个字段跟B表中的某一个字段做关联，那么再删除表A的时候，需要先将表B删除
set null:再删除的时候，把表的关联字段设置成空
*/
 drop table stu;
 
 --创建表的时候可以给表中的数据添加数据校验规则，这些规则称之为约束
 /*
 约束分为五大类
 not null: 非空约束，插入数据的时候某些列不允许为空
 unique key:唯一键约束，可以限定某一个列的值是唯一的，唯一键的列一般被用作索引列。
 primary key:主键：非空且唯一，任何一张表一般情况下最好有主键，用来唯一的标识一行记录，
 foreign key:外键，当多个表之间有关联关系（一个表的某个列的值依赖与另一张表的某个值）的时候，需要使用外键
 check约束:可以根据用户自己的需求去限定某些列的值
 */
 --个人建议：再创建表的时候直接将各个表的约束条件添加好，如果包含外键约束的话，最好先把外键关联表的数据优先插入
 
 insert into emp(empno,ename,deptno) values(9999,'hehe',50);
 
 create table student
(
stu_id number(10) primary key,
name varchar2(20) not null,
age number(3) check(age>0 and age<126),
hiredate date,
grade varchar2(10) default 1,
classes varchar2(10),
email varchar2(50) unique,
deptno number(2)
);
insert into student(stu_id,name,age,hiredate,classes,email,deptno) values(20191109,'zhansgan',111,to_date('2019-11-09','YYYY-MM-DD'),'1','12443@qq.com',10);

alter table student add constraint fk_0001 foreign key(deptno) references dept(deptno);

--函数的测试
/*
组函数又称为聚合函数
  输入多个值，最终只会返回一个值
  组函数仅可用于选择列表或查询的having子句
单行函数
  输入一个值，输出一个值


*/

--查询所有员工的薪水总和
select sum(sal) from emp;
--查看表中有多少条记录
select deptno,count(*) from emp group by deptno where count(*) >3;

--字符函数
--concat：表示字符串的连接  等同于||
select concat('my name is ', ename) from emp;
--将字符串的首字母大写
select initcap(ename) from emp;
--将字符串全部转换为大写
select upper(ename) from emp;
--将字符串全部转换为小写
select lower(ename) from emp;
--填充字符串
select lpad(ename,10,'*') from emp;
select rpad(ename,10,'*') from emp;
--去除空格
select trim(ename) from emp;
select ltrim(ename) from emp;
select rtrim(ename) from emp;
--查找指定字符串的位置
select instr('ABABCDEF','A') from emp;
--查看字符串的长度
select length(ename) from emp;
--截取字符串的操作
select substr(ename,0,2) from emp;
--替换操作
select replace('ababefg','ab','hehe') from emp;

--数值函数
--给小数进行四舍五入操作，可以指定小数部分的位数
select round(123.123,2) from dual;
select round(123.128,2) from dual;
select round(-123.128,2) from dual;

--截断数据,按照位数去进行截取，但是不会进行四舍五入的操作
select trunc(123.128,2) from dual;
--取模操作
select mod(10,4) from dual;
select mod(-10,4) from dual;
--向上取整
select ceil(12.12) from dual;
--向下取整
select floor(13.99) from dual;
--取绝对值
select abs(-100) from dual;
--获取正负值
select sign(-100) from dual;
--x的y次幂
select power(2,3) from dual;

--日期函数
select sysdate from dual;
select current_date from dual;
--add_months,添加指定的月份
select add_months(hiredate,2),hiredate from emp;
--返回输入日期所在月份的最后一天
select last_day(sysdate) from dual;
--两个日期相间隔的月份
select months_between(sysdate,hiredate) from emp;
--返回四舍五入的第一天
select sysdate 当时日期,
round(sysdate) 最近0点日期,
round(sysdate,'day') 最近星期日,
round(sysdate,'month') 最近月初,
round(sysdate,'q') 最近季初日期, 
round(sysdate,'year') 最近年初日期 from dual;
--返回下周的星期几
select next_day(sysdate,'星期一') from dual;
--提取日期中的时间
select 
extract(hour from timestamp '2001-2-16 2:38:40 ' ) 小时,
extract(minute from timestamp '2001-2-16 2:38:40 ' ) 分钟,
extract(second from timestamp '2001-2-16 2:38:40 ' ) 秒,
extract(DAY from timestamp '2001-2-16 2:38:40 ' ) 日,
extract(MONTH from timestamp '2001-2-16 2:38:40 ' ) 月,
extract(YEAR from timestamp '2001-2-16 2:38:40 ' ) 年
 from dual;
--返回日期的时间戳
select localtimestamp from dual;
select current_date from dual;
select current_timestamp from dual;
--给指定的时间单位增加数值
select
trunc(sysdate)+(interval '1' second), --加1秒(1/24/60/60)
trunc(sysdate)+(interval '1' minute), --加1分钟(1/24/60)
trunc(sysdate)+(interval '1' hour), --加1小时(1/24)
trunc(sysdate)+(INTERVAL '1' DAY),  --加1天(1)
trunc(sysdate)+(INTERVAL '1' MONTH), --加1月
trunc(sysdate)+(INTERVAL '1' YEAR), --加1年
trunc(sysdate)+(interval '01:02:03' hour to second), --加指定小时到秒
trunc(sysdate)+(interval '01:02' minute to second), --加指定分钟到秒
trunc(sysdate)+(interval '01:02' hour to minute), --加指定小时到分钟
trunc(sysdate)+(interval '2 01:02' day to minute) --加指定天数到分钟
from dual;


/*

转换函数
     在oracle中存在数值的隐式转换和显式转换
     隐式转换指的是字符串可以转换为数值或者日期
显式转换：
    to_char: 当由数值或者日期转成字符串的时候，必须要规定格式
*/
select '999'+10 from dual;
--date ：to_char
select to_char(sysdate,'YYYY-MI-SS HH24:MI:SS') from dual;
-- number : to_char
select to_char(123.456789,'9999') from dual;
select to_char(123.456789,'0000.00') from dual;
select to_char(123.456789,'$0000.00') from dual;
select to_char(123.456789,'L0000.00') from dual;
select to_char(123456789,'999,999,999,999') from dual;
--to_date:转换之后都是固定的格式
select to_date('2019/10/10 10:10:10','YYYY-MM-DD HH24:MI:SS') from dual;
--to_number:转成数字
select to_number('123,456,789','999,999,999') from dual;


--显示没有上级管理的公司首脑
select ename,nvl(to_char(mgr),'boss') from emp where mgr is null;
--显示员工雇佣期满6个月后下一个星期五的日期
select hiredate,next_day(add_months(hiredate,6),'星期五') from emp;

--条件函数
--decode,case when

--给不同部门的人员涨薪，10部门涨10%，20部门涨20%，30部门涨30%
select ename,sal,deptno,decode(deptno,10,sal*1.1,20,sal*1.2,30,sal*1.3) from emp;
select ename,
       sal,
       deptno,
       case deptno
         when 10 then
          sal * 1.1
         when 20 then
          sal * 1.2
         when 30 then
          sal * 1.3
       end
  from emp;
------------------------------

create table test(
   id number(10) primary key,
   type number(10) ,
   t_id number(10),
   value varchar2(5)
);
insert into test values(100,1,1,'张三');
insert into test values(200,2,1,'男');
insert into test values(300,3,1,'50');

insert into test values(101,1,2,'刘二');
insert into test values(201,2,2,'男');
insert into test values(301,3,2,'30');

insert into test values(102,1,3,'刘三');
insert into test values(202,2,3,'女');
insert into test values(302,3,3,'10');

select * from test;
/*
需求
将表的显示转换为
姓名      性别     年龄
--------- -------- ----
张三       男        50
*/
select decode(type, 1, value) 姓名,
       decode(type, 2, value) 性别,
       decode(type, 3, value) 年龄
  from test;
select min(decode(type, 1, value)) 姓名,
       min(decode(type, 2, value)) 性别,
       min(decode(type, 3, value)) 年龄
  from test group by t_id; 

/*
组函数,一般情况下，组函数都要和groupby组合使用
组函数一般用于选择列表或者having条件判断
常用的组函数有5个
avg()  平均值,只用于数值类型的数据
min()  最小值，适用于任何类型
max()  最大值，适用于任何类型
count() 记录数,处理的时候会跳过空值而处理非空值
    count一般用来获取表中的记录条数，获取条数的时候可以使用*或者某一个具体的列
       甚至可以使用纯数字来代替，但是从运行效率的角度考虑，建议使用数字或者某一个具体的列
       而不要使用*
       
sum()   求和，只适合数值类型的数据
*/
select avg(sal) from emp;
select min(sal) from emp;
select max(sal) from emp;
select count(sal) from emp;
select sum(sal) from emp;
--group by,按照某些相同的值去进行分组操作
--group进行分组操作的时候，可以指定一个列或者多个列，但是当使用了groupby 之后，
--选择列表中只能包含组函数的值或者group by 的普通字段
--求每个部门的平均薪水
select avg(sal) from emp group by deptno;
--求平均新书大于2000的部门
select avg(sal),deptno from emp where sal is not null group by deptno having avg(sal) >2000 order by avg(sal);

select count(10000) from emp;
--部门下雇员的工资>2000 人数
select deptno,count(1) from emp where sal>2000 group by deptno
--部门薪水最高
select deptno,max(sal) from emp group by deptno;
--部门里面 工龄最小和最大的人找出来,知道姓名
select deptno,min(hiredate),max(hiredate) from emp group by deptno;
select ename, deptno
  from emp e
 where hiredate in (select min(hiredate) from emp group by deptno)
    or hiredate in (select max(hiredate) from emp group by deptno)
select * from emp

select mm2.deptno, e1.ename, e1.hiredate
  from emp e1,
       (select min(e.hiredate) mind, max(e.hiredate) maxd, e.deptno
          from emp e
         group by e.deptno) mm2
 where (e1.hiredate = mm2.mind
    or e1.hiredate = mm2.maxd)
    and e1.deptno = mm2.deptno;

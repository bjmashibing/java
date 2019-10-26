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


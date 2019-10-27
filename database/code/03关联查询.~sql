--关联查询
/*
select t1.c1,t2.c2 from t1,t2 where t1.c3 = t2.c4
在进行连接的时候，可以使用等值连接，可以使用非等值连接

*/
--查询雇员的名称和部门的名称
select ename,dname from emp,dept where emp.deptno = dept.deptno;
--查询雇员名称以及自己的薪水等级
select e.ename,e.sal,sg.grade from emp e,salgrade sg where e.sal between sg.losal and sg.hisal;

--等值连接，两个表中包含相同的列名
--非等值连接，两个表中没有相同的列名，但是某一个列在另一张表的列的范围之中
--外连接
select * from emp;
select * from dept;
--需要将雇员表中的所有数据都进行显示,利用等值连接的话只会把关联到的数据显示，
--没有关联到的数据不会显示，此时需要外连接
--分类：左外连接（把左表的全部数据显示）和右外连接（把右表的全部数据显示）
select * from emp e,dept d where e.deptno = d.deptno；--等值连接
select * from emp e,dept d where e.deptno = d.deptno(+);--左外连接
select * from emp e,dept d where e.deptno(+) = d.deptno;--右外连接
--自连接,将一张表当成不同的表来看待，自己关联自己
--将雇员和他经理的名称查出来
select e.ename,m.ename from emp e,emp m where e.mgr = m.empno;
--笛卡尔积,当关联多张表，但是不指定连接条件的时候，会进行笛卡尔积，
--关联后的总记录条数为M*n，一般不要使用
select * from emp e,dept d;

--92的表连接语法有什么问题？？？？
--在92语法中，多张表的连接条件会方法where子句中，同时where需要对表进行条件过滤
--因此，相当于将过滤条件和连接条件揉到一起，太乱了，因此出现了99语法


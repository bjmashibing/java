--给表添加注释
comment on table emp is '雇员表';
--给列添加注释
comment on column emp.ename is '雇员姓名';

/*sql语句学习

SELECT [DISTINCT] {*,column alias,..}
FROM table alias
Where 条件表达式

*/

--查询雇员表中部门编号是10的员工
select empno,ename,job from emp where deptno = 10;
--dinstinct 去除重复数据
select distinct deptno from emp;
--去重也可以针对多个字段，多个字段值只要有一个不匹配就算是不同的记录
select distinct deptno,sal from emp;


--在查询的过程中可以给列添加别名，同时也可以给表添加别名
select e.empno 雇员编号,e.ename 雇员名称,e.job 雇员工作 from emp e where e.deptno = 10;
--给列起别名可以加as，也可以不加，看你心情
select e.empno as 雇员编号,e.ename  as 雇员名称,e.job as 雇员工作 from emp e where e.deptno = 10;
--给列起别名，如果别名中包含空格，那么需要将别名整体用“”包含起来
select e.empno as "雇员 编号",e.ename  as "雇员 名称",e.job as "雇员 工作" from emp e where e.deptno = 10;
--查询表中的所有字段,可以使用*,但是在项目中千万不要随便使用*,容易被打死
select * from emp;


/*
＝，！＝,<>，<,>,<=,>=,any,some,all
is null,is not null
between x and y
in（list），not in（list）
exists（sub－query）
like  _ ,%,escape ‘\‘   _\% escape ‘\’

*/
-- =
select * from emp where deptno = 20;
--!=
select * from emp where deptno !=20;
--<> 不等于
select * from emp where deptno <> 20;
--<,
select sal from emp where sal <1500;
-->,
select sal from emp where sal >1500;
--<=,
select sal from emp where sal <=1500;
-->=,
select sal from emp where sal >=1500;
--any,取其中任意一个
select sal from emp where sal > any(1000,1500,3000);
--some,some跟any是同一个效果，只要大于其中某一个值都会成立
select sal from emp where sal > some(1000,1500,3000);
--all，大于所有的值才会成立
select sal from emp where sal > all(1000,1500,3000);
--is null,在sql的语法中，null表示一个特殊的含义，null != null,不能使用=，！=判断，需要使用is ,is not
select * from emp where comm is null;
--,is not null
select * from emp where comm is not null;
select * from emp where null is null;
--between x and y,包含x和y的值
select * from emp where sal between 1500 and 3000;
select * from emp where sal >=1500 and sal <=3000;
--需要进行某些值的等值判断的时候可以使用in和not in
--in（list），
select * from emp where deptno in(10,20);
--可是用and 和or这样的关键字，and相当于是与操作，or相当于是或操作
--and和or可能出现在同一个sql语句中，此时需要注意and和or的优先级
--and 的优先级要高于or，所以一定要将or的相关操作用（）括起来，提高优先级
select * from emp where deptno = 10 or deptno = 20;
--not in（list）
select * from emp where deptno not in(10,20);
select * from emp where deptno != 10 and deptno !=20;
/*exists（sub－query）,当exists中的子查询语句能查到对应结果的时候，
意味着条件满足
相当于双层for循环
--现在要查询部门编号为10和20的员工，要求使用exists实现
*/
select * from emp where deptno = 10 or deptno = 20;
--通过外层循环来规范内层循环
select *
  from emp e
 where exists (select deptno
          from dept d
         where (d.deptno = 10 or d.deptno = 20)
           and e.deptno = d.deptno)
/*
模糊查询：
like  _ ,%,escape ‘\‘   _\% escape ‘\’

在like的语句中，需要使用占位符或者通配符
_,某个字符或者数字仅出现一次
%，任意字符出现任意次数
escape,使用转义字符,可以自己规定转义字符

使用like的时候要慎重，因为like的效率比较低
使用like可以参考使用索引，但是要求不能以%开头
涉及到大文本的检索的时候，可以使用某些框架 luence，solr，elastic search
*/
--查询名字以S开头的用户
select * from emp where ename like('S%')
--查询名字以S开头且倒数第二个字符为T的用户
select * from emp where ename like('S%T_');
select * from emp where ename like('S%T%');
--查询名字中带%的用户
select * from emp where ename like('%\%%') escape('\')
/*

order by进行排序操作
默认情况下完成的是升序的操作，
asc:是默认的排序方式，表示升序
desc：降序的排序方式

排序是按照自然顺序进行排序的
如果是数值，那么按照从大到小
如果是字符串，那么按照字典序排序

在进行排序的时候可以指定多个字段，而且多个字段可以使用不同的排序方式

每次在执行order by的时候相当于是做了全排序，思考全排序的效率
会比较耗费系统的资源，因此选择在业务不太繁忙的时候进行
*/
select * from emp order by sal;
select * from emp order by sal desc;
select * from emp order by ename;
select * from emp order by sal desc,ename asc;
--使用计算字段
--字符串连接符
select 'my name is '||ename name from emp;
select concat('my name is ',ename) from emp;
--计算所有员工的年薪
select ename,(e.sal+e.comm)*12 from emp e;
--null是比较特殊的存在，null做任何运算都还是为null，因此要将空进行转换
--引入函数nvl，nvl(arg1,arg2),如果arg1是空，那么返回arg2，如果不是空，则返回原来的值
select ename,(e.sal+nvl(e.comm,0))*12  from emp e;
--dual是oracle数据库中的一张虚拟表，没有实际的数据，可以用来做测试
select 100+null from dual;
--A
select * from emp where deptno =30;
--B
select * from emp where sal >1000; 
--并集，将两个集合中的所有数据都进行显示，但是不包含重复的数据
select * from emp where deptno =30 union
select * from emp where sal >1000;
--全集，将两个集合的数据全部显示，不会完成去重的操作
select * from emp where deptno =30 union all
select * from emp where sal >1000;
--交集，两个集合中交叉的数据集，只显示一次
select * from emp where deptno =30 intersect 
select * from emp where sal >1000;
--差集,包含在A集合而不包含在B集合中的数据，跟A和B的集合顺序相关
select * from emp where deptno =30 minus 
select * from emp where sal >1000;







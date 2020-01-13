--在oracle中如果需要完成一个列的自增操作，必须要使用序列
/*
create sequence seq_name
  increment by n  每次增长几
  start with n    从哪个值开始增长
  maxvalue n|nomaxvalue 10^27 or -1  最大值
  minvalue n|no minvalue  最小值
	cycle|nocycle           是否有循环
	cache n|nocache          是否有缓存

*/
create sequence my_sequence
increment by 2
start with 1

--如何使用？
--注意，如果创建好序列之后，没有经过任何的使用，那么不能获取当前的值，必须要先执行nextval之后才能获取当前值
--dual是oracle中提供的一张虚拟表，不表示任何意义，在测试的时候可以随意使用
--查看当前序列的值
select my_sequence.currval from dual;
--获取序列的下一个值
select my_sequence.nextval from dual;

insert into emp(empno,ename) values(my_sequence.nextval,'hehe');
select * from emp;

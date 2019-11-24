--索引：加快数据的检索
--创建索引
create index i_ename on emp(ename);
--删除索引
drop index i_ename;
select * from emp where ename = 'SMITH';

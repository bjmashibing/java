/*

CREATE TABLE [schema.]table
  (column datatype [DEFAULT expr] , ��
	);

*/

--���Ҫ�󣺽���һ�������洢ѧ����Ϣ�ı����е��ֶΰ�����ѧ����ѧ�š����������䡢��ѧ���ڡ��꼶���༶��email����Ϣ��
--����Ϊgradeָ����Ĭ��ֵΪ1������ڲ�������ʱ��ָ��grade��ֵ���ʹ�����һ�꼶��ѧ��

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
--����ı�ṹ�����Ҫʹ�õ��������� powerdesigner
--����ӱ���е�ʱ�򣬲����������ó�not null
alter table student add address varchar2(100);
alter table student drop column address;
alter table student modify(email varchar2(100));
--����������
rename student to stu;
--ɾ����
/*
��ɾ�����ʱ�򣬾����������������������������������ʱ��������ɾ������Ҫʹ�ü���ɾ��
cascade:���A,B,A�е�ĳһ���ֶθ�B���е�ĳһ���ֶ�����������ô��ɾ����A��ʱ����Ҫ�Ƚ���Bɾ��
set null:��ɾ����ʱ�򣬰ѱ�Ĺ����ֶ����óɿ�
*/
 drop table stu;
 
 --�������ʱ����Ը����е������������У�������Щ�����֮ΪԼ��
 /*
 Լ����Ϊ�����
 not null: �ǿ�Լ�����������ݵ�ʱ��ĳЩ�в�����Ϊ��
 unique key:Ψһ��Լ���������޶�ĳһ���е�ֵ��Ψһ�ģ�Ψһ������һ�㱻���������С�
 primary key:�������ǿ���Ψһ���κ�һ�ű�һ����������������������Ψһ�ı�ʶһ�м�¼��
 foreign key:������������֮���й�����ϵ��һ�����ĳ���е�ֵ��������һ�ű��ĳ��ֵ����ʱ����Ҫʹ�����
 checkԼ��:���Ը����û��Լ�������ȥ�޶�ĳЩ�е�ֵ
 */
 --���˽��飺�ٴ������ʱ��ֱ�ӽ��������Լ��������Ӻã�����������Լ���Ļ�������Ȱ������������������Ȳ���
 
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

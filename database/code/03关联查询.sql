--������ѯ
/*
select t1.c1,t2.c2 from t1,t2 where t1.c3 = t2.c4
�ڽ������ӵ�ʱ�򣬿���ʹ�õ�ֵ���ӣ�����ʹ�÷ǵ�ֵ����

*/
--��ѯ��Ա�����ƺͲ��ŵ�����
select ename,dname from emp,dept where emp.deptno = dept.deptno;
--��ѯ��Ա�����Լ��Լ���нˮ�ȼ�
select e.ename,e.sal,sg.grade from emp e,salgrade sg where e.sal between sg.losal and sg.hisal;

--��ֵ���ӣ��������а�����ͬ������
--�ǵ�ֵ���ӣ���������û����ͬ������������ĳһ��������һ�ű���еķ�Χ֮��
--������
select * from emp;
select * from dept;
--��Ҫ����Ա���е��������ݶ�������ʾ,���õ�ֵ���ӵĻ�ֻ��ѹ�������������ʾ��
--û�й����������ݲ�����ʾ����ʱ��Ҫ������
--���ࣺ�������ӣ�������ȫ��������ʾ�����������ӣ����ұ��ȫ��������ʾ��
select * from emp e,dept d where e.deptno = d.deptno��--��ֵ����
select * from emp e,dept d where e.deptno = d.deptno(+);--��������
select * from emp e,dept d where e.deptno(+) = d.deptno;--��������
--������,��һ�ű��ɲ�ͬ�ı����������Լ������Լ�
--����Ա������������Ʋ����
select e.ename,m.ename from emp e,emp m where e.mgr = m.empno;
--�ѿ�����,���������ű����ǲ�ָ������������ʱ�򣬻���еѿ�������
--��������ܼ�¼����ΪM*n��һ�㲻Ҫʹ��
select * from emp e,dept d;

--92�ı������﷨��ʲô���⣿������
--��92�﷨�У����ű�����������᷽��where�Ӿ��У�ͬʱwhere��Ҫ�Ա������������
--��ˣ��൱�ڽ��������������������ൽһ��̫���ˣ���˳�����99�﷨


--99�﷨
/*
CROSS��JOIN
NATURAL JOIN
USING�Ӿ�
ON�Ӿ�
LEFT OUTER JOIN
RIGHT OUTER JOIN
FULL OUTER JOIN
Inner join

*/
--cross join ��ͬ��92�﷨�еĵѿ�����
select * from emp cross join dept;
--natural join  �൱���ǵ�ֵ���ӣ�����ע�⣬����Ҫд����������������ű����ҵ���ͬ����������
--�����ű��в�������ͬ��������ʱ�򣬻���еѿ���������,��Ȼ���Ӹ�92�﷨��������û���κι�ϵ
select * from emp e natural join dept d ;
select * from emp e natural join salgrade sg;
--on�Ӿ䣬����������������������
--����������� �൱��92�﷨�еĵ�ֵ����
select * from emp e join dept d on e.deptno = d.deptno;
--�൱��92�﷨�еķǵ�ֵ���ӣ�
select * from emp e join salgrade sg on e.sal between sg.losal and sg.hisal;
--left outer join ,�������е�ȫ������������ʾ���ұ�û�ж�Ӧ������ֱ����ʾ�ռ���
select * from emp e left outer join dept d on e.deptno = d.deptno;
select * from emp e,dept d where e.deptno = d.deptno(+);
--right outer join ,����ұ��е�ȫ������������ʾ�������û�ж�Ӧ�ļ�¼�Ļ���ʾ�ռ���
select * from emp e right outer join dept  d on e.deptno = d.deptno;
select * from emp e,dept d where e.deptno(+) = d.deptno;
--full outer join ,�൱���������Ӻ��������ӵĺϼ�
select * from emp e full outer join dept d on e.deptno = d.deptno;
--inner outer join�����ű�����Ӳ�ѯ��ֻ���ѯ����ƥ���¼������
select * from emp e inner join dept d on e.deptno = d.deptno;
select * from emp e join dept d on e.deptno = d.deptno;
--using,���˿���ʹ��on��ʾ��������֮�⣬Ҳ����ʹ��using��Ϊ��������,��ʱ�����������в��ٹ������κ�һ�ű�
select deptno from emp e join dept d using(deptno);
select e.deptno,d.deptno from emp e join dept d on e.deptno = d.deptno;
--�ܽ�:�����﷨��SQL���û���κ����ƣ��ٹ�˾�п�������ʹ�ã����ǽ���ʹ��99�﷨����Ҫʹ��92�﷨��SQL�Ե��������

--������Ա���֡����ڵ�λ��нˮ�ȼ�
select e.ename, d.loc, sg.grade
  from emp e
  join dept d
    on e.deptno = d.deptno
  join salgrade sg
    on e.sal between sg.losal and sg.hisal;


/*
�Ӳ�ѯ��
    Ƕ��������sql����е�����sql��䣬���Գ�֮Ϊ�Ӳ�ѯ
���ࣺ
    �����Ӳ�ѯ
    �����Ӳ�ѯ

*/
--����Щ�˵�нˮ����������Ա��ƽ��нˮ֮�ϵ�
--1������ƽ��нˮ
select avg(e.sal) from emp e;
--2���������˵�нˮ��ƽ��нˮ�Ƚ�
select * from emp e where e.sal > (select avg(e.sal) from emp e); 
--����Ҫ���ڹ�Ա������Щ���Ǿ�����
--1����ѯ���еľ����˱��
select distinct e.mgr from emp e;
--2���ٹ�Ա���й�����Щ��ż���
select * from emp e where e.empno in (select distinct e.mgr from emp e);
--ÿ������ƽ��нˮ�ĵȼ�
--1����������ŵ�ƽ��нˮ
select e.deptno,avg(e.sal) from emp e group by e.deptno; 
--2����нˮ�ǼǱ������������ƽ��нˮ�ĵȼ�
select t.deptno, sg.grade
  from salgrade sg
  join (select e.deptno, avg(e.sal) vsal from emp e group by e.deptno) t
    on t.vsal between sg.losal and sg.hisal;


--1����ƽ��нˮ��ߵĲ��ŵĲ��ű��
--���ŵ�ƽ��нˮ
select e.deptno,avg(e.sal) from emp e group by e.deptno;
--��ƽ��нˮ��ߵĲ���
select max(t.vsal) from (select e.deptno,avg(e.sal) vsal from emp e group by e.deptno) t
--���ű��
select t.deptno
  from (select e.deptno, avg(e.sal) vsal from emp e group by e.deptno) t
 where t.vsal =
       (select max(t.vsal)
          from (select e.deptno, avg(e.sal) vsal from emp e group by e.deptno) t);
--2������ƽ��нˮ�ĵȼ�
--3������ƽ����нˮ�ȼ�
--����ÿ���˵�нˮ�ȼ�
select e.deptno, sg.grade
  from emp e
  join salgrade sg
    on e.sal between sg.losal and sg.hisal;
--���ղ�����ƽ���ȼ�
select t.deptno, avg(t.grade)
  from (select e.deptno, sg.grade
          from emp e
          join salgrade sg
            on e.sal between sg.losal and sg.hisal) t
 group by t.deptno;
--���������limit��mysql����������������ģ�����oracle�в���
--��oracle�У������Ҫʹ����������ͷ�ҳ�Ĺ��ܵĻ�������Ҫʹ��rownum��
--����rownum����ֱ��ʹ�ã���ҪǶ��ʹ��
--4����нˮ��ߵ�ǰ5����Ա
select *
  from (select * from emp e order by e.sal desc) t1
 where rownum <= 5
  
 select * from emp e  where rownum <=5 order by e.sal desc
--5����нˮ��ߵĵ�6��10����Ա
select t1.*,rownum
  from (select * from emp e order by e.sal desc) t1
 where rownum <= 10
--ʹ��rownum��ʱ�����Ҫ��������Ƕ�ף���ʱ���ܽ�rownum��Ϊ���е�һ���У�Ȼ���ٽ����������
 select *
   from (select t1.*, rownum rn
           from (select * from emp e order by e.sal desc) t1
          where rownum <= 10) t
  where t.rn >  5
    and t.rn <= 10;


select *
  from (select t1.*, rownum rn
          from (select * from emp e order by e.sal desc) t1) t
 where t.rn > 5
   and t.rn <= 10;
        


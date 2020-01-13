create table tmp(rq varchar2(10),shengfu varchar2(5));

insert into tmp values('2005-05-09','ʤ');
insert into tmp values('2005-05-09','ʤ');
insert into tmp values('2005-05-09','��');
insert into tmp values('2005-05-09','��');
insert into tmp values('2005-05-10','ʤ');
insert into tmp values('2005-05-10','��');
insert into tmp values('2005-05-10','��');

/*
          ʤ ��
2005-05-09 2 2
2005-05-10 1 2

*/

select rq,decode(shengfu,'ʤ',1),decode(shengfu,'��',2) from tmp;

select rq,
       count(decode(shengfu, 'ʤ', 1)) ʤ,
       count(decode(shengfu, '��', 2)) ��
  from tmp
 group by rq;


create table STUDENT_SCORE
(
  name    VARCHAR2(20),
  subject VARCHAR2(20),
  score   NUMBER(4,1)
);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', '����', 78.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', '��ѧ', 88.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', 'Ӣ��', 98.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', '����', 89.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', '��ѧ', 76.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', 'Ӣ��', 90.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', '����', 99.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', '��ѧ', 66.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('����', 'Ӣ��', 91.0);


/*
����   ����  ��ѧ  Ӣ��
����    89    56    89
*/
--����ʹ��4�з�ʽ��д��
--decode
select ss.name,
       max(decode(ss.subject, '����', ss.score)) ����,
       max(decode(ss.subject, '��ѧ', ss.score)) ��ѧ,
       max(decode(ss.subject, 'Ӣ��', ss.score)) Ӣ��
  from student_score ss group by ss.name
--case when
select ss.name,
       max(case ss.subject
             when '����' then
              ss.score
           end) ����,
       max(case ss.subject
             when '��ѧ' then
              ss.score
           end) ��ѧ,
       max(case ss.subject
             when 'Ӣ��' then
              ss.score
           end) Ӣ��
  from student_score ss
 group by ss.name;
--join
select ss.name,ss.score from student_score ss where ss.subject='����';
select ss.name,ss.score from student_score ss where ss.subject='��ѧ';
select ss.name,ss.score from student_score ss where ss.subject='Ӣ��';

select ss01.name, ss01.score ����, ss02.score ��ѧ, ss03.score Ӣ��
  from (select ss.name, ss.score
          from student_score ss
         where ss.subject = '����') ss01
  join (select ss.name, ss.score
          from student_score ss
         where ss.subject = '��ѧ') ss02
    on ss01.name = ss02.name
  join (select ss.name, ss.score
          from student_score ss
         where ss.subject = 'Ӣ��') ss03
    on ss01.name = ss03.name;

--union all
select t.name,sum(t.����),sum(t.��ѧ),sum(t.Ӣ��) from (select ss01.name,ss01.score ����,0 ��ѧ,0 Ӣ�� from student_score ss01 where ss01.subject='����' union all
select ss02.name,0 ����,ss02.score ��ѧ,0 Ӣ�� from student_score ss02 where ss02.subject='��ѧ' union all
select ss03.name,0 ����,0 ��ѧ,ss03.score Ӣ�� from student_score ss03 where ss03.subject='Ӣ��') t group by t.name

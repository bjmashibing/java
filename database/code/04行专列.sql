create table tmp(rq varchar2(10),shengfu varchar2(5));

insert into tmp values('2005-05-09','胜');
insert into tmp values('2005-05-09','胜');
insert into tmp values('2005-05-09','负');
insert into tmp values('2005-05-09','负');
insert into tmp values('2005-05-10','胜');
insert into tmp values('2005-05-10','负');
insert into tmp values('2005-05-10','负');

/*
          胜 负
2005-05-09 2 2
2005-05-10 1 2

*/

select rq,decode(shengfu,'胜',1),decode(shengfu,'负',2) from tmp;

select rq,
       count(decode(shengfu, '胜', 1)) 胜,
       count(decode(shengfu, '负', 2)) 负
  from tmp
 group by rq;


create table STUDENT_SCORE
(
  name    VARCHAR2(20),
  subject VARCHAR2(20),
  score   NUMBER(4,1)
);
insert into student_score (NAME, SUBJECT, SCORE) values ('张三', '语文', 78.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('张三', '数学', 88.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('张三', '英语', 98.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('李四', '语文', 89.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('李四', '数学', 76.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('李四', '英语', 90.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('王五', '语文', 99.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('王五', '数学', 66.0);
insert into student_score (NAME, SUBJECT, SCORE) values ('王五', '英语', 91.0);


/*
姓名   语文  数学  英语
王五    89    56    89
*/
--至少使用4中方式下写出
--decode
select ss.name,
       max(decode(ss.subject, '语文', ss.score)) 语文,
       max(decode(ss.subject, '数学', ss.score)) 数学,
       max(decode(ss.subject, '英语', ss.score)) 英语
  from student_score ss group by ss.name
--case when
select ss.name,
       max(case ss.subject
             when '语文' then
              ss.score
           end) 语文,
       max(case ss.subject
             when '数学' then
              ss.score
           end) 数学,
       max(case ss.subject
             when '英语' then
              ss.score
           end) 英语
  from student_score ss
 group by ss.name;
--join
select ss.name,ss.score from student_score ss where ss.subject='语文';
select ss.name,ss.score from student_score ss where ss.subject='数学';
select ss.name,ss.score from student_score ss where ss.subject='英语';

select ss01.name, ss01.score 语文, ss02.score 数学, ss03.score 英语
  from (select ss.name, ss.score
          from student_score ss
         where ss.subject = '语文') ss01
  join (select ss.name, ss.score
          from student_score ss
         where ss.subject = '数学') ss02
    on ss01.name = ss02.name
  join (select ss.name, ss.score
          from student_score ss
         where ss.subject = '英语') ss03
    on ss01.name = ss03.name;

--union all
select t.name,sum(t.语文),sum(t.数学),sum(t.英语) from (select ss01.name,ss01.score 语文,0 数学,0 英语 from student_score ss01 where ss01.subject='语文' union all
select ss02.name,0 语文,ss02.score 数学,0 英语 from student_score ss02 where ss02.subject='数学' union all
select ss03.name,0 语文,0 数学,ss03.score 英语 from student_score ss03 where ss03.subject='英语') t group by t.name

# Mysql四表联查经典问题

## 创建数据库

```mysql
create database stu_four_table;
```

## 创建表结构

```mysql
-- 学生表
CREATE TABLE `student` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(100) NOT NULL COMMENT '姓名',
  `age` int DEFAULT '1' COMMENT '年龄',
  `sex` varchar(4) DEFAULT NULL COMMENT '性别',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 老师表
CREATE TABLE `teacher` (
`id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
`name` VARCHAR(16) NOT NULL COMMENT '姓名',
PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 课程表
CREATE TABLE `course` (
`id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
`name` VARCHAR(32) NOT NULL COMMENT '课程名称',
`teacherId` INT NOT NULL  COMMENT '上课老师' REFERENCES teacher(id),
PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

-- 分数成绩表
CREATE TABLE `score` (
`id` INT NOT NULL AUTO_INCREMENT COMMENT 'ID',
`stuId` INT NOT NULL COMMENT '学生ID' REFERENCES student(id),
`courseId` INT NOT NULL COMMENT '课程ID' REFERENCES course(id),
`grade` INT NOT NULL COMMENT '成绩',
PRIMARY KEY(`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8;
```

## 准备数据

### 学生数据

```mysql
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (1, '王大锤', 20, '男');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (2, '王小乐', 18, '男');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (3, '张东升', 21, '男');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (4, '张苗', 21, '女');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (5, '梦可儿', 16, '女');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (6, '辰南', 24, '男');
```

### 老师数据

```mysql
INSERT INTO `teacher`(`name`) VALUES('张老师'),('李老师'),('苏老师');
```

### 课程数据

```mysql
INSERT INTO `course`(`name`,`teacherId`) VALUES('语文','1'),('数学','1'),('英语','2'),('物理','3'),('化学','3');
```

成绩数据

```mysql
INSERT INTO `score`(`stuId`,`courseId`,`grade`) VALUES 
(1,1,80),(1,2,98),(1,3,61),(1,4,89),(1,5,96)
,(2,1,50),(2,2,68),(2,3,76),(2,4,78),(2,5,68)
,(3,1,95),(3,2,88),(3,3,86),(3,4,98),(3,5,88)
,(4,1,75),(4,2,68),(4,3,86),(4,4,78),(4,5,88)
,(5,1,80),(5,2,78),(5,3,80),(5,4,88),(5,5,78)
,(6,1,72),(6,2,68),(6,3,70),(6,4,58),(6,5,68);
```

## 练习

1.  查询平均成绩大于80分的学生ID，姓名和平均成绩

   ```mysql
   SELECT a.id,a.`name`,AVG(b.grade) from student as a
   LEFT JOIN score as b on (a.id = b.stuId)
   GROUP BY a.id,a.`name`
   HAVING AVG(b.grade) > 80;
   ```

   以上sql主要是对组函数**AVG**（求平均值）和分组条件判断**HAVING**的应用

2. 查询所有学生的学号、姓名、选课数、总成绩

   ```mysql
   SELECT a.id,a.`name`,COUNT(b.courseId) as courseCount,SUM(b.grade) as allGrade
   from student as a
   LEFT JOIN score as b on (a.id = b.stuId)
   GROUP BY a.id,a.`name`
   ```

3. 查询姓“张”的老师的个数

   ```mysql
   select COUNT(id) from teacher where `name` LIKE '张%';
   ```

4. 查询学过“张老师”所教的所有课的同学的学号、姓名

   ```mysql
   SELECT a.id,a.name
   from student as a
   INNER JOIN score as b on (a.id = b.stuId)
   INNER JOIN course as c on (b.courseId = c.id)
   INNER JOIN teacher as d on (c.teacherId = d.id)
   where d.`name` = '张老师';
   ```

   以上sql存在一个问题，查出来的结果会有重复的数据，结果如下：

   <img src="./res/join_more_result.png" style="float:left" />

   导致以上结果的原因是，张老师有两门课程，通过连接查询的时候就会出现重复的数据，有两种方法可以解决：

   + 使用DISTINCT去重
+ 通过只查询，使用in来过滤
  
```mysql
   SELECT a.id,a.name from student as a
   where a.id in (
   SELECT b.stuId FROM score as b
   INNER JOIN course as c on (b.courseId = c.id)
   INNER JOIN teacher as d on (c.teacherId = d.id)
   where d.`name` = '张老师');
```

5.  查询学过语文和数学两门课程的学生的学号、姓名

   解法一：

   ```mysql
   SELECT a.id,a.`name` from student as a
   INNER JOIN score as b on (a.id=b.stuId)
   WHERE b.courseId in(select id from course WHERE `name` in ('语文' , '数学'))
   GROUP BY a.id,a.`name`
   HAVING count(a.id) = 2;
   ```

   解法二：

   通过exists函数，如下：

   ```mysql
   SELECT a.id,a.`name` from student as a
   INNER JOIN score as b on (a.id=b.stuId)
   where b.courseId = 1 
   and EXISTS(select * from score where courseId=2 and a.id = stuId)
   ```

6.  查询课程编号为“2”的总成绩

   ```mysql
   select sum(grade) from score where courseId = 2
   ```

7.  查询所有课程成绩小于等于80分的学生的学号、姓名

   ```mysql
   SELECT a.id,a.`name` from student as a
   INNER JOIN (SELECT stuId,max(grade) as maxGrade from score GROUP BY stuId) as b
   on (a.id = b.stuId)
   WHERE b.maxGrade <= 80
   ```

   思路：所有课程成绩小于等于80分，也就是说，没有一门课程的成绩是大于80分的，那么可以看每个学生的最高成绩是否大于80分，如果最高成绩都小于等于80分就是我们要找的数据。

8.  查询没有学全所有课的学生的学号、姓名

   ```mysql
   SELECT a.id,a.`name` from student as a
   INNER JOIN score as b on (a.id = b.stuId)
   GROUP BY a.id,a.`name`
   HAVING COUNT(b.id) < (SELECT COUNT(id) from course)
   ```

9.  查询和“2”号同学所学课程完全相同的其他同学的id和name

   ```mysql
   SELECT a.id,a.name FROM student as a
   INNER JOIN score as b on (a.id = b.stuId)
   where a.id != 2 and b.courseId in (SELECT courseId FROM score where stuId = 2)
   GROUP BY a.id,a.name
   HAVING COUNT(b.courseId) = (SELECT COUNT(id) FROM score where stuId = 2)
   ```

10.  查询各科成绩最高和最低的分： 以如下的形式显示：课程名称，最高分，最低分

   ```mysql
   select a.id as '课程ID',a.`name` as '课程名称',MAX(b.grade) as '最高分',MIN(b.grade) as '最低分' from course as a
   LEFT JOIN score as b on (a.id = b.courseId)
   GROUP BY a.id,a.`name`
   ```

   https://blog.csdn.net/wq12310613/article/details/100705492

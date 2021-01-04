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

## 插入数据

```mysql
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (1, '王大锤', 20, '男');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (2, '王小乐', 18, '男');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (3, '张东升', 21, '男');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (4, '张苗', 21, '女');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (5, '梦可儿', 16, '女');
INSERT INTO `student`(`id`, `name`, `age`, `sex`) VALUES (6, '辰南', 24, '男');
```


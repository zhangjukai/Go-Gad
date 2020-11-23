# Mybatis常用知识点梳理总结

Mybatis官网：https://mybatis.org/mybatis-3/zh/index.html
MyBatis-Spring官网：http://mybatis.org/spring/zh/index.html

## Spring整合Mybatis

### 需要导入的三方jar 

```java
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-jdbc</artifactId>
    <version>5.0.12.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis</artifactId>
    <version>3.5.5</version>
</dependency>
<dependency>
    <groupId>org.mybatis</groupId>
    <artifactId>mybatis-spring</artifactId>
    <version>2.0.5</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.22</version>
</dependency>
```

以上jar只是mybatis相关的部分，

### 通过注解实现mybatis配置文件

```java
@ComponentScan(value = "com.zjk.hy.mybatis")
@Configuration
@MapperScan("com.zjk.hy.mybatis.dao")
public class MybatisConfig {
    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost:3306/s-mybatis?serverTimezone=UTC&characterEncoding=utf8&useUnicode=true&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }
}
```

#### @MapperScan

指定Mapper(dao)的包路径，Mybatis会去扫描指定的包路径下的类，然后通过动态代理实现相关功能（后面学习源码时具体了解）

#### 配置数据源-DataSource

此处采用的是Spring-jdbc提供的数据库连接池SimpleDriverDataSource

#### 配置SqlSessionFactory

通过SqlSessionFactoryBean创建一个SqlSessionFactory

### Mapper/Dao

```java
import com.zjk.hy.mybatis.dto.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
    @Select("select * from s_user")
    public List<User> findList();
}
```

对于查询的sql语句，可以通过@Select等注解来写，也可以通过xml文件写

## Mybatis日志实现

具体内容，见同目录下同名文件










### 查看maven依赖树

在pom文件所在目录执行：mvn dependency:tree -f pom.xml，生成jar应用的树形结构

### jar冲突解决

排除包中重复

```java
<exclusions>
   <exclusion>
   <groupId>com.alibaba</groupId>
   <artifactId>fastjson</artifactId>
   </exclusion>
</exclusions>
```

如果存在jar过深引入冲突，在最外层引入较高版本的
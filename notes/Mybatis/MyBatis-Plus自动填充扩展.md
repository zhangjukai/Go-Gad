# MyBatis-Plus自动填充功能扩展

​	本文涉及到MyBatis-Plus、注解、反射、typeconverter（[toddfast的开源项目](https://github.com/toddfast/typeconverter)，在此表示感谢）等技术。

typeconverter的maven地址：

```java
<dependency>
     <groupId>com.toddfast.typeconverter</groupId>
     <artifactId>typeconverter</artifactId>
     <version>1.0</version>
</dependency>
```

## MyBatis-Plus提供的自动填充功能

1、实现元对象处理器接口：com.baomidou.mybatisplus.core.handlers.MetaObjectHandler

2、注解填充字段 @TableField(fill = FieldFill.INSERT) 

```java
 // 注意！这里需要标记为填充字段
    @TableField(fill = FieldFill.INSERT)
    private String fillField;
```

MetaObjectHandler的实现类

```java
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("operator", "Jerry", metaObject);//版本号3.0.6以及之前的版本
    }

    @Override
    public void updateFill(MetaObject metaObject) {
    }
}
```

以上内容基本为官方文档，文档地址：[Mybatis-Plus](https://mp.baomidou.com/guide/auto-fill-metainfo.html)

## 业务需求

​	需要对某些实体的某些属性在insert的时候设置默认值，更新没有要求。从官方API可以看出，设置默认值是固定的，且MetaObjectHandler处理器是针对全局的，这样只试用与对创建时间、创建人、更新时间等这样的字段做自动填充。

## 具体实现

### 定义注解DefaultValue

```java
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DefaultValue {
    String value() default "";
}
```

一个非常普通的注解，有一个value属性，用于记录默认值。

### 使用@DefaultValue

``` java
 @TableField(fill = FieldFill.INSERT)
 @DefaultValue(value = "2019-05-10 09:51:00")
 private LocalDateTime createtime;
 @TableField(fill = FieldFill.INSERT)
 @DefaultValue(value = "10.25")
 private Double money;
```

在需要设置默认值的属性上添加@DefaultValue注解，并设置默认值

### MetaObjectHandler实现

```java
@Component
public class MetaHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // BeanUtils下面提供源码
        List<Field> fields = BeanUtils
            .getFields(metaObject.getOriginalObject()
                       .getClass());
        for(Field field:fields){
            field.setAccessible(true);
            DefaultValue defaultValue = field
                .getAnnotation(DefaultValue.class);
            if(defaultValue!=null){
                // 获取到需要被填充的字段值
                if(getFieldValByName(field.getName(), 	metaObject) == null){ setFieldValByName(field.getName(),TypeConverter.convert(field.getType(),defaultValue.value()),metaObject);
                }
            }
        }
    }
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
```

​	MetaHandler的流程：首先通过反射获取到metaObject的所有属性，然后通过for循环判断每一个属性上是否有@DefaultValue的注解，在注解存在并且属性值为null的情况，就进行默认值得设置。通过TypeConverter.convert对默认值进行类型转换（String -> 具体数据类型），最后通过setFieldValByName方法，设置默认值。

因为update没有业务需求，所以不需要具体实现updateFill。

## 时间类型LocalDateTime的处理

通过MyBatis-Plus自动生成代码，时间类型会对应生成java的LocalDateTime类型，而TypeConverter中并没有提供相应的转化器。

### 自定义LocalDateTime转换器

```java
public class LocalDateTimeConversion implements TypeConverter.Conversion {
    @Override
    public Object[] getTypeKeys() {
        return new Object[]{LocalDateTime.class,  LocalDateTime.class.getName(),"localdatetime"};
    }

    @Override
    public Object convert(Object value) {
        if (value == null) {
            return null;
        } else {
            if (!(value instanceof LocalDateTime)) {
                String v = value.toString();
                if (v.trim().length() == 0) {
                    value = null;
                } else {
                    DateTimeFormatter df = DateTimeFormatter
                        .ofPattern("yyyy-MM-dd HH:mm:ss");
                    value = LocalDateTime.parse(v,df);
                }
            }
            return value;
        }
    }
}
```

​	需要实现TypeConverter.Conversion接口，该接口有两个方法，一个是定义TypeKey（应该是用于类型判断），一个是具体的转换实现。

###LocalDateTime转换器

​	通过查看源码发现，Conversion是存放在一个static的同步的Map集合中的，由此可知，一个Conversion对于一个应用注册一次就够了，所以在系统初始化的时候进行注册。

```java 
@PostConstruct
    public void init(){
        System.out.println("Application.init");
        TypeConverter.registerTypeConversion(new LocalDateTimeConversion());
    }
```

这样就可以实现LocalDateTime的转换了。

## Beanutils.getFields 代码

```java
    public static List<Field> getFields(Class tempClass) {
        List<Field> fromfields = new ArrayList<>();
        while (tempClass != null) {
            fromfields.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }
        return fromfields;
    }
```


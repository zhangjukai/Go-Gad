# Spring循环依赖

为了方便理解循环依赖的整个处理流程，需要对Spring的IOC原理和代码有个简单的认识；

## 循环依赖简介

所谓的循环依赖是指，在Spring中两个或者两个以上的组件相互依赖引用。如下示例：

```java
@Component
public class IndexService {

    @Autowired
    UserService userService;

    public void print(){
        System.out.println("IndexService-aaaaaaaaaaaa");
    }

    public IndexService() {
        System.out.println("IndexService-------construct");
    }
}
```

```java
@Component
public class UserService {
    @Autowired
    IndexService indexService;

    public void print(){
        System.out.println("UserService-aaaaaaaa");
    }

    public UserService() {
        System.out.println("UserService------------------start");
    }
}
```

以上的IndexService和UserService就是一种循环依赖的情况，并且这种情况在Spring中是完全正确的。

对于循环依赖的问题，**Spring默认是支持循环依赖的，但是可以通过设置关闭循环依赖**。

## 关闭循环依赖的方式

### 修改属性的默认值

在`AbstractAutowireCapableBeanFactory`类中，定义了allowCircularReferences属性，默认值为true，表示：自动尝试解析bean之间的循环引用，可将其修改为false。

```java
/** Whether to automatically try to resolve circular references between beans */
private boolean allowCircularReferences = true;
```

### 通过set方法修改

通过set方法将allowCircularReferences的值设置为false，这个操作需要在`refresh();`方法之前执行。这种方式的实现方法比较多：

+ 修改源码

  ```java
  public AnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
      this();
      // 在此处添加关闭循环依赖的设置
      setAllowCircularReferences(false);
      register(annotatedClasses);
      refresh();
  }
  ```

+ 调用方法设置

  ```java
  public static void main(String[] args) {
      AnnotationConfigApplicationContext context = new 
          AnnotationConfigApplicationContext();
      context.setAllowCircularReferences(false);
      context.register(CircleMainConfig.class);
      context.refresh();
  }
  ```

## Spring处理循环依赖的原理

### 实例对象和SpringBean的区别

在这儿需要对实例对象和SpringBean进行下区分，方便后面理解Spring通过三级缓存解决循环依赖问题。

+ 实例对象

  一般是通过new或者反射instance处理的对象

+ SpringBean

  对于Spring中的bean，指的是加载到Spring容器中的bean。Spring首先时间一个Class加载为一个BeanDefinition，然后BeanFactory使用BeanDefinition创建一个实例，整个过程中会有许多前置或者后置方法、属性初始化方法等需要执行，最后完成SpringBean的创建并将其注入到Spring容器中。对于默认的单列的Bean最后是存放到singletonObjects（一个Map）中的。

### Spring的三级缓存

+ 一级缓存-singletonObjects

  ```java
  /** Cache of singleton objects: bean name --> bean instance */
  private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
  ```

  单列模式的SpringBean创建完成后，最终存放的位置，map的key为bean的名称，map的value值为SpringBean的实例。

+ 二级缓存-singletonFactories

  ```java
  /** Cache of singleton factories: bean name --> ObjectFactory */
  private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);
  ```

  以bean的name作为key，缓存bean的factory

+ 三级缓存-earlySingletonObjects

  ```java
  /** Cache of early singleton objects: bean name --> bean instance */
  private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
  ```

  缓存已经实例化的bean

以上三个属性都是位于`DefaultSingletonBeanRegistry`类中。

### 缓存正在创建的Bean的name

在解决循环依赖的过程中有个很重要的点就是如何判断一个bean是否正在创建中，

```java
public boolean isSingletonCurrentlyInCreation(String beanName) {
   return this.singletonsCurrentlyInCreation.contains(beanName);
}
```

Spring采用的方式是通过一个set集合缓存正在创建的bean的名字，

```java
/** Names of beans that are currently in creation */
private final Set<String> singletonsCurrentlyInCreation =
      Collections.newSetFromMap(new ConcurrentHashMap<>(16));
```

明白了以上三点后，有助于对Spring处理循环依赖流程的理解。
# Spring扩展点

## initPropertySources

初始化设置一些属性，子类自定义个性化的属性设置方法

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyAnnotationConfigApplicationContext extends 
    AnnotationConfigApplicationContext {

    public MyAnnotationConfigApplicationContext(Class<?>... annotatedClasses) {
        super(annotatedClasses);
    }
    @Override
    protected void initPropertySources() {
        System.out.println("===================================");
        // 设置abc属性是必须的
        getEnvironment().setRequiredProperties("abc")
        System.out.println("===================================");
    }
}
```

## customizeBeanFactor

```java
@Override
protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
    beanFactory.setAllowBeanDefinitionOverriding(false);
    beanFactory.setAllowCircularReferences(false);
    super.customizeBeanFactory(beanFactory);
}
```

以上代码的作用是

![](./res/BeanFactory_init_info.png)

BeanFactory刚创建完成，这两个参数都是true，能够通过customizeBeanFactory的重载方法进行扩展。

```java
protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
   if (this.allowBeanDefinitionOverriding != null) {
      beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
   }
   if (this.allowCircularReferences != null) {
      beanFactory.setAllowCircularReferences(this.allowCircularReferences);
   }
}
```

## 自定义标签

## 自定义属性编辑器

+ 自定义继承自PropertyEditorSupport的编辑器

+ 自定义一个实现了PropertyEditorRegistrar接口的类

  作为属性编辑器的注册器，该注册器的目的是为了让Spring识别到上面自定义的编辑器

+ 让Spring能够识别到注册器
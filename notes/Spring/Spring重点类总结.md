## Spring重点类总结

### BeanPostProcessor

bean的后置处理器，插手bean的实例化过程，在bean实例化完成后，添加到Spring容器之前执行。

经典应用场景：@PostConstruct，AOP等

### BeanFactoryPostProcessor

BeanFactory的后置处理器，Spring容器中任何一个bean实例化之前执行，针对beanFactory做一些操作，

经典应用场景：configrationClassPostProcessor#postProcessorBeanFactory，针对加了@Configuration注解的类加上CGLIB代理。

### BeanDefinitionRegistryPostProcessor

BeanDefinitionRegistry的后置处理器，是BeanFactoryPostProcessor的子类，在BeanFactoryPostProcessor#postProcessorBeanFactory之前执行。

经典应用场景：configrationClassPostProcessor#postProcessBeanDefinitionRegistry，该方法主要完成指定包路径下类的扫描，三种@Import的扫描，@Configuration、@Bean的扫描，等等。

### ImportSelector

ImportSelector是一个接口，实现该接口，通过实现selectImports方法，返回一个类名（全名）数组，Spring会把这些类封装成BeanDefinition(**这个BD是通过class来的，无法更改，不可变的**)，存放到BeanDefinition对应的map中。

### ImportBeanDefinitionRegistrar

ImportBeanDefinitionRegistrar是一个接口，实现该接口，通过实现registerBeanDefinitions方法，可以得到BeanDefinitionRegistry，通过BeanDefinitionRegistry就可以动态的添加或者改变BeanDefinition。

经典应用场景：Mybatis的mapperScan
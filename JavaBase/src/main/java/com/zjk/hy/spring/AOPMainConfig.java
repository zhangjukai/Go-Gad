package com.zjk.hy.spring;

import com.zjk.hy.spring.aop.*;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class AOPMainConfig {
    @Bean
    public MathCalculator mathCalculator(){
        return new MathCalculator();
    }
    @Bean
    @Scope("prototype")
    public DeclareParentsService declareParentsService(){
        return new DeclareParentsService();
    }
    @Bean
    @Scope("prototype")
    public DefaultMathCalculator defaultMathCalculator(){
        return new DefaultMathCalculator();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AOPMainConfig.class);
     /*   Calculator calculator = (Calculator) context.getBean(DeclareParentsService.class);
        calculator.div(10,1);*/
       /* DeclareParentsService declareParentsService = context.getBean(DeclareParentsService.class);
        declareParentsService.print();
        DeclareParentsService declareParentsService1 = context.getBean(DeclareParentsService.class);
        declareParentsService1.print();*/
        Calculator  calculator = (Calculator) context.getBean("defaultMathCalculator");
        calculator.div(10,2);
        calculator.sayHello();
        context.close();
    }
}

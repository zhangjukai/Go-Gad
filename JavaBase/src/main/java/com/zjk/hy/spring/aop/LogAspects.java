package com.zjk.hy.spring.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Aspect
public class LogAspects {
    @Pointcut("execution(public int com.zjk.hy.spring.aop.MathCalculator.*(..))")
    public void pointCut(){

    }
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        System.out.println(joinPoint.getSignature().getName()+"--除法开始执行,参数为："+ Arrays.toString(args));
    }
    @After("pointCut()")
    public void logEnd(JoinPoint joinPoint){
        System.out.println(joinPoint.getSignature().getName()+"--除法执行完成。。。。");
    }

    @AfterReturning(value = "pointCut()",returning = "result")
    public void logReturn(JoinPoint joinPoint,Object result){
        System.out.println(joinPoint.getSignature().getName()+"--除法正常返回,返回结果为："+result);
    }

    @AfterThrowing(value = "pointCut()",throwing="e")
    public void logError(JoinPoint joinPoint,Exception e){
        System.out.println(joinPoint.getSignature().getName()+"--异常情况,异常信息为："+e.getMessage());
    }
}

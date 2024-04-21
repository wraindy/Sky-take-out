package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Author Wraindy
 * @DateTime 2024/04/21 15:49
 * Description 自定义Aspect（切面），实现公共字段自动填充处理逻辑
 * Notice Aspect = PointCut(切点) + Advice(通知)
 **/
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点
     * 用于指出在何处执行操作
     * ‘*’：匹配任意返回类型的方法
     * execution：方法执行的时候触发
     * com.sky.mapper.* ： com.sky.mapper包下的任意类(类接口枚举注解等)
     * ‘.*’ ： 类中的任意方法
     * (..) ：匹配任意参数的方法
     * annotation(com.sky.annotation.AutoFill) ：表示匹配到的方法还要有AutoFill注解
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {}

    /**
     * Before：指出在何时执行操作（在autoFillPointCut切点指定的任何方法执行前被触发。）
     * 函数体： 指出执行什么操作
     *  joinPoint: 被Pointcut锚定的对象
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("\n\n开始进行公共字段的填充..." +
                "\njoinPoint: {}",joinPoint +"\n");

        // 1.获取当前被拦截的方法的数据库操作类型（通过注解）
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = annotation.value();

        // 2.获取当前被拦截的方法的参数---数据库entity
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) {
            return;
        }
        // 约定，第一个参数是entity
        Object entity = args[0];

        // 3.为公共字段准备赋值的内容
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 4.根据(1.)操作类型，使用反射赋值
        Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
        Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
        setUpdateUser.invoke(entity, currentId);
        setUpdateTime.invoke(entity, now);
        if(operationType == OperationType.INSERT){
            Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
            setCreateUser.invoke(entity, currentId);
            setCreateTime.invoke(entity, now);
        }


    }
}

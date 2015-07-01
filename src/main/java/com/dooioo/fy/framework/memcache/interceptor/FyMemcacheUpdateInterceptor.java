package com.dooioo.fy.framework.memcache.interceptor;

import com.dooioo.fy.framework.memcache.annotation.FyMemcacheNamespaceKey;
import com.dooioo.fy.framework.memcache.annotation.FyMemcachePrimaryKey;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * FyMemcacheUpdate 缓存更新
 * 分为单条和批量更新的方式
 * FyMemcachePrimaryKey 单条更新    暂时都只支持一个
 * FyMemcacheNamespaceKey 批量更新   暂时都只支持一个
 *
 * @author ouyang
 * @since 2015-03-23 12:14
 */
@Aspect
@Component
public class FyMemcacheUpdateInterceptor {

    @Autowired
    private MemcachedClient fyMemcachedClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(FyMemcacheUpdateInterceptor.class);

    /**
     * 拦截 @FyMemcacheUpdate  aop
     *
     * @author ouyang
     * @since 2015-04-24 10:24
     */
    @SuppressWarnings("EmptyMethod")
    @Pointcut("@annotation(com.dooioo.fy.framework.memcache.annotation.FyMemcacheUpdate)")
    private void fyMemcacheUpdatePointCut() {
    }

    /**
     * 处理拦截 @FyMemcacheUpdate  aop
     *
     * @param joinPoint 代理点
     * @return 代理方法的返回值
     * @throws Throwable
     * @author ouyang
     * @since 2015-04-24 10:24
     */
    @Around("fyMemcacheUpdatePointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObject = joinPoint.proceed();
        try {
            Signature signature = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;

            //切面的方法参数
            Object[] args = joinPoint.getArgs();

            //paramsAnoAry二维数组， 每一个paramsAnoAry[i] 可能有多个注解
            //FyMemcacheNamespaceKey 0-1个
            List<String> fyMemcacheNamespaceKeyArgList = new ArrayList<>();
            //FyMemcachePrimaryKey  0-1个
            List<String> fyMemcachePrimaryKeyArgList = new ArrayList<>();

            Annotation[][] paramsAnoAry = methodSignature.getMethod().getParameterAnnotations();
            //This inspection is intended for J2ME and other highly resource constrained environments.
            // Applying the results of this inspection without consideration might have negative effects on code clarity and design.
            //Reports any access to the .length of an array in the condition part of a loop statement.
            // In highly resource constrained environments, such calls may have adverse performance implications.
            int paaLength = paramsAnoAry.length;
            for (int i = 0; i < paaLength; i++) {
                Annotation[] annotationAry = paramsAnoAry[i];
                if (annotationAry.length > 0) {
                    for (Annotation annotation : annotationAry) {
                        if (annotation instanceof FyMemcacheNamespaceKey) {
                            fyMemcacheNamespaceKeyArgList.add(args[i].toString());
                        }
                        if (annotation instanceof FyMemcachePrimaryKey) {
                            fyMemcachePrimaryKeyArgList.add(args[i].toString());
                        }
                    }
                }
            }

            //数据操作层class 一般统一为 DAO
            String daoClassName = joinPoint.getTarget().getClass().getName();

            //单条
            if (fyMemcachePrimaryKeyArgList.size() > 0) {
                //主键加上当前dao空间
                fyMemcachedClient.delete(daoClassName + fyMemcachePrimaryKeyArgList.get(0));
            }
            //批量 用命名空间方式来实现
            if (fyMemcacheNamespaceKeyArgList.size() > 0) {
                fyMemcachedClient.delete(daoClassName + fyMemcacheNamespaceKeyArgList.get(0));
            }
        } catch (TimeoutException | InterruptedException | MemcachedException e) {
            LOGGER.error(e.getClass().getName(), e);
        }
        return returnObject;
    }

}





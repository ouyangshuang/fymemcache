package com.dooioo.fy.framework.memcache.annotation;

import java.lang.annotation.*;

/**
 * 房源缓存注解 30分钟有效 不更新
 *
 * @author ouyang
 * @since 2015-03-23 12:10
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface FyMemcache {

    //过期时间默认30分钟 单位分钟

    /**
     * 过期时间
     *
     * @return 过期时间
     * @author ouyang
     * @since 2015-04-24 10:22
     */
    int exp() default 30;

}

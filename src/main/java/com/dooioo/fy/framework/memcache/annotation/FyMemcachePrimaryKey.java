package com.dooioo.fy.framework.memcache.annotation;

import java.lang.annotation.*;

/**
 * 针对根据主键查询对象的缓存
 *
 * @author ouyang
 * @since 2015-04-20 17:38
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Inherited
public @interface FyMemcachePrimaryKey {
}

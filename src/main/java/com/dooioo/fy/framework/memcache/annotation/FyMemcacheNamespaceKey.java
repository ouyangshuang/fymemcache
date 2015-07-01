package com.dooioo.fy.framework.memcache.annotation;

import java.lang.annotation.*;

/**
 * @author ouyang
 * @since 2015-04-20 17:09
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Inherited
public @interface FyMemcacheNamespaceKey {

}

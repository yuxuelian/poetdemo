package com.kaibo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kaibo
 * @date 2018/11/23 14:40
 * @GitHub:https://github.com/yuxuelian
 * @email:kaibo1hao@gmail.com
 * @description:
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Builder {
}

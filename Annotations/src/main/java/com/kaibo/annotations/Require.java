package com.kaibo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author kaibo
 * @date 2018/11/23 14:41
 * @GitHub:https://github.com/yuxuelian
 * @email:kaibo1hao@gmail.com
 * @description:
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Require {
}

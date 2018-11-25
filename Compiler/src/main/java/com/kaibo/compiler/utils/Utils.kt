package com.kaibo.compiler.utils

/**
 * @author kaibo
 * @date 2018/11/24 18:09
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

fun String.camelToUnderline(): String {
    return fold(StringBuffer()) { acc, c ->
        if (c.isUpperCase()) {
            acc.append("_").append(c.toLowerCase())
        } else {
            acc.append(c)
        }
    }.toString()
}

package com.kaibo.compiler.activity.entity

import com.bennyhuo.aptutils.types.asJavaTypeName
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol

/**
 * @author kaibo
 * @date 2018/11/24 17:01
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */
open class Field(private val symbol: Symbol.VarSymbol) : Comparable<Field> {

    val name = symbol.qualifiedName.toString()

    // 前缀
    open val prefix = "REQUIRE_"

    // 是否是私有的
    val isPrivate = symbol.isPrivate

    // 是否是基本数据类型
    val isPrimitive = symbol.type.isPrimitive

    override fun compareTo(other: Field) = name.compareTo(other.name)

    override fun toString(): String {
        return "$name:${symbol.type}"
    }

    fun asJavaTypeName(): TypeName = symbol.type.asJavaTypeName()
}
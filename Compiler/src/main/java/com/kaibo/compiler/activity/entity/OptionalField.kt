package com.kaibo.compiler.activity.entity

import com.bennyhuo.aptutils.types.asTypeMirror
import com.kaibo.annotations.Optional
import com.sun.tools.javac.code.Symbol
import javax.lang.model.type.TypeKind

/**
 * @author kaibo
 * @date 2018/11/24 17:07
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */


class OptionalField(private val symbol: Symbol.VarSymbol) : Field(symbol) {

    private val optional = symbol.getAnnotation(Optional::class.java)

    override val prefix = "OPTIONAL_"

    @Suppress("IMPLICIT_CAST_TO_ANY")
    val defaultValue = when (symbol.type.kind) {
        TypeKind.CHAR, TypeKind.BYTE, TypeKind.SHORT, TypeKind.INT, TypeKind.LONG -> optional.numValue
        TypeKind.BOOLEAN -> optional.booleanValue
        TypeKind.FLOAT, TypeKind.DOUBLE -> optional.decimalValue
        else -> if (symbol.type == String::class.java.asTypeMirror()) {
            """"${optional.stringValue}""""
        } else {
            null
        }
    }

    override fun compareTo(other: Field): Int {
        return if (other is OptionalField) {
            super.compareTo(other)
        } else {
            1
        }
    }

}
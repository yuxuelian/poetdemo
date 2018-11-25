package com.kaibo.compiler.activity

import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.kaibo.compiler.activity.entity.Field
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 * @author kaibo
 * @date 2018/11/24 16:57
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class ActivityClass(val typeElement: TypeElement) {

    // 类名
    val simpleName = typeElement.simpleName()

    // 包名
    val packageName = typeElement.packageName()

    // 是否是抽象类
    val isAbstract = typeElement.modifiers.contains(Modifier.ABSTRACT)

    val fields = TreeSet<Field>()

    val isKotlin = typeElement.getAnnotation(Metadata::class.java) != null

    // 构建器
    val builder = ActivityClassBuilder(this)

    override fun toString(): String {
        return "$packageName.$simpleName[${fields.joinToString()}]"
    }

}
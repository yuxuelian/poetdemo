package com.kaibo.compiler.activity.method

import com.kaibo.compiler.activity.ActivityClass
import com.kaibo.compiler.activity.entity.Field
import com.kaibo.compiler.utils.camelToUnderline
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeSpec
import com.sun.tools.doclets.internal.toolkit.builders.FieldBuilder
import javax.lang.model.element.Modifier

/**
 * @author kaibo
 * @date 2018/11/24 17:58
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class ConstantBuilder(private val activityClass: ActivityClass) {

    fun build(typeBuilder: TypeSpec.Builder) {
        activityClass.fields.forEach { field: Field ->
            typeBuilder.addField(
                FieldSpec
                    .builder(
                        String::class.java,
                        field.prefix + field.name
                            .camelToUnderline()
                            .toUpperCase()
                    )
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
                    .initializer("\$S", field.name)
                    .build()
            )
        }
    }
}
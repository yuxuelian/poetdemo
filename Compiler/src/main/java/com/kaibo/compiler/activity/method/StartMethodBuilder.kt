package com.kaibo.compiler.activity.method

import com.kaibo.compiler.activity.ActivityClass
import com.kaibo.compiler.activity.ActivityClassBuilder
import com.kaibo.compiler.activity.entity.Field
import com.kaibo.compiler.activity.entity.OptionalField
import com.kaibo.compiler.prebuilt.INTENT
import com.squareup.javapoet.*
import javax.lang.model.element.Modifier

/**
 * @author kaibo
 * @date 2018/11/24 18:22
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class StartMethodBuilder(val activityClass: ActivityClass) {

    fun build(typeBuilder: TypeSpec.Builder) {
        // 对参数进行分组
        val groupBy = activityClass.fields.groupBy { it is OptionalField }
        val requireFields = groupBy[false] ?: emptyList()
        val optionalFields = groupBy[true] ?: emptyList()

        // 一个optional都没有的
        val startActivityNoOptional = StartMethod(activityClass, ActivityClassBuilder.METHOD_NAME_NO_OPTIONAL)
        startActivityNoOptional.addAllField(requireFields)
        startActivityNoOptional.build(typeBuilder)

        // 所有参数都有的 (包括只有一个参数的重载情况)
        if (optionalFields.isNotEmpty()) {
            val startMethod = startActivityNoOptional.copy(ActivityClassBuilder.METHOD_NAME)
            startMethod.addAllField(optionalFields)
            startMethod.build(typeBuilder)
        }

        // 重载optional
        if (optionalFields.size == 2) {
            optionalFields.forEach { field: Field ->
                startActivityNoOptional
                    .copy(ActivityClassBuilder.METHOD_NAME_FOR_OPTIONAL)
                    .also { it.addField(field) }
                    .build(typeBuilder)
            }
        }

        // 超过三个的情况
        if (optionalFields.size >= 3) {
            val builderName = activityClass.simpleName + ActivityClassBuilder.TYPE_POSIX

            // 构建 fillIntent 方法
            val fillIntentMethodBuilder = MethodSpec.methodBuilder("fillIntent")
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.VOID)
                .addParameter(INTENT.java, "intent")

            // 获取生成的那个类的 ClassName
            val builderClassName = ClassName.get(activityClass.packageName, builderName)
            optionalFields.forEach { field: Field ->
                // 添加属性到 生成的那个类里边去
                typeBuilder.addField(
                    FieldSpec.builder(field.asJavaTypeName(), field.name, Modifier.PRIVATE)
                        .build()
                )

                // 添加给属性赋值的方法
                typeBuilder.addMethod(
                    MethodSpec.methodBuilder(field.name)
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(field.asJavaTypeName(), field.name)
                        .addStatement("this.${field.name}=${field.name}")
                        .addStatement("return this")
                        .returns(builderClassName)
                        .build()
                )

                if (field.isPrimitive) {
                    // 如果是基本数据类型  直接  putExtra  到intent
                    fillIntentMethodBuilder.addStatement("intent.putExtra(\$S, \$L)", field.name, field.name)
                } else {
                    // 否则的话 需要  判断是否为 null
                    fillIntentMethodBuilder.beginControlFlow("if(\$L != null)", field.name)
                        .addStatement("intent.putExtra(\$S, \$L)", field.name, field.name)
                        .endControlFlow()
                }
            }

            // 添加 fillIntent 方法到类中
            typeBuilder.addMethod(fillIntentMethodBuilder.build())

            val startActivityWithOptionals =
                startActivityNoOptional.copy(ActivityClassBuilder.METHOD_NAME_FOR_OPTIONALS)
            // 非静态方法
            startActivityWithOptionals
                .startMethod(false)
                .build(typeBuilder)

        }
    }

}
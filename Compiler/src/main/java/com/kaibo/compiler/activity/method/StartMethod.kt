package com.kaibo.compiler.activity.method

import com.kaibo.compiler.activity.ActivityClass
import com.kaibo.compiler.activity.entity.Field
import com.kaibo.compiler.prebuilt.ACTIVITY_BUILDER
import com.kaibo.compiler.prebuilt.CONTEXT
import com.kaibo.compiler.prebuilt.INTENT
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

/**
 * @author kaibo
 * @date 2018/11/24 18:21
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class StartMethod(
    private val activityClass: ActivityClass,
    private val name: String
) {

    private val fields = ArrayList<Field>()

    private var isStaticMethod = true

    fun startMethod(staticMethod: Boolean): StartMethod {
        this.isStaticMethod = staticMethod
        return this
    }

    fun addAllField(fields: List<Field>) {
        this.fields += fields
    }

    fun addField(field: Field) {
        this.fields += field
    }

    fun copy(name: String) = StartMethod(activityClass, name).also {
        it.fields += fields
    }

    fun build(typeBuilder: TypeSpec.Builder) {
        // 创建方法体
        val methodBuilder = MethodSpec.methodBuilder(name)
            .addModifiers(Modifier.PUBLIC)
            .returns(TypeName.VOID)
            .addParameter(CONTEXT.java, "context")

        // 创建Intent
        methodBuilder.addStatement(
            "\$T intent = new \$T(context, \$T.class)",
            INTENT.java,
            INTENT.java,
            activityClass.typeElement
        )


        fields.forEach { field: Field ->
            val name = field.name
            methodBuilder.addParameter(field.asJavaTypeName(), name)
                .addStatement("intent.putExtra(\$S, \$L)", name, name)
        }

        // 是否是静态的方法
        if (isStaticMethod) {
            methodBuilder.addModifiers(Modifier.STATIC)
        } else {
            methodBuilder.addStatement("fillIntent(intent)")
        }

        methodBuilder.addStatement("\$T.INSTANCE.startActivity(context, intent)", ACTIVITY_BUILDER.java)

        // 将方法添加到类中
        typeBuilder.addMethod(methodBuilder.build())
    }
}

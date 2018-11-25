package com.kaibo.compiler.activity.method

import com.kaibo.compiler.activity.ActivityClass
import com.kaibo.compiler.activity.entity.Field
import com.kaibo.compiler.activity.entity.OptionalField
import com.kaibo.compiler.prebuilt.ACTIVITY
import com.kaibo.compiler.prebuilt.BUNDLE
import com.kaibo.compiler.prebuilt.BUNDLE_UTILS
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

/**
 * @author kaibo
 * @date 2018/11/25 0:09
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class InjectMethodBuilder(private val activityClass: ActivityClass) {

    fun build(typeBuilder: TypeSpec.Builder) {
        // 构建 inject 方法
        val injectMethodBuilder = MethodSpec.methodBuilder("inject")
            .addParameter(ACTIVITY.java, "instance")
            .addParameter(BUNDLE.java, "savedInstanceState")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.VOID)
            .beginControlFlow("if(instance instanceof \$T)", activityClass.typeElement)
            .addStatement("\$T typeInstance = (\$T)instance", activityClass.typeElement, activityClass.typeElement)
            .addStatement(
                "\$T extras = savedInstanceState == null ? instance.getIntent().getExtras() : savedInstanceState",
                BUNDLE.java
            )
            .beginControlFlow("if (extras !=null )")

        activityClass.fields.forEach { field: Field ->
            val name = field.name

            // box  的意思是  如果是基础数据类型   就使用它们对应的装箱类型
            val typeName = field.asJavaTypeName().box()

            // 从 extras 中获取值
            if (field is OptionalField) {
                // 获取 OptionalField
                injectMethodBuilder.addStatement(
                    "\$T \$LValue = \$T.<\$T>get(extras, \$S, \$L)",
                    typeName,
                    name,
                    BUNDLE_UTILS.java,
                    typeName,
                    name,
                    field.defaultValue
                )
            } else {
                // 获取 Require
                injectMethodBuilder.addStatement(
                    "\$T \$LValue = \$T.<\$T>get(extras, \$S)",
                    typeName,
                    name,
                    BUNDLE_UTILS.java,
                    typeName,
                    name
                )
            }

            // 赋值
            if (field.isPrivate) {
                // capitalize 大写字符串的首字母
                injectMethodBuilder.addStatement("typeInstance.set\$L(\$LValue)", name.capitalize(), name)
            } else {
                injectMethodBuilder.addStatement("typeInstance.\$L = \$LValue", name, name)
            }
        }

        injectMethodBuilder.endControlFlow()
        injectMethodBuilder.endControlFlow()

        // 添加方法到类中
        typeBuilder.addMethod(injectMethodBuilder.build())
    }

}
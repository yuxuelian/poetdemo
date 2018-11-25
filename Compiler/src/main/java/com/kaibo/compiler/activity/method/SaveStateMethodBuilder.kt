package com.kaibo.compiler.activity.method

import com.kaibo.compiler.activity.ActivityClass
import com.kaibo.compiler.activity.entity.Field
import com.kaibo.compiler.prebuilt.ACTIVITY
import com.kaibo.compiler.prebuilt.BUNDLE
import com.kaibo.compiler.prebuilt.INTENT
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

class SaveStateMethodBuilder(private val activityClass: ActivityClass) {

    fun build(typeBuilder: TypeSpec.Builder) {

        val savedInstanceBuilder = MethodSpec.methodBuilder("savedInstanceState")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addParameter(ACTIVITY.java, "instance")
            .addParameter(BUNDLE.java, "outState")
            .returns(TypeName.VOID)
            // 判断类型
            .beginControlFlow("if(instance instanceof \$T)", activityClass.typeElement)
            // 强转为当前的类型
            .addStatement("\$T typeInstance = (\$T)instance", activityClass.typeElement, activityClass.typeElement)
            .addStatement("\$T intent = new \$T()", INTENT.java, INTENT.java)
        // 保存参数
        activityClass.fields.forEach { field: Field ->
            val name = field.name
            if (field.isPrivate) {
                // 添加值到 intent中
                savedInstanceBuilder.addStatement(
                    "intent.putExtra(\$S, typeInstance.get\$L())",
                    name,
                    name.capitalize()
                )
            } else {
                savedInstanceBuilder.addStatement(
                    "intent.putExtra(\$S, typeInstance.\$L)",
                    name,
                    name
                )
            }
        }
        savedInstanceBuilder.addStatement("outState.putAll(intent.getExtras())")
        savedInstanceBuilder.endControlFlow()
        typeBuilder.addMethod(savedInstanceBuilder.build())
    }

}
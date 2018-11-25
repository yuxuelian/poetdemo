package com.kaibo.compiler.activity

import com.kaibo.compiler.activity.method.ConstantBuilder
import com.kaibo.compiler.activity.method.InjectMethodBuilder
import com.kaibo.compiler.activity.method.SaveStateMethodBuilder
import com.kaibo.compiler.activity.method.StartMethodBuilder
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import javax.annotation.processing.Filer
import javax.lang.model.element.Modifier


/**
 * @author kaibo
 * @date 2018/11/24 17:49
 * @GitHub：https://github.com/yuxuelian
 * @email：kaibo1hao@gmail.com
 * @description：
 */

class ActivityClassBuilder(val activityClass: ActivityClass) {

    companion object {
        // 类名后缀
        const val TYPE_POSIX = "Builder"

        // 方法名
        const val METHOD_NAME = "start"
        const val METHOD_NAME_NO_OPTIONAL = METHOD_NAME + "WithoutOptional"
        const val METHOD_NAME_FOR_OPTIONAL = METHOD_NAME + "WithOptional"
        const val METHOD_NAME_FOR_OPTIONALS = METHOD_NAME + "WithOptionals"
    }

    fun build(filer: Filer) {
        if (activityClass.isAbstract) {
            return
        }
        // 创建类构造器
        val typeBuilder = TypeSpec
            .classBuilder(activityClass.simpleName + TYPE_POSIX)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
        // 创建常量
        ConstantBuilder(activityClass).build(typeBuilder)
        // 创建start方法
        StartMethodBuilder(activityClass).build(typeBuilder)
        // 创建inject方法
        InjectMethodBuilder(activityClass).build(typeBuilder)
        // 创建saveState方法
        SaveStateMethodBuilder(activityClass).build(typeBuilder)
        // 生成类文件
        writeJavaToFile(filer, typeBuilder.build())
    }

    private fun writeJavaToFile(filer: Filer, typeSpec: TypeSpec) {
        try {
            JavaFile.builder(activityClass.packageName, typeSpec).build().writeTo(filer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
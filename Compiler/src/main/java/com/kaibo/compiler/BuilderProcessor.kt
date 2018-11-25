package com.kaibo.compiler

import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.isSubTypeOf
import com.kaibo.annotations.Builder
import com.kaibo.annotations.Optional
import com.kaibo.annotations.Require
import com.kaibo.compiler.activity.ActivityClass
import com.kaibo.compiler.activity.ActivityClassBuilder
import com.kaibo.compiler.activity.entity.Field
import com.kaibo.compiler.activity.entity.OptionalField
import com.sun.tools.javac.code.Symbol
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

/**
 * @author kaibo
 * @date 2018/11/23 14:38
 * @GitHub:https://github.com/yuxuelian
 * @email:kaibo1hao@gmail.com
 * @description:
 */

class BuilderProcessor : AbstractProcessor() {

    private val supportedAnnotations = setOf(Builder::class.java, Require::class.java, Optional::class.java)

    /**
     * 初始化
     */
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
    }

    /**
     * 生成的源码版本
     */
    override fun getSupportedSourceVersion() = SourceVersion.RELEASE_8

    /**
     * 返回支持哪些注解
     */
    override fun getSupportedAnnotationTypes() = supportedAnnotations
        .mapTo(HashSet<String>(), Class<*>::getCanonicalName)

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val activityClasses = HashMap<Element, ActivityClass>()
        roundEnv.getElementsAnnotatedWith(Builder::class.java)
            .filter {
                // 只有标注在类上的注解才进行处理
                it.kind.isClass
            }
            .forEach { element: Element ->
                try {
                    // 是否是Activity的子类
                    if (element.asType().isSubTypeOf("android.app.Activity")) {
                        activityClasses[element] = ActivityClass(element as TypeElement)
                    } else {
                        Logger.error(element, "Unsupported typeElement ${element.simpleName}")
                    }
                } catch (e: Exception) {
                    Logger.logParsingError(element, Builder::class.java, e)
                }
            }

        roundEnv.getElementsAnnotatedWith(Require::class.java)
            .filter {
                // 只有标注在属性上的注解才进行处理
                it.kind == ElementKind.FIELD
            }
            .forEach { element: Element ->
                activityClasses[element.enclosingElement]
                    ?.fields
                    ?.add(Field(element as Symbol.VarSymbol))
                // 为空对应的Activity没有标注Builder注解的情况
                    ?: Logger.error(
                        element,
                        "Field $element annotated as Required while ${element.enclosingElement} not annotated"
                    )
            }
        roundEnv.getElementsAnnotatedWith(Optional::class.java)
            .filter {
                // 只有标注在属性上的注解才进行处理
                it.kind == ElementKind.FIELD
            }
            .forEach { element: Element ->
                activityClasses[element.enclosingElement]
                    ?.fields
                    ?.add(OptionalField(element as Symbol.VarSymbol))
                // 为空对应的Activity没有标注Builder注解的情况
                    ?: Logger.error(
                        element,
                        "Field $element annotated as Required while ${element.enclosingElement} not annotated"
                    )
            }

        // 构建
        activityClasses.values.forEach {
            it.builder.build(AptContext.filer)
        }
        return true
    }

}
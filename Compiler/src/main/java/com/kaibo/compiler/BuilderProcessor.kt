package com.kaibo.compiler

import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.kaibo.annotations.Builder
import com.kaibo.annotations.Optional
import com.kaibo.annotations.Require
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
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
        roundEnv.getElementsAnnotatedWith(Builder::class.java).forEach {
            Logger.note(it, it.simpleName.toString())
        }
        return true
    }

}
package com.ilaftalkful.mobileonthego.retrofit

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Exclude

class ExcludeAnnotationStrategy: ExclusionStrategy {
    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return f?.getAnnotation(Exclude::class.java) != null
    }

    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return clazz?.getAnnotation(Exclude::class.java) != null
    }

}
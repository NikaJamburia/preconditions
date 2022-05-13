package ge.nika.preconditions.core.template

import kotlin.reflect.KProperty1
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties

internal class AnyObject(
    private val obj: Any
) {

    @Suppress("UNCHECKED_CAST")
    fun valueOf(fieldName: String): Any =
        if (obj is Map<*, *>) {
            obj[fieldName] ?: error("Field is not initialized")
        } else {
            getField(fieldName)
        }

    private fun getField(fieldName: String): Any {
        val clazz = obj::class.java
        val field = clazz.kotlin.declaredMemberProperties
            .firstOrNull { it.name == fieldName }
            ?. let { it as KProperty1<Any, *> }
            ?: error("Field $fieldName is not present on ${clazz.findSimpleName()}")

        check(field.visibility == KVisibility.PUBLIC) { "Field ${field.name} of ${clazz.findSimpleName()} is inaccessible" }

        return field.get(obj) ?: error("Field is not initialized")
    }

    private fun Class<out Any>.findSimpleName(): String =
        if(this.simpleName == "") {
            this.name
        } else {
            this.simpleName
        }

}
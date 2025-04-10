package ge.nika.preconditions.core.api.template

import ge.nika.preconditions.core.template.AnyObject

class DeepTemplateContext(
    private val values: Map<String, Any>
) : TemplateContext {

    override fun findValue(dottedQuery: DottedQuery): Any {

        val requestedObject = values[dottedQuery.objectName]
            ?: error("No value provided for template parameter ${dottedQuery.objectName}")

        return if (dottedQuery.requestedFields.isNotEmpty()) {
            dottedQuery.requestedFields
                .fold(requestedObject) { obj, fieldName ->
                    AnyObject(obj).valueOf(fieldName)
                }
        } else {
            requestedObject
        }
    }
}

fun Map<String, Any>.toTemplateContext(): DeepTemplateContext = DeepTemplateContext(this)

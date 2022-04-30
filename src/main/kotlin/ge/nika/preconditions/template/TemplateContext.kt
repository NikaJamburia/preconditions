package ge.nika.preconditions.template

class TemplateContext(
    private val values: Map<String, Any>
) {

    fun findValue(dottedQuery: DottedQuery): Any {

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

fun Map<String, Any>.toTemplateContext(): TemplateContext = TemplateContext(this)

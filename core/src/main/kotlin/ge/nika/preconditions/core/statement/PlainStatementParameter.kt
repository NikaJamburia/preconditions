package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.exceptions.parsingError
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.toDottedQuery
import ge.nika.preconditions.core.utils.isNumber
import ge.nika.preconditions.core.utils.isTemplate
import ge.nika.preconditions.core.utils.removeAll

internal class PlainStatementParameter(
    private val stringValue: String,
    private val indexOffset: Int,
    private val templateContext: TemplateContext
) {
    fun value(): Any? {
        return if (stringValue.startsWith("'") && stringValue.endsWith("'")) {
            stringValue.removeAll("'")
        } else if (stringValue.isNumber()) {
            stringValue.toDouble()
        } else if (stringValue.isTemplate()) {
            findTemplateValue()
        } else if (stringValue == "null") {
            null
        }
        else {
            parsingError(
                message = "Unknown type of parameter $stringValue!",
                startPosition = indexOffset,
                endPosition = indexOffset + stringValue.lastIndex,
            )
        }
    }

    private fun findTemplateValue(): Any {
        val dottedQuery = stringValue
            .removeAll("{")
            .removeAll("}")
            .toDottedQuery()
        return try {
            templateContext.findValue(dottedQuery)
        } catch (e: Exception) {
            parsingError(
                message = e.message ?: "Could not parse template!",
                startPosition = indexOffset + 1,
                endPosition = indexOffset + stringValue.lastIndex - 1
            )
        }
    }
}
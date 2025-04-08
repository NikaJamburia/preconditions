package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.exceptions.parsingError
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.templateEnd
import ge.nika.preconditions.core.api.template.templateStart
import ge.nika.preconditions.core.api.template.toDottedQuery
import ge.nika.preconditions.core.utils.isNumber
import ge.nika.preconditions.core.utils.isTemplate
import ge.nika.preconditions.core.utils.removeAll
import ge.nika.preconditions.core.utils.representsString

internal class PlainStatementParameter(
    private val stringValue: String,
    private val indexOffset: Int,
    private val templateContext: TemplateContext
) {
    fun value(): Any? {
        return when {
            stringValue.representsString() -> stringValue.removeAll("'")
            stringValue.isNumber() -> stringValue.toDouble()
            stringValue.isTemplate() -> findTemplateValue()
            stringValue == "true" -> true
            stringValue == "false" -> false
            stringValue == "null" -> null
            else -> {
                parsingError(
                    message = "Unknown type of parameter $stringValue!",
                    startPosition = indexOffset,
                    endPosition = indexOffset + stringValue.lastIndex,
                )
            }
        }
    }

    private fun findTemplateValue(): Any {
        val dottedQuery = stringValue
            .removeAll(templateStart)
            .removeAll(templateEnd)
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
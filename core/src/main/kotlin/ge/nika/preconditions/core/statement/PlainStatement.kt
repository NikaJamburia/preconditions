package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.exceptions.parsingError
import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.Statement
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.toTemplateContext
import ge.nika.preconditions.core.utils.Metadata
import ge.nika.preconditions.core.utils.removeAll

internal class PlainStatement(
    private val string: String,
    private val templateContext: TemplateContext = mapOf<String, Any>().toTemplateContext()
): Statement {

    override fun describePrecondition(): PreconditionDescription {
        val parts = string
            .trim()
            .removeAll("\n")
            .split(" ")

        if (parts.size != 3) {
            val startPosition = if (parts.size < 3) {
                0
            } else {
                parts.startIndexOfElement(3)
            }
            parsingError(
                message = "Statement contain must consist of 3 parts separated by spaces!",
                startPosition = startPosition,
                endPosition = string.lastIndex
            )
        }

        val firstParameter = PlainStatementParameter(
            stringValue = parts[0],
            indexOffset = parts.startIndexOfElement(0),
            templateContext = templateContext,
        ).value()
        val secondParameter = PlainStatementParameter(
            stringValue = parts[2],
            indexOffset = parts.startIndexOfElement(2),
            templateContext = templateContext
        ).value()
        val preconditionName = parts[1]

        return PreconditionDescription(
            listOf(firstParameter, secondParameter),
            preconditionName
        ).withMetadataParam(Metadata.OFFSET, string.length)
    }

    private fun List<String>.startIndexOfElement(index: Int): Int =
        if (index == 0) {
            0
        } else {
            take(index).fold(0) { acc, s -> acc + s.length } + index
        }
}

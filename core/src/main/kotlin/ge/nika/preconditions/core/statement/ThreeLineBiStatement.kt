package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.exceptions.parsingError
import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.Statement
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.toTemplateContext
import ge.nika.preconditions.core.statement.OffsetStatement.Companion.withOffset


internal class ThreeLineBiStatement(
    private val statementText: String,
    private val templateContext: TemplateContext = mapOf<String, Any>().toTemplateContext()
): Statement {

    override fun describePrecondition(): PreconditionDescription {
        val lines = statementText.lines()
            .filter(String::isNotBlank)
            .map(String::trim)

        if (lines.size == 1) {
            return PlainStatement(statementText, templateContext).describePrecondition()
        }

        check(lines.size == 3) {
            parsingError(
                message = "ThreeLineBiStatement should consist of 3 lines!",
                startPosition = 0,
                endPosition = statementText.lastIndex,
            )
        }

        return PreconditionDescription(
            parameters = listOf(
                lines[0].toStatement().describePrecondition(),
                lines[2].toStatement().describePrecondition()
            ),
            preconditionName = lines[1]
        )

    }

    private fun String.toStatement(): Statement =
        PlainStatement(this, templateContext).withOffset(statementText.indexOf(this))
}
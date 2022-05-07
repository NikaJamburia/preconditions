package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.Statement
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.toTemplateContext


internal class ComplexStatement(
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
            "Invalid complex statement: $statementText"
        }

        return PreconditionDescription(
            listOf(
                PlainStatement(lines[0], templateContext).describePrecondition(),
                PlainStatement(lines[2], templateContext).describePrecondition()
            ),
            lines[1]
        )

    }
}
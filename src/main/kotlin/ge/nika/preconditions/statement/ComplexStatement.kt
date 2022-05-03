package ge.nika.preconditions.statement

import ge.nika.preconditions.template.TemplateContext
import ge.nika.preconditions.template.toTemplateContext


class ComplexStatement(
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
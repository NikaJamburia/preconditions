package ge.nika.preconditions.statement

import ge.nika.preconditions.template.TemplateContext
import ge.nika.preconditions.template.toTemplateContext
import ge.nika.preconditions.utils.removeAll

class MixedStatement(
    private val statementText: String,
    private val templateContext: TemplateContext = mapOf<String, Any>().toTemplateContext()
): Statement {

    override fun describePrecondition(): PreconditionDescription {
        return if (statementText.contains("(")) {
            val composingStatementStrings = BracedStatement(statementText).getFirstLevelSubstrings()

            PreconditionDescription(
                parameters = composingStatementStrings.map { MixedStatement(it, templateContext).describePrecondition() },
                preconditionName = composingStatementStrings.fold(statementText) { acc, statementString ->
                    acc.removeAll(statementString)
                }.removeAll("(")
                    .removeAll(")")
                    .trim()
            )

        } else {
            ComplexStatement(statementText, templateContext).describePrecondition()
        }
    }
}
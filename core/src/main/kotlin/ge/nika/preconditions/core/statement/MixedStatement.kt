package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.Statement
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.toTemplateContext
import ge.nika.preconditions.core.statement.OffsetStatement.Companion.withOffset
import ge.nika.preconditions.core.utils.removeAll

internal class MixedStatement(
    private val statementText: String,
    private val templateContext: TemplateContext = mapOf<String, Any>().toTemplateContext()
): Statement {

    override fun describePrecondition(): PreconditionDescription {
        return if (statementText.contains("(")) {
            val composingStatementStrings = BracedStatement(statementText).getFirstLevelSubstrings()

            PreconditionDescription(
                parameters = composingStatementStrings.map {
                    MixedStatement(it, templateContext).withOffset(statementText.indexOf(it)).describePrecondition()
                },
                preconditionName = composingStatementStrings.fold(statementText) { acc, statementString ->
                    acc.removeAll(statementString)
                }.removeAll("(")
                    .removeAll(")")
                    .trim()
            )

        } else {
            ThreeLineBiStatement(statementText, templateContext).describePrecondition()
        }
    }
}
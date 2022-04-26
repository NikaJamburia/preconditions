package ge.nika.preconditions.statement

import ge.nika.preconditions.utils.removeAll

class MixedStatement(
    private val statementText: String,
    private val templateValues: Map<String, Any> = mapOf()
): Statement {

    override fun describePrecondition(): PreconditionDescription {
        return if (statementText.contains("(")) {
            val composingStatementStrings = BracedStatement(statementText).getFirstLevelSubstrings()

            PreconditionDescription(
                parameters = composingStatementStrings.map { MixedStatement(it, templateValues).describePrecondition() },
                preconditionName = composingStatementStrings.fold(statementText) { acc, statementString ->
                    acc.removeAll(statementString)
                }.removeAll("(")
                    .removeAll(")")
                    .trim()
            )

        } else {
            ComplexStatement(statementText, templateValues).describePrecondition()
        }
    }
}
package ge.nika.preconditions.statement


class ComplexStatement(
    private val statementText: String,
    private val templateParameters: Map<String, Any> = mapOf()
): Statement {

    override fun describePrecondition(): PreconditionDescription {
        val lines = statementText.lines()
            .filter(String::isNotBlank)
            .map(String::trim)

        if (lines.size == 1) {
            return PlainStatement(statementText, templateParameters).describePrecondition()
        }

        check(lines.size == 3) {
            "Invalid complex statement: $statementText"
        }

        return PreconditionDescription(
            listOf(
                PlainStatement(lines[0], templateParameters).describePrecondition(),
                PlainStatement(lines[2], templateParameters).describePrecondition()
            ),
            lines[1]
        )

    }
}
package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.exceptions.StatementParsingException
import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.Statement

internal class OffsetStatement(
    private val indexOffset: Int,
    private val statement: Statement,
) : Statement {

    companion object {
        internal fun Statement.withOffset(offset: Int): OffsetStatement =
            OffsetStatement(offset, this)
    }

    override fun describePrecondition(): PreconditionDescription {
        try {
            return statement.describePrecondition()
                .withMetadataParam("offset", indexOffset)
        } catch (e: StatementParsingException) {
            throw StatementParsingException(
                e.message,
                indexOffset + e.startPosition,
                indexOffset + e.endPosition
            )
        }
    }
}
package ge.nika.preconditions.statement

interface Statement {
    fun describePrecondition(): PreconditionDescription
}
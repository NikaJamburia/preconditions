package ge.nika.preconditions.core.api.precondition

interface Statement {
    fun describePrecondition(): PreconditionDescription
}
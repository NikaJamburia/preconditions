package ge.nika.preconditions.statement

data class PreconditionDescription(
    val parameters: List<Any?>,
    val preconditionName: String
)
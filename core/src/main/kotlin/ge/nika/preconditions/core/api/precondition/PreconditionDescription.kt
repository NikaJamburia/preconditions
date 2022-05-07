package ge.nika.preconditions.core.api.precondition

data class PreconditionDescription(
    val parameters: List<Any?>,
    val preconditionName: String
)
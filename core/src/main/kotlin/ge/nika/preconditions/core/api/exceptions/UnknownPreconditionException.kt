package ge.nika.preconditions.core.api.exceptions

data class UnknownPreconditionException(
    val preconditionName: String,
) : RuntimeException() {
    override val message: String
        get() = "Precondition translator not present for <$preconditionName>"
}

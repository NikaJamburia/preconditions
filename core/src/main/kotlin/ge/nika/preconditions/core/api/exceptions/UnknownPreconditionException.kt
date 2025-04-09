package ge.nika.preconditions.core.api.exceptions

data class UnknownPreconditionException(
    val preconditionName: String,
) : PreconditionsException() {
    override fun data(): PreconditionsExceptionData = PreconditionsExceptionData(
        type = this::class.simpleName!!,
        message = message,
        indexRange = RangeData(0, 0),
        additionalData = mapOf("preconditionName" to preconditionName),
    )

    override val message: String
        get() = "Precondition translator not present for <$preconditionName>"
}

package ge.nika.preconditions.core.api.exceptions


data class StatementParsingException(
    override val message: String,
    val startPosition: Int,
    val endPosition: Int,
) : PreconditionsException() {
    override fun data(): PreconditionsExceptionData = PreconditionsExceptionData(
        type = this::class.simpleName!!,
        message = message,
        indexRange = RangeData(startPosition, endPosition),
        additionalData = emptyMap(),
    )
}

fun parsingError(
    message: String,
    startPosition: Int = 0,
    endPosition: Int = startPosition,
) {
    throw StatementParsingException(
        message = message,
        startPosition = startPosition,
        endPosition = endPosition,
    )
}

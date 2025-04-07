package ge.nika.preconditions.core.api.exceptions


data class StatementParsingException(
    override val message: String,
    val startPosition: Int,
    val endPosition: Int,
) : RuntimeException()

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

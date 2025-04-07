package ge.nika.preconditions.core

import ge.nika.preconditions.core.api.exceptions.StatementParsingException
import io.kotest.matchers.shouldBe

fun assertParsingError(
    startPosition: Int,
    endPosition: Int,
    block: () -> Unit,
): StatementParsingException {
    try {
        block()
    } catch (e: Exception) {
        return when (e) {
            is StatementParsingException -> {
                e.startPosition shouldBe startPosition
                e.endPosition shouldBe endPosition
                e
            }
            else -> {
                throw AssertionError("Expected an exception of ${StatementParsingException::javaClass} to be thrown, but was ${e::javaClass}")
            }
        }
    }
    throw AssertionError("Expected an exception of ${StatementParsingException::javaClass} to be thrown, but was nothing was thrown")
}
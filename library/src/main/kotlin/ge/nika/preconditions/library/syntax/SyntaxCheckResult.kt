package ge.nika.preconditions.library.syntax

import ge.nika.preconditions.core.api.exceptions.PreconditionsExceptionData

data class SyntaxCheckResult(
    val isSuccess: Boolean,
    val exceptions: List<PreconditionsExceptionData>,
) {
    companion object {
        fun success(): SyntaxCheckResult = SyntaxCheckResult(true, emptyList())

        fun fail(exception: List<PreconditionsExceptionData>): SyntaxCheckResult = SyntaxCheckResult(false, exception)

        fun ofExceptions(exceptions: List<PreconditionsExceptionData>): SyntaxCheckResult =
            if (exceptions.isEmpty()) {
                success()
            } else {
                fail(exceptions)
            }
    }
}

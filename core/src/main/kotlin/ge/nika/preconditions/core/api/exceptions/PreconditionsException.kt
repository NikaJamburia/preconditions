package ge.nika.preconditions.core.api.exceptions

abstract class PreconditionsException: RuntimeException() {
    abstract fun data(): PreconditionsExceptionData
}
package ge.nika.preconditions.core.api.precondition

fun interface PreconditionTranslator {
    fun translate(description: PreconditionDescription): Precondition
}
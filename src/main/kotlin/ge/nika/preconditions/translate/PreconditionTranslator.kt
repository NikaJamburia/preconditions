package ge.nika.preconditions.translate

import ge.nika.preconditions.precondition.Precondition
import ge.nika.preconditions.statement.PreconditionDescription

fun interface PreconditionTranslator {
    fun translate(description: PreconditionDescription): Precondition
}
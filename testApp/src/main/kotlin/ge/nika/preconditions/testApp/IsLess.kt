package ge.nika.preconditions.testApp

import ge.nika.preconditions.core.api.precondition.Precondition
import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator

class IsLess(
    private val firstNubmer: Number,
    private val secondNumber: Number,
): Precondition {
    override fun asBoolean(): Boolean =
        firstNubmer.toDouble() < secondNumber.toDouble()
}

class IsLessTranslator : PreconditionTranslator {
    override fun translate(description: PreconditionDescription): Precondition =
        IsLess(
            description.parameters[0] as Number,
            description.parameters[1] as Number
        )
}
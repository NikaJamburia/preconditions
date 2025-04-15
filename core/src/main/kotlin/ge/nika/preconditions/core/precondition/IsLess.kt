package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition

internal class IsLess(
    private val firstNumber: Number,
    private val secondNumber: Number,
): Precondition {
    override fun asBoolean(): Boolean =
        firstNumber.toDouble() < secondNumber.toDouble()
}
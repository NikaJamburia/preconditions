package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition

internal class Is(
    private val firstValue: Any?,
    private val secondValue: Any?
): Precondition {

    override fun asBoolean(): Boolean {
        return firstValue == secondValue
    }

}
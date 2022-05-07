package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition

internal class IsGreater(
    private val first: Number,
    private val second: Number
): Precondition {

    override fun asBoolean(): Boolean {
        return first.toDouble() > second.toDouble()
    }
}
package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition

internal class Or(
    private val firstPrecondition: Precondition,
    private val secondPrecondition: Precondition,
): Precondition {

    override fun asBoolean(): Boolean {
        return firstPrecondition.asBoolean() || secondPrecondition.asBoolean()
    }
}
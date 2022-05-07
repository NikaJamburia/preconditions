package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition

internal class Negated(
    private val precondition: Precondition
) : Precondition {
    override fun asBoolean(): Boolean {
        return !precondition.asBoolean()
    }
}
package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition

internal class Or private constructor(
    private val firstParamToBoolean: () -> Boolean,
    private val secondParamToBoolean: () -> Boolean,
): Precondition {

    companion object {
        fun ofPreconditions(firstPrecondition: Precondition, secondPrecondition: Precondition): Or = Or(
            firstPrecondition::asBoolean,
            secondPrecondition::asBoolean
        )

        fun ofBooleans(firstBoolean: Boolean, secondBoolean: Boolean): Or = Or({ firstBoolean }, { secondBoolean })

        fun Precondition.or(other: Precondition) = ofPreconditions(this, other)
    }

    override fun asBoolean(): Boolean =
        firstParamToBoolean() || secondParamToBoolean()
}
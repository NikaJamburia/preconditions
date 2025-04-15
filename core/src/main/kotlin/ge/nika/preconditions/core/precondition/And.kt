package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition

internal class And private constructor(
    private val firstParamToBoolean: () -> Boolean,
    private val secondParamToBoolean: () -> Boolean,
) : Precondition {

    companion object {
        fun ofPreconditions(firstPrecondition: Precondition, secondPrecondition: Precondition): And = And(
            firstPrecondition::asBoolean,
            secondPrecondition::asBoolean
        )

        fun ofBooleans(firstBoolean: Boolean, secondBoolean: Boolean): And = And({ firstBoolean }, { secondBoolean })
    }

    override fun asBoolean(): Boolean {
        return firstParamToBoolean() && secondParamToBoolean()
    }
}
package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.precondition.Precondition

class TestPrecondition(private val value: Boolean): Precondition {
    override fun asBoolean(): Boolean {
        return value
    }
}
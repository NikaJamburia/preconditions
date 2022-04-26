package ge.nika.preconditions.precondition

class Negated(
    private val precondition: Precondition
) : Precondition {
    override fun asBoolean(): Boolean {
        return !precondition.asBoolean()
    }
}
package ge.nika.preconditions.precondition

class And(
    private val firstPrecondition: Precondition,
    private val secondPrecondition: Precondition,
) : Precondition {

    override fun asBoolean(): Boolean {
        return firstPrecondition.asBoolean() && secondPrecondition.asBoolean()
    }
}
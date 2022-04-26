package ge.nika.preconditions.precondition

class Or(
    private val firstPrecondition: Precondition,
    private val secondPrecondition: Precondition,
): Precondition {

    override fun asBoolean(): Boolean {
        return firstPrecondition.asBoolean() || secondPrecondition.asBoolean()
    }
}
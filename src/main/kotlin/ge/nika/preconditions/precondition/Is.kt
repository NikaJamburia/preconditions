package ge.nika.preconditions.precondition

class Is(
    private val firstValue: Any?,
    private val secondValue: Any?
): Precondition {

    override fun asBoolean(): Boolean {
        return firstValue == secondValue
    }

}
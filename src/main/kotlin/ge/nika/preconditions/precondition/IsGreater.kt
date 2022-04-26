package ge.nika.preconditions.precondition

class IsGreater(
    private val first: Number,
    private val second: Number
): Precondition {

    override fun asBoolean(): Boolean {
        return first.toDouble() > second.toDouble()
    }
}
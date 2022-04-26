package ge.nika.preconditions.statement

import ge.nika.preconditions.utils.isNumber
import ge.nika.preconditions.utils.isTemplate
import ge.nika.preconditions.utils.removeAll

class PlainStatement(
    private val string: String,
    private val templateParameters: Map<String, Any> = mapOf()
): Statement {

    override fun describePrecondition(): PreconditionDescription {
        val parts = string
            .trim()
            .removeAll("\n")
            .split(" ")

        check(parts.size == 3) { "Invalid plain statement: <$string>" }

        val firstParameter = PlainStatementFragment(parts[0], templateParameters).value()
        val secondParameter = PlainStatementFragment(parts[2], templateParameters).value()
        val preconditionName = parts[1]

        return PreconditionDescription(
            listOf(firstParameter, secondParameter),
            preconditionName
        )
    }

    private fun partToParameter(part: String): Any? {
        return if (part.contains("'")) {
            part.removeAll("'")
        } else if (part.isNumber()) {
            part.toDouble()
        } else if (part.isTemplate()) {
            val templateParamName = part
                .removeAll("{")
                .removeAll("}")
            templateParameters[templateParamName] ?: error("{parameter $templateParamName not found")
        } else if (part == "null") {
            null
        }
        else {
            error("Unknown type of parameter $part")
        }
    }

}

class PlainStatementFragment(
    private val stringValue: String,
    private val templateParameters: Map<String, Any> = mapOf()
) {
    fun value(): Any? {
        return if (stringValue.contains("'")) {
            stringValue.removeAll("'")
        } else if (stringValue.isNumber()) {
            stringValue.toDouble()
        } else if (stringValue.isTemplate()) {
            val templateParamName = stringValue
                .removeAll("{")
                .removeAll("}")
            templateParameters[templateParamName] ?: error("parameter $templateParamName not found")
        } else if (stringValue == "null") {
            null
        }
        else {
            error("Unknown type of parameter $stringValue")
        }
    }
}


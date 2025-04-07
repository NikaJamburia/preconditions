package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.exceptions.parsingError

internal class BracedStatement(
    private val value: String
) {

    init {
        check(value.contains("(")) {
            parsingError(
                message = "Braced statement must contain at least 1 '(' symbol!",
                startPosition = 0,
                endPosition = value.lastIndex,
            )
        }

        validateBracesBalance()
    }

    fun getFirstLevelSubstrings(): List<String> {
        var openedBraces = 0
        var currentOpenIndex = 0
        val statements = mutableListOf<String>()

        val chars = value.toCharArray()
        (chars.indices).forEach { index ->
            val currentChar = chars[index]
            if (currentChar == '(') {
                openedBraces++
                if(openedBraces == 1) {
                    currentOpenIndex = index
                }
            }
            if (currentChar == ')') {
                openedBraces--
                if(openedBraces == 0) {
                    statements.add(value.substring(currentOpenIndex..index))
                }
            }
        }

        return statements.map {
            it.substring(1..it.length - 2)
        }
    }

    private fun validateBracesBalance() {
        val balance = value.toCharArray().foldIndexed(0) { i, accumulated, char ->
            val newValue = when (char) {
                '(' -> { accumulated + 1 }
                ')' -> { accumulated - 1 }
                else -> { accumulated }
            }
            if (newValue < 0) {
                parsingError(
                    message = "A closing brace does not have a corresponding opening brace!",
                    startPosition = i,
                )
            }
            newValue
        }
        if (balance > 0) {
            parsingError(
                message = "An opening brace does not have a corresponding closing brace!",
                startPosition = value.indexOf("("),
            )
        }
    }
}
package ge.nika.preconditions.core.statement

internal class BracedStatement(
    private val value: String
) {

    init {
        check(value.contains("(")) {
            "Invalid statement: $value"
        }

        check(bracesBalance() == 0) {
            "Invalid statement: $value"
        }
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

    private fun bracesBalance(): Int {
        return value.toCharArray().fold(0) { accumulated, char ->
            when (char) {
                '(' -> { accumulated + 1 }
                ')' -> { accumulated - 1 }
                else -> { accumulated }
            }
        }
    }
}
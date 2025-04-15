package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.assertParsingError
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BracedStatementTest {

    @Test
    fun `should return list of strings contained in first level braces`() {
        val statement = """
            (
                (
                    ragaca
                ) OR (
                    ragaca 2
                )
            ) AND (
                ragaca 3
            )
        """.trimIndent()

        val substrings = BracedStatement(statement).getFirstLevelSubstrings()
        substrings.size shouldBe 2
        substrings[0].trimIndent() shouldBe """
            (
                ragaca
            ) OR (
                ragaca 2
            )
        """.trimIndent()

        substrings[1].trimIndent() shouldBe "ragaca 3"
    }


    @Test
    fun `should work when there are words before first brace`() {
        val string = "aa(b)((c))"

        val substrings = BracedStatement(string).getFirstLevelSubstrings()

        substrings.size shouldBe 2
        substrings[0] shouldBe "b"
        substrings[1] shouldBe "(c)"
    }

    @Test
    fun `should not accept string without braces`() {
        val string = "aaaaa"

        assertParsingError(0, 4) {
            BracedStatement(string)
        }.message shouldBe "Braced statement must contain at least 1 '(' symbol!"
    }

    @Test
    fun `should not accept string with unclosed braces`() {
        val string = "aaa(aa(aaa)"

        assertParsingError(3, 3) {
            BracedStatement(string)
        }.message shouldBe "An opening brace does not have a corresponding closing brace!"
    }

    @Test
    fun `should throw error if braces balance goes below 0`() {
        val string = "(aa))(aaa)"

        assertParsingError(4, 4) {
            BracedStatement(string)
        }.message shouldBe "A closing brace does not have a corresponding opening brace!"
    }
}
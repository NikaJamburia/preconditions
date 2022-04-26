package ge.nika.preconditions.statement

import io.kotest.matchers.shouldBe
import org.junit.Test
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
    fun `should not accept string without braces`() {
        val string = "aaaaa"

        assertThrows<IllegalStateException>("Invalid statement: $string") {
            BracedStatement(string)
        }
    }

    @Test
    fun `should not accept string with unclosed braces`() {
        val string = "aaa(aa(aaa)"

        assertThrows<IllegalStateException>("Invalid statement: $string") {
            BracedStatement(string)
        }
    }

    @Test
    fun `should not accept string with unopened braces`() {
        val string = "aaa(aa(aaa))))"

        assertThrows<IllegalStateException>("Invalid statement: $string") {
            BracedStatement(string)
        }
    }

    @Test
    fun `should work when there are words before first brace`() {
        val string = "aa(b)((c))"

        val substrings = BracedStatement(string).getFirstLevelSubstrings()

        substrings.size shouldBe 2
        substrings[0] shouldBe "b"
        substrings[1] shouldBe "(c)"
    }
}
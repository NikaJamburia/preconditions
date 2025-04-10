package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.exceptions.RangeData
import ge.nika.preconditions.library.syntax.validateDottedQueryTemplatesInText
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.Test

class ValidateDottedQueryTemplatesInTextTest {

    @Test
    fun `collects correqt dotted queries and exceptions`() {
        val text = """
            {correct} == {still.correct}
            AND
            {in correct} == {}
        """.trimIndent()

        val result = validateDottedQueryTemplatesInText(text)

        result.correctQueries.size shouldBe 2
        result.correctQueries.any { it.originalString == "correct" } shouldBe true
        result.correctQueries.any { it.originalString == "still.correct" } shouldBe true

        result.exceptions.size shouldBe 2

        result.exceptions.first().asClue {
            it.indexRange shouldBe RangeData(34, 43)
            it.message shouldBe "Template error: DottedQuery can't contain white spaces!"
        }

        result.exceptions.last().asClue {
            it.indexRange shouldBe RangeData(49, 50)
            it.message shouldBe "Template error: DottedQuery can't be empty!"
        }
    }
}
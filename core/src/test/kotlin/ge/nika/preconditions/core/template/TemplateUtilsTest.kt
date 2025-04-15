package ge.nika.preconditions.core.template

import ge.nika.preconditions.core.api.template.findTemplateVariables
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class TemplateUtilsTest {

    @Test
    fun `correctly extracts template values from string`() {
        val str = """
            Hello {user.fullname.first}!
            Your credit on {executionDate} is {user.credit}.
        """.trimIndent()

        val result = str.findTemplateVariables().toList()

        result.size shouldBe 3
        result.first { it.value == "user.fullname.first"}.indexRange shouldBe 7..25
        result.first { it.value == "executionDate"}.indexRange shouldBe 45..57
        result.first { it.value == "user.credit"}.indexRange shouldBe 64..74

    }
}
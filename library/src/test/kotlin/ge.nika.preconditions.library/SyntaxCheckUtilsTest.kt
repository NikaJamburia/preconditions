package ge.nika.preconditions.library

import ge.nika.preconditions.library.syntax.replaceTemplateVariables
import io.kotest.matchers.shouldBe
import org.junit.Test

class SyntaxCheckUtilsTest {

    @Test
    fun `should be able to replace all template variables with a test variable`() {
        val str = """
            Hello {user.fullname.first}!
            Your credit on {executionDate} is {user.credit}.
        """.trimIndent()

        val result = str.replaceTemplateVariables()

        result shouldBe """
            Hello {test}!
            Your credit on {test} is {test}.
        """.trimIndent()
    }
}
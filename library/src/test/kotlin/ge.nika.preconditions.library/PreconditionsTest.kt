package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.config.CorePreconditions
import ge.nika.preconditions.core.api.exceptions.UnknownPreconditionException
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator
import ge.nika.preconditions.library.config.PreconditionsConfig
import ge.nika.preconditions.library.config.configurePreconditions
import ge.nika.preconditions.library.config.customModule
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class PreconditionsTest {

    private val preconditionsConfig: PreconditionsConfig = configurePreconditions {
        registerModule { CorePreconditions.withoutAliases() }
        registerModule {
            customModule("custom") {
                "TEST_TRUE" translatedBy PreconditionTranslator { TestPrecondition(true) }
                "TEST_TRUE_ALIAS" aliasFor "TEST_TRUE"
            }
        }
        registerPrecondition("TEST_FALSE") { TestPrecondition(false) }
    }

    private val subject = Preconditions(preconditionsConfig)

    @Test
    fun `evaluates given core and custom preconditions and aliases`() {
        subject.evaluate("'a' IS 'b'") shouldBe false
        subject.evaluate("1 GREATER_THEN 0") shouldBe true
        subject.evaluate("'a' TEST_TRUE 'b'") shouldBe true
        subject.evaluate("'a' TEST_TRUE_ALIAS 'b'") shouldBe true
        subject.evaluate("'a' TEST_FALSE 'b'") shouldBe false
    }

    @Test
    fun `throws exception when precondition is not defined in config`() {
        val exception = assertThrows<UnknownPreconditionException> {
            Preconditions(preconditionsConfig).evaluate("'a' SOMETHING 'b'") shouldBe false
        }
        exception.message shouldBe "Precondition translator not present for <SOMETHING>"
    }

    @Test
    fun `returns success if syntax check is successfull`() {
        val result = subject.checkSyntax("'a' IS 'b'")
        result.isSuccess shouldBe true
        result.exceptions.size shouldBe 0
    }

    @Test
    fun `returns errors in template variables with respective indeces`() {
        val str = """
            {user.full name.first} IS 'Nika'
        """.trimIndent()

        val result = subject.checkSyntax(str)

        result.isSuccess shouldBe false
        result.exceptions.size shouldBe 1

        result.exceptions[0].asClue {
            it.message shouldBe "Template error: DottedQuery can't contain white spaces!"
            it.indexRange shouldBe 1..20
        }
    }

    @Test
    fun `returns error from evaluation and template variables together`() {
        val str = """
            {user.full name.first} SOMETHING 'Nika'
        """.trimIndent()

        val result = subject.checkSyntax(str)

        result.isSuccess shouldBe false
        result.exceptions.size shouldBe 2

        result.exceptions[0].asClue {
            it.type shouldBe "StatementParsingException"
            it.message shouldBe "Template error: DottedQuery can't contain white spaces!"
            it.indexRange shouldBe 1..20
        }

        result.exceptions[1].asClue {
            it.type shouldBe "UnknownPreconditionException"
            it.message shouldBe "Precondition translator not present for <SOMETHING>"
            it.indexRange shouldBe 0..0
            it.additionalData["preconditionName"] shouldBe "SOMETHING"
        }
    }
}
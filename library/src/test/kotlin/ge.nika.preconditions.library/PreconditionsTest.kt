package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.corePreconditions
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class PreconditionsTest {

    private val preconditionsConfig: PreconditionsConfig = configurePreconditions {
        registerModule { corePreconditions() }
        registerModule {
            customModule {
                name = "custom"
                "TEST_TRUE" translatedBy PreconditionTranslator { TestPrecondition(true) }
            }
        }
        registerPrecondition("TEST_FALSE") { TestPrecondition(false) }
    }

    @Test
    fun `evaluates given core and custom preconditions`() {

        val preconditions = Preconditions(preconditionsConfig)

        preconditions.evaluate("'a' IS 'b'") shouldBe false
        preconditions.evaluate("1 GREATER_THEN 0") shouldBe true
        preconditions.evaluate("'a' TEST_TRUE 'b'") shouldBe true
        preconditions.evaluate("'a' TEST_FALSE 'b'") shouldBe false

    }

    @Test
    fun `throws exception when precondition is not defined in config`() {

        val exception = assertThrows<IllegalStateException> {
            Preconditions(preconditionsConfig).evaluate("'a' SOMETHING 'b'") shouldBe false
        }
        exception.message shouldBe "Precondition translator not present for <SOMETHING>"
    }
}
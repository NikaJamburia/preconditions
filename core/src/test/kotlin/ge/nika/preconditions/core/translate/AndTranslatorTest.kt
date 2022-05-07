package ge.nika.preconditions.core.translate

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.andTranslator
import ge.nika.preconditions.core.precondition.And
import ge.nika.preconditions.core.precondition.Is
import ge.nika.preconditions.core.precondition.IsGreater
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test

class AndTranslatorTest {

    @Test
    fun `translates given description to And precondition`() {
        val description = PreconditionDescription(
            parameters = listOf(Is(1, 1), IsGreater(1, 2)),
            preconditionName = "AND"
        )

        val precondition = andTranslator.translate(description)

        (precondition is And) shouldBe true
        precondition.asBoolean() shouldBe false
    }

    @Test
    fun `throws exception when any of the parameters isn't a precondition`() {
        val description = PreconditionDescription(
            parameters = listOf(1, Is(1, 2)),
            preconditionName = "AND"
        )
        val exception = shouldThrow<IllegalStateException> { andTranslator.translate(description) }
        exception.message shouldBe "Both parameters of AND precondition should be preconditions"
    }

    @Test
    fun `throws exception when parameter count is not 2`() {
        val exception = shouldThrow<IllegalStateException> {
            andTranslator.translate(PreconditionDescription(listOf(Is(1, 2), Is(1, 2), Is(1, 2)),"AND"))
        }
        exception.message shouldBe "AND precondition must have 2 parameters. 3 provided"
    }
}
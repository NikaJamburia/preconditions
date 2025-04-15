package ge.nika.preconditions.core.translate

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.andTranslator
import ge.nika.preconditions.core.precondition.And
import ge.nika.preconditions.core.precondition.Is
import ge.nika.preconditions.core.precondition.IsGreater
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class AndTranslatorTest {

    @Test
    fun `translates given description with precondition parameters to And precondition`() {
        val description = PreconditionDescription(
            parameters = listOf(Is(1, 1), IsGreater(1, 2)),
            preconditionName = "AND"
        )

        val precondition = andTranslator.translate(description)

        (precondition is And) shouldBe true
        precondition.asBoolean() shouldBe false
    }

    @Test
    fun `translates given description with boolean parameters to And precondition`() {
        val description = PreconditionDescription(
            parameters = listOf(true, true),
            preconditionName = "AND"
        )

        val precondition = andTranslator.translate(description)

        (precondition is And) shouldBe true
        precondition.asBoolean() shouldBe true

        andTranslator.translate(PreconditionDescription(
            listOf(true, false),"AND"
        )).asBoolean() shouldBe false
    }

    @Test
    fun `throws exception when parameter count is not 2`() {
        val exception = shouldThrow<IllegalStateException> {
            andTranslator.translate(PreconditionDescription(listOf(Is(1, 2), Is(1, 2), Is(1, 2)),"AND"))
        }
        exception.message shouldBe "AND precondition must have 2 parameters. 3 provided"
    }

    @Test
    fun `throws exception when both of parameters arent preconditions nor booleans`() {
        shouldThrow<IllegalStateException> {
            andTranslator.translate(
                PreconditionDescription(listOf(1, Is(1, 2)),"AND")
            )
        }.message shouldBe "Type of both parameters of AND precondition must be either precondition or boolean"

        shouldThrow<IllegalStateException> {
            andTranslator.translate(
                PreconditionDescription(listOf(true, Is(1, 2)),"AND")
            )
        }.message shouldBe "Type of both parameters of AND precondition must be either precondition or boolean"

        shouldThrow<IllegalStateException> {
            andTranslator.translate(
                PreconditionDescription(listOf(true, 1),"AND")
            )
        }.message shouldBe "Type of both parameters of AND precondition must be either precondition or boolean"
    }
}
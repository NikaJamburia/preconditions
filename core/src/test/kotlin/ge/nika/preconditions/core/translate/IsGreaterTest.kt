package ge.nika.preconditions.core.translate

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.greaterThenTranslator
import ge.nika.preconditions.core.precondition.IsGreater
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test

class IsGreaterTest {

    @Test
    fun `correctly translates given description to is greater precondition`() {

        val description = PreconditionDescription(
            parameters = listOf(1, 2),
            preconditionName = ">"
        )

        val precondition = greaterThenTranslator.translate(description)

        (precondition is IsGreater) shouldBe true
        precondition.asBoolean() shouldBe false
    }

    @Test
    fun `works for different kinds of numbers`() {

        val intAndLong = PreconditionDescription(
            parameters = listOf(3, 2L),
            preconditionName = ">"
        )
        greaterThenTranslator.translate(intAndLong).asClue {
            (it is IsGreater) shouldBe true
            it.asBoolean() shouldBe true
        }

        val longAndBigDecimal = PreconditionDescription(
            parameters = listOf(3L, "5.00".toBigDecimal()),
            preconditionName = ">"
        )
        greaterThenTranslator.translate(longAndBigDecimal).asClue {
            (it is IsGreater) shouldBe true
            it.asBoolean() shouldBe false
        }

        val doubleAndBigDecimal = PreconditionDescription(
            parameters = listOf(3.123, "3.10".toBigDecimal()),
            preconditionName = ">"
        )
        greaterThenTranslator.translate(doubleAndBigDecimal).asClue {
            (it is IsGreater) shouldBe true
            it.asBoolean() shouldBe true
        }
    }

    @Test
    fun `throws exception number of given parameters is not a number`() {
        val exception = shouldThrow<IllegalStateException> {
            greaterThenTranslator.translate(
                PreconditionDescription(
                listOf(1L, "2"), ">")
            )
        }
        exception.message shouldBe "Both parameters of > precondition should be numbers"
    }

    @Test
    fun `throws exception number of given parameters is not 2`() {
        val exception = shouldThrow<IllegalStateException> {
            greaterThenTranslator.translate(PreconditionDescription(listOf(), ">"))
        }
        exception.message shouldBe "> precondition must have 2 parameters. 0 provided"
    }
}
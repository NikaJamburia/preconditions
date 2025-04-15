package ge.nika.preconditions.core.translate

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.isLessTranslator
import ge.nika.preconditions.core.precondition.IsLess
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test

class IsLessTranslatorTest {
    
    @Test
    fun `correctly translates given description to is less precondition`() {

        val description = PreconditionDescription(
            parameters = listOf(1, 2),
            preconditionName = "<"
        )

        val precondition = isLessTranslator.translate(description)

        (precondition is IsLess) shouldBe true
        precondition.asBoolean() shouldBe true
    }

    @Test
    fun `works for different kinds of numbers`() {

        val intAndLong = PreconditionDescription(
            parameters = listOf(3, 2L),
            preconditionName = "<"
        )
        isLessTranslator.translate(intAndLong).asClue {
            (it is IsLess) shouldBe true
            it.asBoolean() shouldBe false
        }

        val longAndBigDecimal = PreconditionDescription(
            parameters = listOf(3L, "5.00".toBigDecimal()),
            preconditionName = "<"
        )
        isLessTranslator.translate(longAndBigDecimal).asClue {
            (it is IsLess) shouldBe true
            it.asBoolean() shouldBe true
        }

        val doubleAndBigDecimal = PreconditionDescription(
            parameters = listOf(3.123, "3.10".toBigDecimal()),
            preconditionName = "<"
        )
        isLessTranslator.translate(doubleAndBigDecimal).asClue {
            (it is IsLess) shouldBe true
            it.asBoolean() shouldBe false
        }
    }

    @Test
    fun `throws exception when any of given parameters is not a number`() {
        val exception = shouldThrow<IllegalStateException> {
            isLessTranslator.translate(
                PreconditionDescription(
                    listOf(1L, null), "<")
            )
        }
        exception.message shouldBe "Both parameters of < precondition should be numbers"
    }

    @Test
    fun `throws exception when number of given parameters is not 2`() {
        val exception = shouldThrow<IllegalStateException> {
            isLessTranslator.translate(PreconditionDescription(listOf(), "<"))
        }
        exception.message shouldBe "< precondition must have 2 parameters. 0 provided"
    }
}
package ge.nika.preconditions.core.translate

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.isGreaterOrEqualTranslator
import ge.nika.preconditions.core.precondition.Or
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class IsGreaterOrEqualTranslatorTest {

    @ParameterizedTest
    @CsvSource(value = [
        "1, >=, 2, false",
        "2, >=, 2, true",
        "3, >=, 2, true",
    ])
    fun `correctly translates given description to OR precondition`(
        n1: Int,
        precondition: String,
        n2: Int,
        result: Boolean
    ) {
        isGreaterOrEqualTranslator.translate(
            PreconditionDescription(
                parameters = listOf(n1, n2),
                preconditionName = precondition
            )
        ).asClue {
            (it is Or) shouldBe true
            it.asBoolean() shouldBe result
        }
    }


    @Test
    fun `throws exception when any of given parameters is not a number`() {
        val exception = shouldThrow<IllegalStateException> {
            isGreaterOrEqualTranslator.translate(
                PreconditionDescription(
                    listOf(1L, null), ">=")
            )
        }
        exception.message shouldBe "Both parameters of >= precondition should be numbers"
    }

    @Test
    fun `throws exception when number of given parameters is not 2`() {
        val exception = shouldThrow<IllegalStateException> {
            isGreaterOrEqualTranslator.translate(PreconditionDescription(listOf(), ">="))
        }
        exception.message shouldBe ">= precondition must have 2 parameters. 0 provided"
    }
}
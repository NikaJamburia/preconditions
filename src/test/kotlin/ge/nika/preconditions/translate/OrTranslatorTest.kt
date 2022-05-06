package ge.nika.preconditions.translate

import ge.nika.preconditions.precondition.Is
import ge.nika.preconditions.precondition.IsGreater
import ge.nika.preconditions.precondition.Or
import ge.nika.preconditions.statement.PreconditionDescription
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test

class OrTranslatorTest {

    @Test
    fun `translates given description to or precondition`() {
        val description = PreconditionDescription(
            parameters = listOf(Is(1, 1), IsGreater(1, 2)),
            preconditionName = "OR"
        )

        val precondition = orTranslator.translate(description)

        (precondition is Or) shouldBe true
        precondition.asBoolean() shouldBe true
    }

    @Test
    fun `throws exception if any of the parameters is not a precondition`() {
        val description = PreconditionDescription(
            parameters = listOf(1, Is(1, 2)),
            preconditionName = "OR"
        )
        val exception = shouldThrow<IllegalStateException> { orTranslator.translate(description) }
        exception.message shouldBe "Both parameters of OR precondition should be preconditions"
    }

    @Test
    fun `throws exception if parameter number is not 2`() {
        val exception = shouldThrow<IllegalStateException> {
            orTranslator.translate(PreconditionDescription(listOf(Is(1, 2)),"OR"))
        }
        exception.message shouldBe "OR precondition must have 2 parameters. 1 provided"
    }
}
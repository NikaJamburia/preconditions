package ge.nika.preconditions.core.translate

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.orTranslator
import ge.nika.preconditions.core.precondition.Is
import ge.nika.preconditions.core.precondition.IsGreater
import ge.nika.preconditions.core.precondition.Or
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class OrTranslatorTest {

    @Test
    fun `translates given description with precondition parameters to OR precondition`() {
        val description = PreconditionDescription(
            parameters = listOf(Is(1, 1), IsGreater(1, 2)),
            preconditionName = "OR"
        )

        val precondition = orTranslator.translate(description)

        (precondition is Or) shouldBe true
        precondition.asBoolean() shouldBe true
    }

    @Test
    fun `translates given description with boolean parameters to OR precondition`() {
        val description = PreconditionDescription(
            parameters = listOf(true, false),
            preconditionName = "OR"
        )

        val precondition = orTranslator.translate(description)

        (precondition is Or) shouldBe true
        precondition.asBoolean() shouldBe true
    }

    @Test
    fun `throws exception if parameter number is not 2`() {
        val exception = shouldThrow<IllegalStateException> {
            orTranslator.translate(PreconditionDescription(listOf(Is(1, 2)),"OR"))
        }
        exception.message shouldBe "OR precondition must have 2 parameters. 1 provided"
    }

    @Test
    fun `throws exception if all of the parameters is not a precondition nor boolean`() {
        val description = PreconditionDescription(
            parameters = listOf(1, Is(1, 2)),
            preconditionName = "OR"
        )
        val exception = shouldThrow<IllegalStateException> { orTranslator.translate(description) }
        exception.message shouldBe "Type of both parameters of OR precondition must be either precondition or boolean"
    }
}
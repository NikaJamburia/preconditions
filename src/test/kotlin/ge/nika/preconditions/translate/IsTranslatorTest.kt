package ge.nika.preconditions.translate

import ge.nika.preconditions.precondition.Is
import ge.nika.preconditions.statement.PreconditionDescription
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test

class IsTranslatorTest {

    @Test
    fun `correctly translates given description to is precondition`() {

        val description = PreconditionDescription(
            parameters = listOf(1, 2),
            preconditionName = "IS"
        )

        val precondition = isTranslator.translate(description)

        (precondition is Is) shouldBe true
        precondition.asBoolean() shouldBe false
    }

    @Test
    fun `throws exception number of given parameters is not 2`() {
        val exception = shouldThrow<IllegalStateException> {
            isTranslator.translate(PreconditionDescription(listOf(), "IS"))
        }
        exception.message shouldBe "IS precondition must have 2 parameters. 0 provided"
    }
}
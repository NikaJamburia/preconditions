package ge.nika.preconditions.service

import ge.nika.preconditions.precondition.Negated
import ge.nika.preconditions.precondition.Precondition
import ge.nika.preconditions.translate.PreconditionTranslator
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertTrue

class StatementTranslationServiceTest {

    private val service = StatementTranslationService(mapOf(
        "IS" to PreconditionTranslator { desc ->
            TestIs(desc.parameters[0], desc.parameters[1])
         },
        ">" to PreconditionTranslator { desc ->
            TestIsGreaterFloat(desc.parameters[0] as Number, desc.parameters[1] as Number)
        },
        "OR" to PreconditionTranslator { desc ->
            TestOr(
                desc.parameters[0] as Precondition,
                desc.parameters[1] as Precondition,
            )
        }
    ))

    @Test
    fun `should translate given plain statement text to a precondition with simple parameters`() {
        val statement = "'a' IS 'b'"

        val precondition = service.translate(statement)
        (precondition as TestIs).asClue {
            it.first shouldBe "a"
            it.second shouldBe "b"
        }
    }

    @Test
    fun `should be able to return different preconditions based on precondition name in given text`() {
        val isStatement = "'nika' IS 'bika'"
        val isGreaterStatement = "5 > 4"

        (service.translate(isStatement) as TestIs).asClue {
            it.first shouldBe "nika"
            it.second shouldBe "bika"
        }

        (service.translate(isGreaterStatement) as TestIsGreaterFloat).asClue {
            it.first shouldBe 5.0
            it.second shouldBe 4.0
        }
    }

    @Test
    fun `should throw exception if precondition translator is not present`() {
        val statement = "'nika' EQUALS 'bika'"

        val exception = assertThrows<IllegalStateException> { service.translate(statement) }
        exception.message shouldBe "Precondition translator not present for <EQUALS>"
    }

    @Test
    fun `should insert template parameters into precondition`() {
        val statement = "{custom} IS 'b'"

        val precondition = service.translate(statement, mapOf("custom" to "nika"))
        (precondition as TestIs).asClue {
            it.first shouldBe "nika"
            it.second shouldBe "b"
        }
    }

    @Test
    fun `should negate precondition if it starts with exclamation mark`() {
        val statement = "'a' !IS 'b'"

        val precondition = service.translate(statement)
        assertTrue { precondition is Negated }
        precondition.asBoolean() shouldBe true
    }

    @Test
    fun `should throw exception if precondition text is invalid`() {
        val statement = "'nika' IS 'bika' aaaaa"

        val exception = assertThrows<IllegalStateException> { service.translate(statement) }
        exception.message shouldBe "Invalid plain statement: <'nika' IS 'bika' aaaaa>"
    }

    @Test
    fun `should be able to translate preconditions with other preconditions as parameters`() {

        val statement = """
            'a' IS 'a'
            OR
            1 > 2
        """.trimIndent()

        val precondition = service.translate(statement)

        (precondition as TestOr).asClue {
            (it.first as TestIs).asClue { firstPrecondition ->
                firstPrecondition.first shouldBe "a"
                firstPrecondition.second shouldBe "a"
            }
            (it.second as TestIsGreaterFloat).asClue { secondPrecondition ->
                secondPrecondition.first shouldBe 1.0
                secondPrecondition.second shouldBe 2.0
            }
        }

    }

}

class TestIs(
    val first: Any?,
    val second: Any?
): Precondition {

    override fun asBoolean(): Boolean {
        return first == second
    }

}

class TestIsGreaterFloat(
    val first: Number,
    val second: Number
): Precondition {
    override fun asBoolean(): Boolean {
        return first.toFloat() > second.toFloat()
    }
}

class TestOr(
    val first: Precondition,
    val second: Precondition
) : Precondition {
    override fun asBoolean(): Boolean {
        return first.asBoolean() || second.asBoolean()
    }
}
package ge.nika.preconditions.core.service

import ge.nika.preconditions.core.api.exceptions.UnknownPreconditionException
import ge.nika.preconditions.core.api.precondition.Precondition
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator
import ge.nika.preconditions.core.api.service.StatementTranslationService
import ge.nika.preconditions.core.precondition.Negated
import ge.nika.preconditions.core.api.template.toTemplateContext
import ge.nika.preconditions.core.assertParsingError
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
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

        val exception = assertThrows<UnknownPreconditionException> { service.translate(statement) }
        exception.message shouldBe "Precondition translator not present for <EQUALS>"
    }

    @Test
    fun `should insert template parameters into precondition`() {
        val statement = "{custom} IS {user.firstName}"
        val templateContext = mapOf(
            "custom" to "nika",
            "user" to object {
                val firstName = "nika"
                val lastName = "jamburia"
            }
        ).toTemplateContext()

        val precondition = service.translate(statement, templateContext)
        (precondition as TestIs).asClue {
            it.first shouldBe "nika"
            it.second shouldBe "nika"
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

        val exception = assertParsingError(17, 21) { service.translate(statement) }
        exception.message shouldBe "Statement contain must consist of 3 parts separated by spaces!"
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

    @Test
    fun `should be able to translate big preconditions`() {
        val statement = """
            ((
                1 IS 1
                OR
                2 !IS 2
            ) OR (
                5 > 6
                OR
                'nika' IS 'bika'
            )) OR (
                1 IS 1
            )
        """.trimIndent()

        val precondition = service.translate(statement)

        (precondition as TestOr).asClue { root ->

            (root.first as TestOr).asClue { firstPrecondition ->
                (firstPrecondition.first as TestOr).asClue { firstMixed ->
                    (firstMixed.first as TestIs).asClue { firstPlain ->
                        firstPlain.first shouldBe 1.0
                        firstPlain.second shouldBe 1.0
                    }
                    firstMixed.second.asClue { secondPlain ->
                        (secondPlain is Negated) shouldBe true
                        secondPlain.asBoolean() shouldBe false
                    }
                }

                (firstPrecondition.second as TestOr).asClue { secondMixed ->
                    (secondMixed.first as TestIsGreaterFloat).asClue { firstPlain ->
                        firstPlain.first shouldBe 5.0
                        firstPlain.second shouldBe 6.0
                    }
                    (secondMixed.second as TestIs).asClue { secondPlain ->
                        secondPlain.first shouldBe "nika"
                        secondPlain.second shouldBe "bika"
                    }
                }
            }

            (root.second as TestIs).asClue { secondPrecondition ->
                secondPrecondition.first shouldBe 1.0
                secondPrecondition.second shouldBe 1.0
            }
        }

        precondition.asBoolean() shouldBe true
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
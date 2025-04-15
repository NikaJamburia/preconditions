package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.template.toTemplateContext
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MixedStatementTest {

    @Test
    fun `should describe precondition for 2 complex statements`() {

        val statement = """
            (
                1 IS 1
                AND
                2 !IS 2
            ) OR (
                5 > 6
                OR
                'nika' IS 'bika'
            )
        """.trimIndent()

        val description = MixedStatement(statement).describePrecondition()
        assertEquals(description.preconditionName, "OR")

        (description.parameters[0] as PreconditionDescription).asClue { firstStatement ->
            firstStatement.preconditionName shouldBe "AND"
            (firstStatement.parameters[0] as PreconditionDescription).asClue { firstParameter ->
                firstParameter.preconditionName shouldBe "IS"
                firstParameter.parameters[0] shouldBe 1.0
                firstParameter.parameters[1] shouldBe 1.0
            }
            (firstStatement.parameters[1] as PreconditionDescription).asClue { secondParameter ->
                secondParameter.preconditionName shouldBe "!IS"
                secondParameter.parameters[0] shouldBe 2.0
                secondParameter.parameters[1] shouldBe 2.0
            }
        }

        (description.parameters[1] as PreconditionDescription).asClue { secondStatement ->
            secondStatement.preconditionName shouldBe "OR"
            (secondStatement.parameters[0] as PreconditionDescription).asClue { firstParameter ->
                firstParameter.preconditionName shouldBe ">"
                firstParameter.parameters[0] shouldBe 5.0
                firstParameter.parameters[1] shouldBe 6.0
            }
            (secondStatement.parameters[1] as PreconditionDescription).asClue { secondParameter ->
                secondParameter.preconditionName shouldBe "IS"
                secondParameter.parameters[0] shouldBe "nika"
                secondParameter.parameters[1] shouldBe "bika"
            }
        }
    }

    @Test
    fun `should describe precondition for 2 independant statements`() {
        val statement = """
            ((
                1 IS 1
                AND
                2 !IS 2
            ) OR (
                5 > 6
                OR
                'nika' IS 'bika'
            )) AND (
                5 > 4
            )
        """.trimIndent()

        val description = MixedStatement(statement).describePrecondition()

        description.preconditionName shouldBe  "AND"
        description.parameters.size shouldBe 2

        (description.parameters[0] as PreconditionDescription).asClue { firstParameter ->
            firstParameter.preconditionName shouldBe "OR"
            (firstParameter.parameters[0] is PreconditionDescription) shouldBe true
            (firstParameter.parameters[1] is PreconditionDescription) shouldBe true
        }

        (description.parameters[1] as PreconditionDescription).asClue { secondParameter ->
            secondParameter.preconditionName shouldBe ">"
            secondParameter.parameters[0] shouldBe 5.0
            secondParameter.parameters[1] shouldBe 4.0
        }
    }

    @Test
    fun `should be able to replace template values`() {
        val statement = """
            ((
                {custom} IS 1
                AND
                2 !IS 2
            ) OR (
                5 > 6
                OR
                'nika' IS 'bika'
            )) AND (
                5 > 4
            )
        """.trimIndent()

        val description = MixedStatement(statement, mapOf("custom" to "customValue").toTemplateContext()).describePrecondition()

        (((description.parameters[0] as PreconditionDescription)
            .parameters[0] as PreconditionDescription)
            .parameters[0] as PreconditionDescription)
            .parameters[0] shouldBe "customValue"
    }

}
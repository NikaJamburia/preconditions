package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.template.toTemplateContext
import ge.nika.preconditions.core.assertParsingError
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ThreeLineBiStatementTest {

    @Test
    fun `should describe precondition as precondition of 2 different preconditions`() {
        val statement = """
            'a' IS 'a'
            AND
            'b' !IS 'b'
        """.trimIndent()

        val description = ThreeLineBiStatement(statement).describePrecondition()
        assertEquals(description.preconditionName, "AND")

        val firstParameter = description.parameters[0]
        assertTrue(firstParameter is PreconditionDescription)
        assertEquals(firstParameter.preconditionName, "IS")
        assertEquals(firstParameter.parameters[0], "a")
        assertEquals(firstParameter.parameters[1], "a")

        val secondParameter = description.parameters[1]
        assertTrue(secondParameter is PreconditionDescription)
        assertEquals(secondParameter.preconditionName, "!IS")
        assertEquals(secondParameter.parameters[0], "b")
        assertEquals(secondParameter.parameters[1], "b")
    }

    @Test
    fun `should ignore blank lines and weird spaces`() {
        val statement = """
            
             'a' IS 'a'
            
            AND     
            
                 'b' !IS 'b'
            
        """.trimIndent()

        val description = ThreeLineBiStatement(statement).describePrecondition()
        assertEquals(description.preconditionName, "AND")

        val firstParameter = description.parameters[0]
        assertTrue(firstParameter is PreconditionDescription)
        assertEquals(firstParameter.preconditionName, "IS")
        assertEquals(firstParameter.parameters[0], "a")
        assertEquals(firstParameter.parameters[1], "a")

        val secondParameter = description.parameters[1]
        assertTrue(secondParameter is PreconditionDescription)
        assertEquals(secondParameter.preconditionName, "!IS")
        assertEquals(secondParameter.parameters[0], "b")
        assertEquals(secondParameter.parameters[1], "b")
    }

    @Test
    fun `should process template parameters`() {

        val statement = """
            {custom} IS 'a'
            AND
            'b' !IS 'b'
        """.trimIndent()

        val description = ThreeLineBiStatement(statement, mapOf("custom" to "nika").toTemplateContext()).describePrecondition()
        assertEquals(description.preconditionName, "AND")

        val firstParameter = description.parameters[0]
        assertTrue(firstParameter is PreconditionDescription)
        assertEquals(firstParameter.preconditionName, "IS")
        assertEquals(firstParameter.parameters[0], "nika")
        assertEquals(firstParameter.parameters[1], "a")
    }

    @Test
    fun `should be able to describe plain statement`() {
        val plainStatement = "'a' IS 'b'"
        val description = ThreeLineBiStatement(plainStatement).describePrecondition()

        description.parameters[0] shouldBe "a"
        description.parameters[1] shouldBe "b"
        description.preconditionName shouldBe "IS"
    }

    @Test
    fun `should throw error when statement is  invalid`() {
        val invalidStatement = """
            'a IS 'a'
            AND
            'b' !IS 'b'
            OR
        """.trimIndent()
        assertParsingError(0, invalidStatement.lastIndex) {
            ThreeLineBiStatement(invalidStatement).describePrecondition()
        }.message shouldBe "ThreeLineBiStatement should consist of 3 lines!"
    }

    @Test
    fun `should correctly handle errors from plain statement`() {
        val invalidStatement = """
            'a' IS 'a'
            AND
            'b' !IS b'
        """.trimIndent()
        assertParsingError(invalidStatement.indexOf(" b'")+1, invalidStatement.lastIndex) {
            ThreeLineBiStatement(invalidStatement).describePrecondition()
        }.message shouldBe "Unknown type of parameter b'!"
    }

}
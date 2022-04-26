package ge.nika.preconditions.statement

import io.kotest.matchers.shouldBe
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ComplexStatementTest {

    @Test
    fun `should describe precondition as precondition of 2 different preconditions`() {
        val statement = """
            'a' IS 'a'
            AND
            'b' !IS 'b'
        """.trimIndent()

        val description = ComplexStatement(statement).describePrecondition()
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

        val description = ComplexStatement(statement).describePrecondition()
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

        val description = ComplexStatement(statement, mapOf("custom" to "nika")).describePrecondition()
        assertEquals(description.preconditionName, "AND")

        val firstParameter = description.parameters[0]
        assertTrue(firstParameter is PreconditionDescription)
        assertEquals(firstParameter.preconditionName, "IS")
        assertEquals(firstParameter.parameters[0], "nika")
        assertEquals(firstParameter.parameters[1], "a")
    }

    @Test
    fun `should throw error when statement is incomplete or invalid`() {
        val incompleteStatement = "'a IS 'a'"
        assertFailsWith<IllegalStateException>("Invalid complex statement: $incompleteStatement") {
            ComplexStatement(incompleteStatement).describePrecondition()
        }

        val invalidStatement = """
            'a IS 'a'
            AND
            'b' !IS 'b'
            OR
        """.trimIndent()
        assertFailsWith<IllegalStateException>("Invalid complex statement: $invalidStatement") {
            ComplexStatement(invalidStatement).describePrecondition()
        }
    }

    @Test
    fun `should be able to describe plain statement`() {
        val plainStatement = "'a' IS 'b'"
        val description = ComplexStatement(plainStatement).describePrecondition()

        description.parameters[0] shouldBe "a"
        description.parameters[1] shouldBe "b"
        description.preconditionName shouldBe "IS"
    }

}
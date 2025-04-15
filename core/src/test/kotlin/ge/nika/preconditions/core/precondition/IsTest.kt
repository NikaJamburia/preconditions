package ge.nika.preconditions.core.precondition

import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsTest {

    @Test
    fun `should evaluate if two strings are equal or not`() {

        val equalPrecondition = Is("nika", "nika")
        assertTrue(equalPrecondition.asBoolean())

        val notEqual = Is("nika", "bika")
        assertFalse(notEqual.asBoolean())
    }

    @Test
    fun `should evaluate if two numbers are equal or not`() {

        assertTrue(Is(1, 1).asBoolean())
        assertTrue(Is(1.1, 1.1).asBoolean())
        assertFalse(Is(1, 2).asBoolean())
        assertFalse(Is(1.1, 2).asBoolean())
    }

    @Test
    fun `should evaluate if two data objects are equal or not`() {

        assertTrue(Is(TestDto("a", 1), TestDto("a", 1)).asBoolean())
        assertFalse(Is(TestDto("a", 1), TestDto("a", 2)).asBoolean())
    }

    @Test
    fun `should return false when given uncomparable types`() {

        assertFalse(Is("nika", 1).asBoolean())
    }

    @Test
    fun `should correctly compare values with null`() {
        assertFalse(Is("nika", null).asBoolean())
        assertTrue(Is(null, null).asBoolean())
    }

    data class TestDto(
        val value1: String,
        val value2: Int
    )
}
package ge.nika.preconditions.precondition

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IsGreaterTest {

    @Test
    fun `should return true is first number is greater`() {
        assertTrue(IsGreater(2, 1).asBoolean())
        assertTrue(IsGreater(1.200, 1.199).asBoolean())
    }

    @Test
    fun `should return false if second value is greater or same as first`() {
        assertFalse(IsGreater(1, 1).asBoolean())
        assertFalse(IsGreater(0, 1).asBoolean())
    }
}
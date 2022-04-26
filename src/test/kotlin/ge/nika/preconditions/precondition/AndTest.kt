package ge.nika.preconditions.precondition

import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class AndTest {

    @Test
    fun `returns true if both preconditions are true`() {
        val precondition1 = mockk<Precondition> { every { asBoolean() } returns true }
        val precondition2 = mockk<Precondition> { every { asBoolean() } returns true }
        assertTrue(And(precondition1, precondition2).asBoolean())
    }

    @Test
    fun `returns false if both preconditions are false`() {
        val precondition1 = mockk<Precondition> { every { asBoolean() } returns false }
        val precondition2 = mockk<Precondition> { every { asBoolean() } returns false }
        assertFalse(And(precondition1, precondition2).asBoolean())
    }

    @Test
    fun `returns false any of the preconditions is false`() {
        val precondition1 = mockk<Precondition> { every { asBoolean() } returns true }
        val precondition2 = mockk<Precondition> { every { asBoolean() } returns false }
        assertFalse(And(precondition1, precondition2).asBoolean())
    }
}
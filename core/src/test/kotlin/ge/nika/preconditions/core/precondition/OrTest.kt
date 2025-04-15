package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OrTest {

    @Test
    fun `returns true if any of the preconditions is true`() {

        val truePrecondition = mockk<Precondition> { every { asBoolean() } returns true }
        val falsePrecondition = mockk<Precondition> { every { asBoolean() } returns false }

        assertTrue(Or.ofPreconditions(truePrecondition, falsePrecondition).asBoolean())
    }

    @Test
    fun `returns true if both preconditions are true`() {
        val precondition1 = mockk<Precondition> { every { asBoolean() } returns true }
        val precondition2  = mockk<Precondition> { every { asBoolean() } returns true }

        assertTrue(Or.ofPreconditions(precondition1, precondition2).asBoolean())
    }

    @Test
    fun `returns false if both preconditions are false`() {
        val precondition1 = mockk<Precondition> { every { asBoolean() } returns false }
        val precondition2  = mockk<Precondition> { every { asBoolean() } returns false }

        assertFalse(Or.ofPreconditions(precondition1, precondition2).asBoolean())
    }
}
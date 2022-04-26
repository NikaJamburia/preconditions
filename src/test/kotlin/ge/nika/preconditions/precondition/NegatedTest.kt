package ge.nika.preconditions.precondition

import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NegatedTest {

    @Test
    fun `should negate given precondition`() {

        val truePrecondition = mockk<Precondition> {
            every { asBoolean() } returns true
        }

        val falsePrecondition = mockk<Precondition> {
            every { asBoolean() } returns false
        }

        assertFalse(Negated(truePrecondition).asBoolean())
        assertTrue(Negated(falsePrecondition).asBoolean())
    }
}
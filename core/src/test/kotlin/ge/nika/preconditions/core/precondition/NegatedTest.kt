package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
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
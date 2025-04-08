package ge.nika.preconditions.core.precondition

import ge.nika.preconditions.core.api.precondition.Precondition
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class AndTest {

    @Test
    fun `ofPreconditions returns true if both preconditions are true`() {
        val precondition1 = mockk<Precondition> { every { asBoolean() } returns true }
        val precondition2 = mockk<Precondition> { every { asBoolean() } returns true }
        assertTrue(And.ofPreconditions(precondition1, precondition2).asBoolean())
    }

    @Test
    fun `ofPreconditions returns false if both preconditions are false`() {
        val precondition1 = mockk<Precondition> { every { asBoolean() } returns false }
        val precondition2 = mockk<Precondition> { every { asBoolean() } returns false }
        assertFalse(And.ofPreconditions(precondition1, precondition2).asBoolean())
    }

    @Test
    fun `ofPreconditions returns false any of the preconditions is false`() {
        val precondition1 = mockk<Precondition> { every { asBoolean() } returns true }
        val precondition2 = mockk<Precondition> { every { asBoolean() } returns false }
        assertFalse(And.ofPreconditions(precondition1, precondition2).asBoolean())
    }

    @Test
    fun `ofBooleans returns true if both booleans are true`() {
        And.ofBooleans(true, true).asBoolean() shouldBe true
    }

    @Test
    fun `ofBooleans returns false if both booleans are false`() {
        And.ofBooleans(false, false).asBoolean() shouldBe false
    }

    @Test
    fun `ofBooleans returns false any of the booleans is false`() {
        And.ofBooleans(true, false).asBoolean() shouldBe false
    }
}
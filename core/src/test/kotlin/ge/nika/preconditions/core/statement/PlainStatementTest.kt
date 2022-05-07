package ge.nika.preconditions.core.statement

import ge.nika.preconditions.core.api.template.toTemplateContext
import io.kotest.matchers.shouldBe
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PlainStatementTest {

    @Test
    fun `should correctly describe given text precondition`() {

        val text = "'a' IS 1"
        val description = PlainStatement(text).describePrecondition()

        assertEquals(description.parameters.first(), "a")
        assertEquals(description.parameters.last(), 1.0)
        assertEquals(description.preconditionName, "IS")

    }

    @Test
    fun `should correctly describe parametrized precondition with a direct parameter`() {
        val text = "{customNumber} !IS 1"
        val description = PlainStatement(text, mapOf("customNumber" to 10).toTemplateContext()).describePrecondition()

        assertEquals(description.parameters.first(), 10)
        assertEquals(description.parameters.last(), 1.0)
        assertEquals(description.preconditionName, "!IS")

    }

    @Test
    fun `should be able to insert given objects field into template value`() {
        val text = "{user1.name} !IS {user2.name}"
        val params = mapOf(
            "user1" to object {
                val name = "nika"
            },
            "user2" to object {
                val name = "bika"
            }
        ).toTemplateContext()
        val description = PlainStatement(text, params).describePrecondition()
        description.parameters[0] shouldBe "nika"
        description.parameters[1] shouldBe "bika"
        description.preconditionName shouldBe "!IS"
    }

    @Test
    fun `should correctly describe null`() {
        val text = "'something' !IS null"
        val description = PlainStatement(text).describePrecondition()

        assertEquals(description.parameters.first(), "something")
        assertEquals(description.parameters.last(), null)
        assertEquals(description.preconditionName, "!IS")
    }

    @Test
    fun `should throw error if any of parameters is of unknown types`() {
        assertFailsWith<IllegalStateException> ("Unknown type of parameter a") { PlainStatement("a IS 'a'").describePrecondition() }
        assertFailsWith<IllegalStateException> ("Unknown type of parameter b") { PlainStatement("'a' IS b").describePrecondition() }
    }

    @Test
    fun `throws error if statement contains more than 3 fragments`() {
        assertFailsWith<IllegalStateException> ("Invalid plain statement: <'a' IS 'a' AND>") {
            PlainStatement("a IS 'a' AND").describePrecondition()
        }
    }
}
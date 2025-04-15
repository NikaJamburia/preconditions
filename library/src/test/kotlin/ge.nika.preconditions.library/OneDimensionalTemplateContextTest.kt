package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.template.toDottedQuery
import ge.nika.preconditions.library.syntax.OneDimensionalTemplateContext.Companion.constructOneDimensionalTemplateContext
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class OneDimensionalTemplateContextTest {

    @Test
    fun `given dotted queries associates a default value with queries original string`() {
        val queries = listOf(
            "a.b.c.d".toDottedQuery(),
            "user.fullname.first".toDottedQuery(),
            "user.fullname.last".toDottedQuery(),
        )

        val subject = queries.constructOneDimensionalTemplateContext("Test")

        subject.findValue(queries[0]) shouldBe "Test"
        subject.findValue(queries[1]) shouldBe "Test"
        subject.findValue(queries[2]) shouldBe "Test"

        subject.findValue("a.b".toDottedQuery()) shouldNotBe "Test"
    }
}
package ge.nika.preconditions.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.Test

class TemplateContextTest {

    private val templateContext = mapOf(
        "customNumber" to 2,
        "customString" to "nika",
        "person" to object {
            val firstName: String = "Nika"
            val lastName: String = "Jamburia"
            val creditCard = object {
                val number = "1234 5678 9000 0000"
                val ccv = 123
                val bank = object {
                    val name = "tbc"
                }
            }
        }
    ).toTemplateContext()

    @Test
    fun `gives primitive value defined in the context`() {
        templateContext.findValue("customNumber".toDottedQuery()) shouldBe 2
        templateContext.findValue("customString".toDottedQuery()) shouldBe "nika"
    }

    @Test
    fun `throws exception when trying to get not existing value`() {
        val exception = shouldThrow<IllegalStateException> { templateContext.findValue("doesnotexist".toDottedQuery()) }
        exception.message shouldBe "No value provided for template parameter doesnotexist"

        val exception2 = shouldThrow<IllegalStateException> { templateContext.findValue("worker.salary".toDottedQuery()) }
        exception2.message shouldBe "No value provided for template parameter worker"

        val exception3 = shouldThrow<IllegalStateException> { templateContext.findValue("person.salary".toDottedQuery()) }
        exception3.message shouldContain "Field salary is not present on"
    }

    @Test
    fun `returns fields of a given object` () {
        templateContext.findValue("person.firstName".toDottedQuery()) shouldBe "Nika"
        templateContext.findValue("person.lastName".toDottedQuery()) shouldBe "Jamburia"
        templateContext.findValue("person.creditCard.number".toDottedQuery()) shouldBe "1234 5678 9000 0000"
        templateContext.findValue("person.creditCard.ccv".toDottedQuery()) shouldBe 123
        templateContext.findValue("person.creditCard.bank.name".toDottedQuery()) shouldBe "tbc"
    }

}
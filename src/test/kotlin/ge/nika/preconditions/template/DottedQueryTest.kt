package ge.nika.preconditions.template

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.Test

class DottedQueryTest {

    @Test
    fun `correctly identifies requested objects name in any case`() {
        DottedQuery("person").objectName shouldBe "person"
        DottedQuery("person.firstName").objectName shouldBe "person"
        DottedQuery("person.mother.age").objectName shouldBe "person"
        DottedQuery("person.wallet.properties.currency").objectName shouldBe "person"
    }

    @Test
    fun `correctly identifies list of requested fields in any case`() {
        DottedQuery("person").requestedFields shouldHaveSize 0
        DottedQuery("person.firstName").requestedFields shouldContainInOrder listOf("firstName")
        DottedQuery("person.mother.age").requestedFields shouldContainInOrder listOf("mother", "age")
        DottedQuery("person.wallet.properties.currency").requestedFields shouldContainInOrder
                listOf("wallet", "properties", "currency")
    }

    @Test
    fun `should throw exception when given empty string`() {
        val exception = shouldThrow<IllegalStateException> { DottedQuery("") }
        exception.message shouldBe "DottedQuery can't be empty!"
    }

    @Test
    fun `should throw exception when given string has whitespaces`() {
        val exception = shouldThrow<IllegalStateException> { DottedQuery("some thing") }
        exception.message shouldBe "DottedQuery can't contain white spaces!"
    }

    @Test
    fun `should throw exception when given string has line breaks`() {
        val exception = shouldThrow<IllegalStateException> { DottedQuery("""some
            |thing"""".trimMargin()) }
        exception.message shouldBe "DottedQuery can't contain line breaks!"
    }
}
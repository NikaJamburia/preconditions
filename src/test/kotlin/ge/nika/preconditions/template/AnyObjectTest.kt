package ge.nika.preconditions.statement

import ge.nika.preconditions.template.AnyObject
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test

class AnyObjectTest {

    val nika = TestObject(
        name = "nika",
        age = 21,
        personalNumber = 123456
    )

    @Test
    fun `gives value of the public field`() {
        val anyObject = AnyObject(nika)

        anyObject.valueOf("name") shouldBe "nika"
        anyObject.valueOf("age") shouldBe 21
    }

    @Test
    fun `throws exception when trying to get value of non-public field`() {
        val anyObject = AnyObject(nika)

        val exception = shouldThrow<IllegalStateException> {
            anyObject.valueOf("personalNumber")
        }
        exception.message shouldBe "Field personalNumber of TestObject is inaccessible"

    }

    @Test
    fun `throws exception when trying to get field that does not exist`() {
        val anyObject = AnyObject(nika)

        val exception = shouldThrow<IllegalStateException> {
            anyObject.valueOf("firstName")
        }
        exception.message shouldBe "Field firstName is not present on TestObject"
    }

    @Test
    fun `works on anonymous objects`() {
        val something = object {
            val firstName: String = "nika"
            var lastName: String = "jamburia"
            private val age: Int = 21
        }

        val anyObject = AnyObject(something)

        anyObject.valueOf("firstName") shouldBe "nika"
        anyObject.valueOf("lastName") shouldBe "jamburia"

        val exception = shouldThrow<IllegalStateException> { anyObject.valueOf("age") }
        exception.message shouldBe "Field age of ${something::class.java.name} is inaccessible"
    }

    @Test
    fun `throws error on primitives`() {
        val string = AnyObject("nika")
        val exception = shouldThrow<IllegalStateException> { string.valueOf("name") }
        exception.message shouldBe "Field name is not present on String"
    }
}

data class TestObject(
    val name: String,
    val age: Int,
    private val personalNumber: Long
)
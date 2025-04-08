package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator
import ge.nika.preconditions.library.config.CustomModuleBuilder
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class CustomModuleBuilderTest {

    private val dummyDescription = PreconditionDescription(listOf(), "")

    @Test
    fun `adds given translator to the module`() {
        val builder = CustomModuleBuilder()
        with(builder) {
            "TEST" translatedBy PreconditionTranslator { TestPrecondition(true) }
            name = "test"
        }
        val module = builder.build()

        module.translators().size shouldBe 1
        module.translators()["TEST"]!!.translate(dummyDescription).asClue {
            (it is TestPrecondition)
        }
    }

    @Test
    fun `throws exception if translator already exists`() {
        val exception = assertThrows<IllegalStateException> {
            with(CustomModuleBuilder()) {
                "TEST" translatedBy PreconditionTranslator { TestPrecondition(true) }
                "TEST" translatedBy PreconditionTranslator { TestPrecondition(false) }
            }
        }

        exception.message shouldBe "Translator for precondition TEST already defined"
    }

    @Test
    fun `throws exception if module name not specified`() {
        val exception = assertThrows<IllegalStateException> {
            with(CustomModuleBuilder()) {
                "TEST" translatedBy PreconditionTranslator { TestPrecondition(true) }
                build()
            }
        }
        exception.message shouldBe "Name not defined for custom module"
    }
}
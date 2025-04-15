package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator
import ge.nika.preconditions.library.config.CustomModuleBuilder
import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
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

    @Test
    fun `registers alias for a precondition with same translator but different name`() {
        val builder = CustomModuleBuilder()
        with(builder) {
            "TEST" translatedBy PreconditionTranslator { TestPrecondition(true) }
            "TEST2" aliasFor "TEST"
            "TEST3" aliasFor "TEST"
            name = "test"
        }
        val translators = builder.build().translators()

        translators.size shouldBe 3

        translators["TEST"] shouldNotBe null
        translators["TEST2"] shouldBe translators["TEST"]
        translators["TEST3"] shouldBe translators["TEST"]
    }

    @Test
    fun `throws error when trying to register alias that already exists`() {
        val builder = CustomModuleBuilder()
        assertThrows<IllegalStateException> {
            with(builder) {
                name = "test"
                "TEST" translatedBy PreconditionTranslator { TestPrecondition(true) }
                "TEST2" aliasFor "TEST"
                "TEST2" aliasFor "TEST"
            }
        }.message shouldBe "Cannot register alias to module test: TEST2 already defined!"
    }

    @Test
    fun `throws error when trying to register alias for precondition that does not exist`() {
        val builder = CustomModuleBuilder()
        assertThrows<IllegalStateException> {
            with(builder) {
                name = "test"
                "TEST2" aliasFor "TEST"
            }
        }.message shouldBe "Cannot register alias to module test: precondition with name TEST not found!"
    }
}
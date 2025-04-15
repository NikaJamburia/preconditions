package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.config.CorePreconditions
import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator
import ge.nika.preconditions.library.config.PreconditionsConfigBuilder
import ge.nika.preconditions.library.config.customModule
import io.kotest.assertions.asClue
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PreconditionsConfigBuilderTest {

    @Test
    fun `throws exception when modules are conflicting`() {

        val builder = PreconditionsConfigBuilder()
        builder.registerModule { CorePreconditions.withoutAliases() }

        val exception = shouldThrow<IllegalStateException> {
            builder.registerModule { customModule("TEST") {
                "IS" translatedBy PreconditionTranslator { TestPrecondition(true) }
            } }
        }
        exception.message shouldBe "Cant register module <TEST>: Precondition 'IS' already defined in module <Core>."
    }

    @Test
    fun `throws exception when trying to register conflicting single precondition`() {

        val builder = PreconditionsConfigBuilder()
        builder.registerModule { CorePreconditions.withoutAliases() }

        val exception = shouldThrow<IllegalStateException> {
            builder.registerPrecondition("IS") { TestPrecondition(true) }
        }
        exception.message shouldBe "Precondition 'IS' already defined in module <Core>."
    }

    @Test
    fun `throws exception when trying to register two conflicting single preconditions`() {

        val builder = PreconditionsConfigBuilder()
        builder.registerPrecondition("TEST") { TestPrecondition(true) }

        val exception = shouldThrow<IllegalStateException> {
            builder.registerPrecondition("TEST") { TestPrecondition(false) }
        }
        exception.message shouldBe "Precondition 'TEST' already defined in module <TEST>."
    }

    @Test
    fun `adds given module to configuration`() {
        val builder = PreconditionsConfigBuilder()

        val module = customModule("my module") {
            "IS_TRUE" translatedBy PreconditionTranslator { TestPrecondition(true) }
        }
        val module2 = customModule("my module") {
            "IS_FALSE" translatedBy PreconditionTranslator { TestPrecondition(false) }
        }

        builder.registerModule { module }
        builder.registerModule { module2 }

        val config = builder.build()

        config.definedTranslators.size shouldBe 2
        config.definedTranslators["IS_TRUE"]!!.translate(dummyDescription).asClue {
            (it is TestPrecondition) shouldBe true
            it.asBoolean() shouldBe true
        }
        config.definedTranslators["IS_FALSE"]!!.translate(dummyDescription).asClue {
            (it is TestPrecondition) shouldBe true
            it.asBoolean() shouldBe false
        }

    }

    @Test
    fun `adds single precondition to configuration`() {
        val builder = PreconditionsConfigBuilder()

        builder.registerPrecondition("IS_TRUE") { TestPrecondition(true) }
        builder.registerPrecondition("IS_FALSE") { TestPrecondition(false) }

        val config = builder.build()
        config.definedTranslators.size shouldBe 2

        config.definedTranslators["IS_TRUE"]!!.translate(dummyDescription).asClue {
            (it is TestPrecondition) shouldBe true
            it.asBoolean() shouldBe true
        }
        config.definedTranslators["IS_FALSE"]!!.translate(dummyDescription).asClue {
            (it is TestPrecondition) shouldBe true
            it.asBoolean() shouldBe false
        }

    }

    @Test
    fun `adds both modules and single preconditions to config`() {
        val builder = PreconditionsConfigBuilder()

        builder.registerPrecondition("IS_TRUE") { TestPrecondition(true) }
        builder.registerModule { customModule("module") {
            "IS_FALSE" translatedBy PreconditionTranslator { TestPrecondition(false) }
            "IS_TRUE_M" translatedBy PreconditionTranslator { TestPrecondition(true) }
        } }

        val definedTranslators = builder.build().definedTranslators

        definedTranslators.size shouldBe 3
        definedTranslators["IS_TRUE"]!!.translate(dummyDescription).asClue {
            (it is TestPrecondition) shouldBe true
            it.asBoolean() shouldBe true
        }
        definedTranslators["IS_TRUE_M"]!!.translate(dummyDescription).asClue {
            (it is TestPrecondition) shouldBe true
            it.asBoolean() shouldBe true
        }
        definedTranslators["IS_FALSE"]!!.translate(dummyDescription).asClue {
            (it is TestPrecondition) shouldBe true
            it.asBoolean() shouldBe false
        }

    }

    private val dummyDescription = PreconditionDescription(listOf(), "")
}
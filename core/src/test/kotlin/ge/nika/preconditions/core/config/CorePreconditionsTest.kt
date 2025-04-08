package ge.nika.preconditions.core.config

import ge.nika.preconditions.core.api.config.CorePreconditionSyntax
import ge.nika.preconditions.core.api.config.CorePreconditions
import ge.nika.preconditions.core.api.precondition.andTranslator
import ge.nika.preconditions.core.api.precondition.greaterThenTranslator
import ge.nika.preconditions.core.api.precondition.isTranslator
import ge.nika.preconditions.core.api.precondition.orTranslator
import io.kotest.matchers.shouldBe
import org.junit.Test

class CorePreconditionsTest {

    @Test
    fun `withoutAliases method returns module with core translators without any aliases`() {
        val translators = CorePreconditions.withoutAliases().translators()

        translators.size shouldBe 4
        translators[CorePreconditionSyntax.isPrecondition] shouldBe isTranslator
        translators[CorePreconditionSyntax.and] shouldBe andTranslator
        translators[CorePreconditionSyntax.or] shouldBe orTranslator
        translators[CorePreconditionSyntax.greaterThen] shouldBe greaterThenTranslator
    }

    @Test
    fun `withAliases method returns module with core translators and given aliases`() {
        val translators = CorePreconditions.withAliases(
            isP = listOf("==", "is", "SHOULD_BE"),
            and = listOf("&&", "and"),
            or = listOf("||"),
        ).translators()

        translators.size shouldBe 10
        translators[CorePreconditionSyntax.isPrecondition] shouldBe isTranslator
        translators[CorePreconditionSyntax.and] shouldBe andTranslator
        translators[CorePreconditionSyntax.or] shouldBe orTranslator
        translators[CorePreconditionSyntax.greaterThen] shouldBe greaterThenTranslator

        translators["=="] shouldBe isTranslator
        translators["is"] shouldBe isTranslator
        translators["SHOULD_BE"] shouldBe isTranslator

        translators["&&"] shouldBe andTranslator
        translators["and"] shouldBe andTranslator

        translators["||"] shouldBe orTranslator
    }

    @Test
    fun `withAliases ignores given duplicates`() {
        val translators = CorePreconditions.withAliases(
            isP = listOf("==", "IS"),
            and = listOf("&&"),
            or = listOf("&&"),
        ).translators()

        translators.size shouldBe 6
        translators[CorePreconditionSyntax.isPrecondition] shouldBe isTranslator
        translators[CorePreconditionSyntax.and] shouldBe andTranslator
        translators[CorePreconditionSyntax.or] shouldBe orTranslator
        translators[CorePreconditionSyntax.greaterThen] shouldBe greaterThenTranslator
        translators["=="] shouldBe isTranslator
        translators["&&"] shouldBe andTranslator
    }
}
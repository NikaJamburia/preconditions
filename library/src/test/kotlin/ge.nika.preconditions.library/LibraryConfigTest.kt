package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.CorePreconditionSyntax
import ge.nika.preconditions.core.api.basePreconditionTranslators
import ge.nika.preconditions.core.api.corePreconditionTranslators
import ge.nika.preconditions.core.api.service.StatementTranslationService
import org.junit.Test

class LibraryConfigTest {

    @Test
    fun `library can easily be configured and used`() {
        val translatorsConfiguration: TranslatorsConfiguration = translatorConfigBuilder {
            use { corePreconditionTranslators(CorePreconditionSyntax()) }
        }

        val service = StatementTranslationService(
            translators = translatorsConfiguration.definedTranslators
        )
    }
}
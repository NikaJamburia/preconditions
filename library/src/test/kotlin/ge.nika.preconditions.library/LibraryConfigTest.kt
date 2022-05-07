package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.service.StatementTranslationService
import org.junit.Test

class LibraryConfigTest {

    @Test
    fun `library can easily be configured and used`() {
        val translatorsConfiguration = TranslatorsConfiguration()

        val service = StatementTranslationService(
            translators = translatorsConfiguration.definedTranslators
        )
    }
}
package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.precondition.PreconditionTranslator

data class PreconditionsLibraryConfig(
    val translatorsConfiguration: TranslatorsConfiguration

)

data class TranslatorsConfiguration(
    val definedTranslators: Map<String, PreconditionTranslator>
)

class TranslatorsConfigBuilder(

)
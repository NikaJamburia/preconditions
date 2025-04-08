package ge.nika.preconditions.core.api.config

import ge.nika.preconditions.core.api.precondition.*

interface PreconditionsModule {
    val name: String
    fun translators(): Map<String, PreconditionTranslator>
}

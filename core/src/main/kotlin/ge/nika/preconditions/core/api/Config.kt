package ge.nika.preconditions.core.api

import ge.nika.preconditions.core.api.precondition.*

interface PreconditionsModule {
    val name: String
    fun translators(): Map<String, PreconditionTranslator>
}

internal class CorePreconditions(
    private val syntax: CorePreconditionSyntax = CorePreconditionSyntax()
): PreconditionsModule {
    override val name: String = "Core"

    override fun translators(): Map<String, PreconditionTranslator> = mapOf(
        syntax.isPrecondition to isTranslator,
        syntax.and to andTranslator,
        syntax.or to orTranslator,
        syntax.isGreater to isGreaterTranslator,
    )

}

data class CorePreconditionSyntax(
    val isPrecondition: String = "IS",
    val and: String = "AND",
    val or: String = "OR",
    val isGreater: String = "GREATER_THEN",
) {
    init {
        check(setOf(isPrecondition, and, or, isGreater).size == 4) {
            "All precondition names must be unique"
        }
    }
}

fun corePreconditions(syntaxBuilder: (() -> CorePreconditionSyntax)? = null): PreconditionsModule {
    return CorePreconditions(syntaxBuilder?.let { it() } ?: CorePreconditionSyntax())
}

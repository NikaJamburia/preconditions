package ge.nika.preconditions.core.api.config

import ge.nika.preconditions.core.api.precondition.*

class CorePreconditions private constructor(
    private val translators: Map<String, PreconditionTranslator>
): PreconditionsModule {

    companion object {
        fun withAliases(
            isP: List<String> = emptyList(),
            and: List<String> = emptyList(),
            or: List<String> = emptyList(),
            isGreater: List<String> = emptyList(),
            isLess: List<String> = emptyList(),
        ): CorePreconditions{
            return CorePreconditions(
                translators = buildMap {
                    putAll(
                        mapOf(
                            CorePreconditionSyntax.isPrecondition to isTranslator,
                            CorePreconditionSyntax.and to andTranslator,
                            CorePreconditionSyntax.or to orTranslator,
                            CorePreconditionSyntax.isGreater to isGreaterTranslator,
                            CorePreconditionSyntax.isLess to isLessTranslator,
                        )
                    )
                    isP.forEach { putIfAbsent(it, isTranslator) }
                    and.forEach { putIfAbsent(it, andTranslator) }
                    or.forEach { putIfAbsent(it, orTranslator) }
                    isGreater.forEach { putIfAbsent(it, isGreaterTranslator) }
                    isLess.forEach { putIfAbsent(it, isLessTranslator) }
                }
            )
        }

        fun withoutAliases(): CorePreconditions = withAliases()
    }

    override val name: String = "Core"
    override fun translators(): Map<String, PreconditionTranslator> = translators
}

internal object CorePreconditionSyntax {
    val isPrecondition: String = "IS"
    val and: String = "AND"
    val or: String = "OR"
    val isGreater: String = "GREATER_THEN"
    val isLess: String = "LESS_THEN"
}
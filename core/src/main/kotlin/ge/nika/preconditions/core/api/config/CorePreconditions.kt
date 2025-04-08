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
            greaterThen: List<String> = emptyList(),
        ): CorePreconditions{
            return CorePreconditions(
                buildMap {
                    putAll(
                        mapOf(
                            CorePreconditionSyntax.isPrecondition to isTranslator,
                            CorePreconditionSyntax.and to andTranslator,
                            CorePreconditionSyntax.or to orTranslator,
                            CorePreconditionSyntax.greaterThen to greaterThenTranslator,
                        )
                    )
                    isP.forEach { putIfAbsent(it, isTranslator) }
                    and.forEach { putIfAbsent(it, andTranslator) }
                    or.forEach { putIfAbsent(it, orTranslator) }
                    greaterThen.forEach { putIfAbsent(it, greaterThenTranslator) }
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
    val greaterThen: String = "GREATER_THEN"
}
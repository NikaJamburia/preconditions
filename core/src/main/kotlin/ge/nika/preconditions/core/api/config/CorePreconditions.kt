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
            isGreaterOrEqual: List<String> = emptyList(),
            isLess: List<String> = emptyList(),
            isLessOrEqual: List<String> = emptyList(),
        ): CorePreconditions{
            return CorePreconditions(
                translators = buildMap {
                    putAll(
                        mapOf(
                            CorePreconditionSyntax.isPrecondition to isTranslator,
                            CorePreconditionSyntax.and to andTranslator,
                            CorePreconditionSyntax.or to orTranslator,
                            CorePreconditionSyntax.isGreater to isGreaterTranslator,
                            CorePreconditionSyntax.isGreaterOrEqual to isGreaterOrEqualTranslator,
                            CorePreconditionSyntax.isLess to isLessTranslator,
                            CorePreconditionSyntax.isLessOrEqual to isLessOrEqualTranslator,
                        )
                    )
                    isP.forEach { putIfAbsent(it, isTranslator) }
                    and.forEach { putIfAbsent(it, andTranslator) }
                    or.forEach { putIfAbsent(it, orTranslator) }
                    isGreater.forEach { putIfAbsent(it, isGreaterTranslator) }
                    isGreaterOrEqual.forEach { putIfAbsent(it, isGreaterOrEqualTranslator) }
                    isLess.forEach { putIfAbsent(it, isLessTranslator) }
                    isLessOrEqual.forEach { putIfAbsent(it, isLessOrEqualTranslator) }
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
    val isGreater: String = "IS_GREATER"
    val isGreaterOrEqual: String = "GREATER_THEN"
    val isLess: String = "IS_LESS"
    val isLessOrEqual: String = "IS_LESS_OR_EQUAL"
}
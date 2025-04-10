package ge.nika.preconditions.library.syntax

import ge.nika.preconditions.core.api.template.DottedQuery
import ge.nika.preconditions.core.api.template.TemplateContext

internal class OneDimensionalTemplateContext private constructor(
    private val values: Map<String, Any>
): TemplateContext {

    companion object {
        fun List<DottedQuery>.constructOneDimensionalTemplateContext(defaultValue: Any): OneDimensionalTemplateContext =
            OneDimensionalTemplateContext(
                this.associate { it.originalString to defaultValue }
            )
    }

    override fun findValue(dottedQuery: DottedQuery): Any {
        return values[dottedQuery.originalString] ?: Any()
    }

}
package ge.nika.preconditions.core.api.template

import ge.nika.preconditions.core.utils.removeAll

val templateStart = "{"
val templateEnd = "}"
val templateRegex = "\\$templateStart([^$templateEnd]*)\\$templateEnd".toRegex()

fun String.findTemplateVariables(): Sequence<TemplateVariableLookupResult> =
    templateRegex.findAll(this).map {
        TemplateVariableLookupResult(
            it.value.removeAll("{").removeAll("}"),
            it.range.first+1 until it.range.last
        )
    }

data class TemplateVariableLookupResult(
    val value: String,
    val indexRange: IntRange,
)
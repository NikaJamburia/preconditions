package ge.nika.preconditions.library.syntax

import ge.nika.preconditions.core.api.exceptions.PreconditionsExceptionData
import ge.nika.preconditions.core.api.exceptions.StatementParsingException
import ge.nika.preconditions.core.api.template.DottedQuery
import ge.nika.preconditions.core.api.template.findTemplateVariables
import ge.nika.preconditions.core.api.template.toDottedQuery
import kotlin.math.max
import kotlin.math.min

internal fun validateDottedQueryTemplatesInText(text: String): TemplateVariablesValidationResult {
    val correctQueries: MutableList<DottedQuery> = mutableListOf()
    val exceptions: MutableList<PreconditionsExceptionData> = mutableListOf()

    text.findTemplateVariables().forEach {
        try {
            correctQueries.add(it.value.toDottedQuery())
        } catch (e: Exception) {
            exceptions.add(
                StatementParsingException(
                    message = "Template error: ${e.message}",
                    startPosition = min(it.indexRange.first, it.indexRange.last),
                    endPosition = max(it.indexRange.first, it.indexRange.last),
                ).data()
            )
        }
    }

    return TemplateVariablesValidationResult(
        correctQueries = correctQueries.toList(),
        exceptions = exceptions.toList(),
    )
}

data class TemplateVariablesValidationResult(
    val correctQueries: List<DottedQuery>,
    val exceptions: List<PreconditionsExceptionData>,
) {
    val containsErrors: Boolean
        get() = exceptions.isNotEmpty()
}
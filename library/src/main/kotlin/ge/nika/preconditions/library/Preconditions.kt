package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.exceptions.PreconditionsException
import ge.nika.preconditions.core.api.exceptions.PreconditionsExceptionData
import ge.nika.preconditions.core.api.exceptions.StatementParsingException
import ge.nika.preconditions.core.api.service.StatementTranslationService
import ge.nika.preconditions.core.api.template.*
import ge.nika.preconditions.library.config.PreconditionsConfig
import ge.nika.preconditions.library.syntax.*

class Preconditions(
    preconditionsConfig: PreconditionsConfig,
) {

    private val translationService = StatementTranslationService(preconditionsConfig.definedTranslators)

    fun evaluate(
        preconditionText: String,
        templateContext: TemplateContext = mapOf<String, Any>().toTemplateContext()
    ): Boolean =
        translationService.translate(preconditionText, templateContext).asBoolean()

    fun checkSyntax(preconditionText: String): SyntaxCheckResult {
        val exceptions: List<PreconditionsExceptionData> = buildList {
            addAll(preconditionText.findTemplateVariables().validateDottedQueries())
            try {
                evaluate(preconditionText.replaceTemplateVariables(), syntaxCheckTemplateContext)
            } catch (exception: PreconditionsException) {
                add(exception.data())
            }
        }
        return SyntaxCheckResult.ofExceptions(exceptions)
    }

    private fun Sequence<TemplateVariableLookupResult>.validateDottedQueries(): List<PreconditionsExceptionData> {
        return buildList {
            this@validateDottedQueries.forEach {
                try {
                    it.value.toDottedQuery()
                } catch (e: Exception) {
                    add(
                        StatementParsingException(
                            message = "Template error: ${e.message}",
                            startPosition = it.indexRange.first,
                            endPosition = it.indexRange.last,
                        ).data()
                    )
                }
            }
        }.toList()
    }
}
package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.exceptions.PreconditionsException
import ge.nika.preconditions.core.api.service.StatementTranslationService
import ge.nika.preconditions.core.api.template.*
import ge.nika.preconditions.library.config.PreconditionsConfig
import ge.nika.preconditions.library.syntax.*
import ge.nika.preconditions.library.syntax.OneDimensionalTemplateContext.Companion.constructOneDimensionalTemplateContext

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

        val templatesValidationResult = validateDottedQueryTemplatesInText(preconditionText)

        return if(templatesValidationResult.containsErrors) {
            SyntaxCheckResult.ofExceptions(templatesValidationResult.exceptions)
        } else {
            tryEvaluate(
                preconditionText = preconditionText,
                templateContext = templatesValidationResult.correctQueries
                    .constructOneDimensionalTemplateContext("test")
                )
        }
    }

    private fun tryEvaluate(
        preconditionText: String,
        templateContext: OneDimensionalTemplateContext
    ) = try {
        evaluate(preconditionText, templateContext)
        SyntaxCheckResult.success()
    } catch (exception: PreconditionsException) {
        SyntaxCheckResult.ofException(exception.data())
    }
}
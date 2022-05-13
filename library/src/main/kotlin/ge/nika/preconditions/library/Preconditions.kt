package ge.nika.preconditions.library

import ge.nika.preconditions.core.api.service.StatementTranslationService
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.toTemplateContext

class Preconditions(
    preconditionsConfig: PreconditionsConfig,
) {

    private val translationService = StatementTranslationService(preconditionsConfig.definedTranslators)

    fun evaluate(
        preconditionText: String,
        templateContext: TemplateContext = mapOf<String, Any>().toTemplateContext()
    ): Boolean =
        translationService.translate(preconditionText, templateContext).asBoolean()

    fun checkSyntax(preconditionText: String) {
        TODO()
    }
}
package ge.nika.preconditions.core.api.service

import ge.nika.preconditions.core.api.precondition.Precondition
import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.toTemplateContext
import ge.nika.preconditions.core.precondition.Negated
import ge.nika.preconditions.core.statement.MixedStatement
import ge.nika.preconditions.core.utils.removeAll

class StatementTranslationService(
    private val translators: Map<String, PreconditionTranslator>
) {

    fun translate(
        statement: String,
        templateContext: TemplateContext = mapOf<String, Any>().toTemplateContext()
    ): Precondition {
        val description = MixedStatement(statement, templateContext).describePrecondition()
        return translateDescription(description)
    }

    private fun translateDescription(description: PreconditionDescription): Precondition {
        val preconditionName = description.preconditionName

        val withTranslatedParameters = if (description.parameters.any { it is PreconditionDescription }) {
            description.translateParameters()
        } else {
            description
        }

        val precondition = translators[preconditionName.removeAll("!")]
            ?.translate(withTranslatedParameters)
            ?: error("Precondition translator not present for <$preconditionName>")

        return if (preconditionName.startsWith("!")) {
            Negated(precondition)
        } else {
            precondition
        }
    }

    private fun PreconditionDescription.translateParameters(): PreconditionDescription =
        this.copy(parameters = this.parameters.map {
            if(it is PreconditionDescription) {
                translateDescription(it)
            } else {
                it
            }
        })
}
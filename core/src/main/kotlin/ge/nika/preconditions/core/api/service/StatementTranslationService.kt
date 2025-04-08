package ge.nika.preconditions.core.api.service

import ge.nika.preconditions.core.api.exceptions.StatementParsingException
import ge.nika.preconditions.core.api.exceptions.UnknownPreconditionException
import ge.nika.preconditions.core.api.exceptions.parsingError
import ge.nika.preconditions.core.api.precondition.Precondition
import ge.nika.preconditions.core.api.precondition.PreconditionDescription
import ge.nika.preconditions.core.api.precondition.PreconditionTranslator
import ge.nika.preconditions.core.api.template.TemplateContext
import ge.nika.preconditions.core.api.template.toTemplateContext
import ge.nika.preconditions.core.precondition.Negated
import ge.nika.preconditions.core.statement.MixedStatement
import ge.nika.preconditions.core.statement.OffsetStatement.Companion.withOffset
import ge.nika.preconditions.core.utils.Metadata
import ge.nika.preconditions.core.utils.removeAll
import kotlin.jvm.Throws

class StatementTranslationService(
    private val translators: Map<String, PreconditionTranslator>
) {

    fun translate(
        statement: String,
        templateContext: TemplateContext = mapOf<String, Any>().toTemplateContext()
    ): Precondition {
        val description = MixedStatement(statement, templateContext).withOffset(0).describePrecondition()
        return translateDescription(description)
    }

    private fun translateDescription(description: PreconditionDescription): Precondition {
        val preconditionName = description.preconditionName

        val withTranslatedParameters = if (description.parameters.any { it is PreconditionDescription }) {
            description.translateParameters()
        } else {
            description
        }

        val translator = translators[preconditionName.removeAll("!")]
            ?: throw UnknownPreconditionException(preconditionName)

        return try {
            val precondition = translator.translate(withTranslatedParameters)
            if (preconditionName.startsWith("!")) {
                Negated(precondition)
            } else {
                precondition
            }
        } catch (e: Exception) {
            val startPosition = description.getMetadataInt(Metadata.OFFSET) ?: 0
            val endPosition = startPosition + (description.getMetadataInt(Metadata.STRLEN) ?: 0) - 1
            throw StatementParsingException(
                e.message ?: "",
                startPosition = startPosition,
                endPosition = endPosition,
            )
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
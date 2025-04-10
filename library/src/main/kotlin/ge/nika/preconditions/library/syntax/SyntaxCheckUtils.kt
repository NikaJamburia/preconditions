package ge.nika.preconditions.library.syntax

import ge.nika.preconditions.core.api.template.templateEnd
import ge.nika.preconditions.core.api.template.templateRegex
import ge.nika.preconditions.core.api.template.templateStart
import ge.nika.preconditions.core.api.template.toTemplateContext

internal val syntaxCheckTestTemplateVariable = "test"
internal val syntaxCheckTemplateContext =
    mapOf(syntaxCheckTestTemplateVariable to syntaxCheckTestTemplateVariable).toTemplateContext()

internal fun String.replaceTemplateVariables(): String =
    replace(templateRegex, "${templateStart}$syntaxCheckTestTemplateVariable$templateEnd")


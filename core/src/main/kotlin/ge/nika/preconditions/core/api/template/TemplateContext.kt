package ge.nika.preconditions.core.api.template

interface TemplateContext {
    fun findValue(dottedQuery: DottedQuery): Any
}
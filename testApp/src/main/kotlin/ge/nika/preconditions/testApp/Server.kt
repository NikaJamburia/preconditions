package ge.nika.preconditions.testApp

import ge.nika.preconditions.core.api.service.StatementTranslationService
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import ge.nika.preconditions.core.api.template.toTemplateContext
import org.http4k.routing.routes

class Server {

    fun getRoutes() = routes(evaluate())

    private fun evaluate(): RoutingHttpHandler =
        "evaluate" bind Method.POST to { request ->
            val requestBody = request.bodyString().fromJson<EvaluatePreconditionRequest>()

            try {
                val result = PRECONDITIONS.evaluate(
                    requestBody.preconditionText,
                    requestBody.templateObjects.toTemplateContext()
                )
                jsonResponse(Status.OK, EvaluationResponse(result, listOf()))
            } catch (e: Exception) {
                jsonResponse(Status.BAD_REQUEST, EvaluationResponse(null, listOf(e.message ?: "Unknown error")))
            }

        }
}

data class EvaluatePreconditionRequest(
    val preconditionText: String,
    val templateObjects: Map<String, Any> = mapOf()
)

data class EvaluationResponse(
    val result: Boolean?,
    val errors: List<String>
)

fun jsonResponse(status: Status, body: Any) = Response(status)
    .header("content-type", "application/json")
    .body(body.toJson())

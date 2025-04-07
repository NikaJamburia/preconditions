package ge.nika.preconditions.testApp

import ge.nika.preconditions.core.api.exceptions.StatementParsingException
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import ge.nika.preconditions.core.api.template.toTemplateContext
import ge.nika.preconditions.testApp.StatementParsingExceptionResponse.Companion.toResponse
import org.http4k.routing.routes

class Server {

    fun getRoutes() = routes(evaluate(), ping())

    private fun evaluate(): RoutingHttpHandler =
        "evaluate" bind Method.POST to { request ->
            val requestBody = request.bodyString().fromJson<EvaluatePreconditionRequest>()

            try {
                val result = PRECONDITIONS.evaluate(
                    requestBody.preconditionText,
                    requestBody.templateObjects.toTemplateContext()
                )
                jsonResponse(Status.OK, EvaluationResponse(result))
            }
            catch (e: StatementParsingException) {
                jsonResponse(Status.BAD_REQUEST, EvaluationResponse(parsingError = e.toResponse()))
            }
            catch (e: Exception) {
                jsonResponse(Status.BAD_REQUEST, EvaluationResponse(
                    errors = listOf(e.message ?: "Unknown error")
                ))
            }

        }

    private fun ping(): RoutingHttpHandler =
        "ping" bind Method.GET to {
            jsonResponse(
                status = Status.OK,
                body = object { val message: String = "All good" }
            )
        }
}

data class EvaluatePreconditionRequest(
    val preconditionText: String,
    val templateObjects: Map<String, Any> = mapOf()
)

data class EvaluationResponse(
    val result: Boolean? = null,
    val parsingError: StatementParsingExceptionResponse? = null,
    val errors: List<String> = emptyList(),
)

data class StatementParsingExceptionResponse(
    val message: String,
    val startPosition: Int,
    val endPosition: Int,
) {
    companion object {
        fun StatementParsingException.toResponse() =
            StatementParsingExceptionResponse(message, startPosition, endPosition)
    }
}

fun jsonResponse(status: Status, body: Any) = Response(status)
    .header("content-type", "application/json")
    .body(body.toJson())

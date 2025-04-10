package ge.nika.preconditions.testApp

import ge.nika.preconditions.core.api.exceptions.PreconditionsException
import ge.nika.preconditions.core.api.exceptions.PreconditionsExceptionData
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.RoutingHttpHandler
import org.http4k.routing.bind
import ge.nika.preconditions.core.api.template.toTemplateContext
import org.http4k.routing.routes

class Server {

    fun getRoutes() = routes(evaluate(), ping(), checkSyntax())

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
            catch (e: PreconditionsException) {
                jsonResponse(Status.BAD_REQUEST, EvaluationResponse(exceptions = listOf(e.data())))
            }
            catch (e: Exception) {
                jsonResponse(Status.BAD_REQUEST, e.message ?: "Unknown error")
            }

        }

    private fun checkSyntax(): RoutingHttpHandler =
        "check-syntax" bind Method.POST to { request ->
            val requestBody = request.bodyString().fromJson<EvaluatePreconditionRequest>()

            try {
                val result = PRECONDITIONS.checkSyntax(
                    requestBody.preconditionText,
                )
                jsonResponse(Status.OK, result)
            }
            catch (e: Exception) {
                jsonResponse(Status.BAD_REQUEST, e.message ?: "Unknown error")
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
    val exceptions: List<PreconditionsExceptionData> = emptyList()
)

fun jsonResponse(status: Status, body: Any) = Response(status)
    .header("content-type", "application/json")
    .body(body.toJson())

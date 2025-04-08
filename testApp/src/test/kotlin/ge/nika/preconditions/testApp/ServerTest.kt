package ge.nika.preconditions.testApp

import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.Test

class ServerTest {

    private val routingHandler = Server().getRoutes()

    @Test
    fun `can evaluate given precondition without templates`() {
        val request = Request(Method.POST, "/evaluate").body(
            EvaluatePreconditionRequest("'A' IS 'A'").toJson()
        )

        val response = routingHandler(request)

        response.status shouldBe Status.OK
        response.bodyString().fromJson<EvaluationResponse>().asClue {
            it.result shouldBe true
            it.exceptions.size shouldBe 0
        }
    }

    @Test
    fun `can evaluate given precondition with templates`() {
        val request = Request(Method.POST, "/evaluate").body(
            EvaluatePreconditionRequest(
                "{custom} IS {foo.bar}",
                mapOf(
                    "custom" to "nika",
                    "foo" to object { val bar = "nika" }
                )
            ).toJson()
        )

        val response = routingHandler(request)

        response.status shouldBe Status.OK
        response.bodyString().fromJson<EvaluationResponse>().asClue {
            it.result shouldBe true
            it.exceptions.size shouldBe 0
        }
    }

    @Test
    fun `correctly handles precondition syntax errors`() {
        val request = Request(Method.POST, "/evaluate").body(
            EvaluatePreconditionRequest("'A' IS A").toJson()
        )

        val response = routingHandler(request)

        response.status shouldBe Status.BAD_REQUEST
        response.bodyString().fromJson<EvaluationResponse>().asClue {
            it.result shouldBe null
            it.exceptions.size shouldBe 1
            it.exceptions[0].asClue { pe ->
                pe.message shouldBe "Unknown type of parameter A!"
                pe.indexRange shouldBe 7..7
            }
        }
    }
}
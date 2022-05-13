package ge.nika.preconditions.testApp

import org.http4k.core.then
import org.http4k.filter.CorsPolicy.Companion.UnsafeGlobalPermissive
import org.http4k.filter.DebuggingFilters
import org.http4k.filter.ServerFilters
import org.http4k.server.Netty
import org.http4k.server.asServer

fun main() {
    DebuggingFilters.PrintRequest()
        .then(ServerFilters.Cors(UnsafeGlobalPermissive))
        .then(Server().getRoutes())
        .asServer(Netty(PORT))
        .start()

    println("Web server started on port $PORT")
}
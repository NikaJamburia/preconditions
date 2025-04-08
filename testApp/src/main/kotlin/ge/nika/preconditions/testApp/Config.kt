package ge.nika.preconditions.testApp

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ge.nika.preconditions.core.api.config.corePreconditions
import ge.nika.preconditions.library.Preconditions
import ge.nika.preconditions.library.config.configurePreconditions

private val preconditionsConfig = configurePreconditions {
    registerModule { CorePreconditions() }
    registerPrecondition("IS_LESS_THEN", IsLessTranslator())
}
val PRECONDITIONS = Preconditions(preconditionsConfig)

// Json
val objectMapper = jacksonObjectMapper()
fun Any.toJson(): String = objectMapper.writeValueAsString(this)
inline fun <reified T> String.fromJson(): T = objectMapper.readValue(this, T::class.java)

// Web
val PORT = 8080
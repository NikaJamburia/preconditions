package ge.nika.preconditions.core.api.exceptions

data class PreconditionsExceptionData(
    val type: String,
    val message: String,
    val indexRange: IntRange,
    val additionalData: Map<String, String>,
)
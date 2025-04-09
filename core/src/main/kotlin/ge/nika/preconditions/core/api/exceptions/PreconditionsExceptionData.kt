package ge.nika.preconditions.core.api.exceptions

data class PreconditionsExceptionData(
    val type: String,
    val message: String,
    val indexRange: RangeData,
    val additionalData: Map<String, String>,
)

data class RangeData(
    val start: Int,
    val end: Int,
)
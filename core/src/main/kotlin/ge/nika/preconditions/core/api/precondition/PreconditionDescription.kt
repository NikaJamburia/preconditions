package ge.nika.preconditions.core.api.precondition

data class PreconditionDescription(
    val parameters: List<Any?>,
    val preconditionName: String,
    val metaData: Map<String,Any>? = null,
) {
    fun withMetadataParam(name: String, value: Any): PreconditionDescription {
        return this.copy(
            metaData = buildMap {
                metaData?.let { putAll(it) }
                put(name, value)
            }
        )
    }

    fun getMetadataParam(name: String): Any? = metaData?.get(name)
    fun getMetadataInt(name: String): Int? = getMetadataParam(name) as Int?
}
package ge.nika.preconditions.core.api.template

class DottedQuery(
    val originalString: String
) {
    val objectName: String
    val requestedFields: List<String>

    init {
        check(originalString.isNotBlank()) { "DottedQuery can't be empty!" }
        check(!originalString.contains(" ")) { "DottedQuery can't contain white spaces!" }
        check(!originalString.contains("\n")) { "DottedQuery can't contain line breaks!" }

        val dotSeperated = originalString.split(".")
        objectName = dotSeperated[0]
        requestedFields = dotSeperated.filterIndexed { index, _ -> index > 0 }
    }
}

fun String.toDottedQuery(): DottedQuery = DottedQuery(this)
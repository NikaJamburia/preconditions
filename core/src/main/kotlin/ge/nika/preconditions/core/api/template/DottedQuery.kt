package ge.nika.preconditions.core.api.template

class DottedQuery(
    string: String
) {
    val objectName: String
    val requestedFields: List<String>

    init {
        check(string.isNotBlank()) { "DottedQuery can't be empty!" }
        check(!string.contains(" ")) { "DottedQuery can't contain white spaces!" }
        check(!string.contains("\n")) { "DottedQuery can't contain line breaks!" }

        val dotSeperated = string.split(".")
        objectName = dotSeperated[0]
        requestedFields = dotSeperated.filterIndexed { index, _ -> index > 0 }
    }
}

internal fun String.toDottedQuery(): DottedQuery = DottedQuery(this)
package ge.nika.preconditions.core.utils

import ge.nika.preconditions.core.api.template.templateEnd
import ge.nika.preconditions.core.api.template.templateStart

internal fun String.removeAll(symbol: String): String =
    this.replace(symbol, "")

internal fun String.isTemplate(): Boolean =
    this.startsWith(templateStart) && this.endsWith(templateEnd)

internal fun String.isNumber(): Boolean =
    this.matches("-?\\d+(\\.\\d+)?".toRegex())

internal fun String.representsString(): Boolean =
    this.startsWith("'") && this.endsWith("'")

internal fun String.isAllCaps(): Boolean =
    this.toCharArray().all { it.isUpperCase() }

internal fun String.isOneWord(): Boolean =
    !this.contains(" ")
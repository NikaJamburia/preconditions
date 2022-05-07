package ge.nika.preconditions.core.utils

internal fun String.removeAll(symbol: String): String =
    this.replace(symbol, "")

internal fun String.isTemplate(): Boolean =
    this.startsWith("{") && this.endsWith("}")

internal fun String.isNumber(): Boolean =
    this.matches("-?\\d+(\\.\\d+)?".toRegex())

internal fun String.isAllCaps(): Boolean =
    this.toCharArray().all { it.isUpperCase() }

internal fun String.isOneWord(): Boolean =
    !this.contains(" ")
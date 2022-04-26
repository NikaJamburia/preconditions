package ge.nika.preconditions.utils

fun String.removeAll(symbol: String): String =
    this.replace(symbol, "")

fun String.isTemplate(): Boolean =
    this.startsWith("{") && this.endsWith("}")

fun String.isNumber(): Boolean =
    this.matches("-?\\d+(\\.\\d+)?".toRegex())

fun String.isAllCaps(): Boolean =
    this.toCharArray().all { it.isUpperCase() }

fun String.isOneWord(): Boolean =
    !this.contains(" ")
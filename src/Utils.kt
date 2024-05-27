inline fun <T> Iterable<T>.customToString(
    before: String = "",
    after: String = "",
    separator: String = " ",
    action: (T) -> String = { it.toString() },
): String {
    val stringBuilder = StringBuilder()
    for (element in this) stringBuilder.append(before + action(element) + after + separator)
    return stringBuilder.toString().dropLast(separator.length)
}
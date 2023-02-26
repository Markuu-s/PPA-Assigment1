package org.example

data class SyntaxError(
    val message: String,
    val line: Int
) {
    override fun toString(): String = String.format("Error on line %d: %s", line, message)
}

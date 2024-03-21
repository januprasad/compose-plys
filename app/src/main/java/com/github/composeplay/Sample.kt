package com.github.composeplay

fun main() {
    val items: List<Triple<String, Int, Int>> = listOf(
        Triple("Apple", 1, 0),
        Triple("Hazelnut", 1, 0),
        Triple("Beetroot", 1, 0),
        Triple("Halwa", 1, 0),
        Triple("Icecream", 1, 0),
    )

    val sortedWords = items.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.first })
    println(sortedWords)
}

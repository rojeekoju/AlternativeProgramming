import src.loadData;

fun main() {
    val filePath = "cells.csv"
    try {
        val cells = loadData(filePath)
        cells.forEach { println(it) }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
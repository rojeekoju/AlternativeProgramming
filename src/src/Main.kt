package src;

import org.src.Cell
import org.src.calculateAverage
import org.src.loadData

fun main() {
    val filePath = "cells.csv"
    val cells: List<Cell>
    try {
        cells = loadData(filePath)
        var idx :Int = 0
        cells.forEach { idx++;println(idx.toString()+" " +it)}
    } catch (e: Exception) {
        e.printStackTrace()
        return
    }

    // 1. What company(oem) has the highest average wieght of the phone body?
    val highestAverageWeight = calculateAverage("body_weight", cells)
    val highestAverageWeightOem = cells
        ?.filter { it.bodyWeight != null && it.bodyWeight!! >= highestAverageWeight }
        ?.mapNotNull { it.oem }
        ?.firstOrNull()
    println("1. The company with the highest average weight of the phone body is: $highestAverageWeightOem")
    //Was there any phones that were anounced in one year and released in another year? What are they? Give me the oem and models.
    val phonesWithDifferentLaunchAndReleaseYear = cells.filter { it.launchAnounced != null && it.lauchStatus != null && it.lauchStatus == "Available. Released 2019" && it.launchAnounced != 2019 }
    println("2. Phones that were announced in one year and released in another year:")
    phonesWithDifferentLaunchAndReleaseYear.forEach { println(it) }
    //How many phones have only one feature sensor?
    val phonesWithOneFeatureSensor = cells.filter { it.featuresSensors?.split(" ")?.size == 1 }
    println("3. Number of phones with only one feature sensor: ${phonesWithOneFeatureSensor.size}")
    //What year had the most phones launched in any year later than 1999?
    val years = cells.mapNotNull { it.launchAnounced }.filter { it > 1999 }
    val yearWithMostPhones = years.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
    println("4. The year with the most phones launched in any year later than 1999 is: $yearWithMostPhones")

}

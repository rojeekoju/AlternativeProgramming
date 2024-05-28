package org.src

import java.io.File
import java.util.regex.Pattern

/**
 * @author Rosie
 * data class representing a cell phone with various attributes
 */

data class Cell(
    var oem:String?,
    var model:String?,
    var launchAnounced:Int?,
    var lauchStatus:String?,
    var bodyDimensions:String?,
    var bodyWeight:Double?,
    var bodySim:String?,
    var displayType:String?,
    var displaySize:Double?,
    var displayResolution:String?,
    var featuresSensors:String?,
    var platformOs:String?
) {

    /**
     * creates a cell object from a map of string values
     * @param map where the keys are attributes names
     * @return a cell object with cleaned and parsed values
     */
    companion object {
        fun fromMap(data: Map<String, String>): Cell {
            return Cell(
                oem = cleanOem(data["oem"]),
                model = cleanModel(data["model"]),
                launchAnounced = cleanLaunchAnounced(data["launch_announced"]),
                lauchStatus = cleanLaunchStatus(data["launch_status"]),
                bodyDimensions = cleanBodyDimensions(data["body_dimensions"]),
                bodyWeight = cleanBodyWeight(data["body_weight"]),
                bodySim = cleanBodySim(data["body_sim"]),
                displayType = cleanDisplayType(data["display_type"]),
                displaySize = cleanDisplaySize(data["display_size"]),
                displayResolution = cleanDisplayResolution(data["display_resolution"]),
                featuresSensors = cleanFeaturesSensors(data["features_sensors"]),
                platformOs = cleanPlatformOs(data["platform_os"])
            )
        }

        /**
         * @param value the raw model value
         * @return the cleaned model value, or null if invalid
         */

        private fun cleanModel(value: String?): String? {
            return value?. trim()?.takeIf { it.isNotEmpty() }

        }

        /**
         * Cleans the OEM value.
         *
         * @param value The raw OEM value.
         * @return The cleaned OEM value, or null if invalid.
         */

        private fun cleanOem(value: String?): String? {
            return value?.trim()?.takeIf { it.isNotEmpty() }
        }

        /**
         * Cleans and parses the launch announced value.
         *
         * @param value The raw launch announced value.
         * @return The cleaned and parsed launch announced year, or null if invalid.
         */

        private fun cleanLaunchAnounced(value: String?): Int? {
            val pattern = Pattern.compile("(\\d{4})")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1).toInt() else null;
        }

        /**
         * Cleans the launch status value.
         *
         * @param value The raw launch status value.
         * @return The cleaned launch status value, or null if invalid.
         */

        private fun cleanLaunchStatus(value: String?): String? {
            val pattern = Pattern.compile("(Discontinued|Cancelled|Available. Released \\d{4})")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1) else null;
        }

        /**
         * Cleans the body dimensions value.
         *
         * @param value The raw body dimensions value.
         * @return The cleaned body dimensions value, or null if invalid.
         */

        private fun cleanBodyDimensions(value: String?): String? {
            return value?.trim()?.takeIf { it.isNotEmpty() }
        }

        /**
         * @param the raw body weight value
         * @return the cleaned and parsed body weight, or null if invalid
         */

        private fun cleanBodyWeight(value: String?): Double? {
            val pattern = Pattern.compile("(\\d+(\\.\\d+)?)")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1).toDouble() else null;
        }


        private fun cleanBodySim(value: String?): String? {
            return value?.trim()?.takeIf { it.isNotEmpty() && it != "Yes" && it != "No" }
        }

        private fun cleanDisplayType(value: String?): String? {
            return value?.trim()?.takeIf { it.isNotEmpty() }
        }

        private fun cleanDisplaySize(value: String?): Double? {
            // anything after float and then inches is invalid so we return null
            val pattern = Pattern.compile("(\\d+(\\.\\d+)?)")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1).toDouble() else null;
        }

        private fun cleanDisplayResolution(value: String?): String? {
            return value?.trim()?.takeIf { it.isNotEmpty() }
        }

        /**
         * Cleans the features sensors value.
         *
         * @param value The raw features sensors value.
         * @return The cleaned features sensors value, or null if invalid.
         */

        private fun cleanFeaturesSensors(value: String?): String? {
            // invalid if it is only number
            val pattern = Pattern.compile("([a-zA-Z ]+)")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1) else null;
        }

        /**
         * Cleans the platform OS value.
         *
         * @param value The raw platform OS value.
         * @return The cleaned platform OS value, or null if invalid.
         */

        private fun cleanPlatformOs(value: String?): String? {
            val pattern = Pattern.compile("([a-zA-Z0-9 ]+)")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1) else null;
        }
    }

    override fun toString(): String {
        return "Cell(oem=$oem, model=$model, launchAnounced=$launchAnounced, lauchStatus=$lauchStatus, bodyDimensions=$bodyDimensions, bodyWeight=$bodyWeight, bodySim=$bodySim, displayType=$displayType, displaySize=$displaySize, displayResolution=$displayResolution, featuresSensors=$featuresSensors, platformOs=$platformOs)"
    }
}

/**
 * @param the path to CSV file
 * @return a list of cell obejcts parsed from CSV file
 * @throws illegalargumentexception if the file is not found.
 */
fun loadData(filePath: String): List<Cell> {
    val cells = mutableListOf<Cell>()
    val file = File(filePath)

    if (!file.exists()) {
        throw IllegalArgumentException("File not found: $filePath")
    }

    file.useLines { lines ->
        val iterator = lines.iterator()
        if (!iterator.hasNext()) return emptyList()
        val headers = iterator.next().split(",").map { it.trim() }

        iterator.forEachRemaining { line ->
            val values = line.split(",").map { it.replace("\"","").trim() }
            // if (values.size == headers.size) {
            val dataMap = headers.zip(values).toMap()
            cells.add(Cell.fromMap(dataMap))
            // }
        }
    }
    return cells
}

/**
 * calculates the average of a specified attribute in a list of cell objects.
 * @param value the attribute to average.
 * @param cells The list of cell objects to calculate the average from.
 * @return the average value of the specified attribute
 */
fun calculateAverage(value: String, cells: List<Cell>): Double {
    return cells.mapNotNull {
        when (value) {
            "launch_announced" -> it.launchAnounced?.toDouble() // Convert Int to Double for averaging
            "body_weight" -> it.bodyWeight?.toDouble()
            "display_size" -> it.displaySize?.toDouble()
            else -> null
        }
    }.average()?.takeIf { !it.isNaN() } ?: 0.0

}



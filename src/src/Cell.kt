package src;

import java.io.File
import java.util.regex.Pattern

data class Cell(
    var oem:String?,
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

    companion object {
        fun fromMap(data: Map<String, String>): Cell {
            return Cell(
                oem = cleanOem(data["oem"]),
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

        private fun cleanOem(value: String?): String? {
            return value?.trim()?.takeIf { it.isNotEmpty() }
        }

        private fun cleanLaunchAnounced(value: String?): Int? {
            val pattern = Pattern.compile("(\\d{4})")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1).toInt() else null;
        }

        private fun cleanLaunchStatus(value: String?): String? {
            val pattern = Pattern.compile("(Discontinued|Cancelled|Available. Released \\d{4})")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1) else null;
        }

        private fun cleanBodyDimensions(value: String?): String? {
            return value?.trim()?.takeIf { it.isNotEmpty() }
        }

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

        private fun cleanFeaturesSensors(value: String?): String? {
            // invalid if it is only number
            val pattern = Pattern.compile("([a-zA-Z ]+)")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1) else null;
        }

        private fun cleanPlatformOs(value: String?): String? {
            val pattern = Pattern.compile("([a-zA-Z0-9 ]+)")
            val matcher = pattern.matcher(value ?: "")
            return if (matcher.find()) matcher.group(1) else null;
        }
    }

    override fun toString(): String {
        return "Cell(oem=$oem, launchAnounced=$launchAnounced, lauchStatus=$lauchStatus, bodyDimensions=$bodyDimensions, bodyWeight=$bodyWeight, bodySim=$bodySim, displayType=$displayType, displaySize=$displaySize, displayResolution=$displayResolution, featuresSensors=$featuresSensors, platformOs=$platformOs)"
    }
}

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
            val values = line.split(",").map { it.trim() }
            if (values.size == headers.size) {
                val dataMap = headers.zip(values).toMap()
                cells.add(Cell.fromMap(dataMap))
            }
        }
    }
    return cells
}

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




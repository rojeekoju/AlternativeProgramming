import org.junit.Test
import org.src.Cell

import kotlin.test.assertEquals
import kotlin.test.assertNull

/**
 * @author rosie
 * Test class fo the cell data class.
 */
class CellTest {

    /**
     * Tests the fromMap function with valid data.
     *
     * Verifies that the Cell object is correctly created from the input data map
     * with all fields populated correctly.
     */

    @Test
    fun testFromMap_validData() {
        val data = mapOf(
            "oem" to "BrandA",
            "launch_announced" to "2020",
            "launch_status" to "Available. Released 2020",
            "body_dimensions" to "150x70x8.9 mm",
            "body_weight" to "170.0",
            "body_sim" to "Nano-SIM",
            "display_type" to "LCD",
            "display_size" to "6.1",
            "display_resolution" to "1080x2340 pixels",
            "features_sensors" to "Accelerometer",
            "platform_os" to "Android 10"
        )
        val cell = Cell.fromMap(data)

        assertEquals("BrandA", cell.oem)
        assertEquals(2020, cell.launchAnounced)
        assertEquals("Available. Released 2020", cell.lauchStatus)
        assertEquals("150x70x8.9 mm", cell.bodyDimensions)
        assertEquals(170.0, cell.bodyWeight)
        assertEquals("Nano-SIM", cell.bodySim)
        assertEquals("LCD", cell.displayType)
        assertEquals(6.1, cell.displaySize)
        assertEquals("1080x2340 pixels", cell.displayResolution)
        assertEquals("Accelerometer", cell.featuresSensors)
        assertEquals("Android 10", cell.platformOs)
    }

    /**
    * Tests the fromMap function with invalid data.
    *
    * Verifies that the Cell object is created with null values for fields
    * that cannot be parsed from the input data map.
    */

    @Test
    fun testFromMap_invalidData() {
        val data = mapOf(
            "oem" to "BrandC",
            "launch_announced" to "abcd",
            "body_weight" to "xyz",
            "display_size" to "large"
        )
        val cell = Cell.fromMap(data)

        assertEquals("BrandC", cell.oem)
        assertNull(cell.launchAnounced)
        assertNull(cell.lauchStatus)
        assertNull(cell.bodyDimensions)
        assertNull(cell.bodyWeight)
        assertNull(cell.bodySim)
        assertNull(cell.displayType)
        assertNull(cell.displaySize)
        assertNull(cell.displayResolution)
        assertNull(cell.featuresSensors)
        assertNull(cell.platformOs)
    }

    /**
     * Tests the fromMap function with partial data.
     *
     * Verifies that the Cell object is created with values for fields
     * that can be parsed from the input data map and null for others.
     */

    @Test
    fun testFromMap_partialData() {
        val data = mapOf(
            "oem" to "BrandB",
            "launch_announced" to "2021",
            "body_weight" to "180.5 kg",
            "display_size" to "6.5 inches",
            "platform_os" to "iOS 14"
        )
        val cell = Cell.fromMap(data)


        assertEquals("BrandB", cell.oem)
        assertEquals(2021, cell.launchAnounced)
        assertNull(cell.lauchStatus)
        assertNull(cell.bodyDimensions)
        assertEquals(180.5, cell.bodyWeight)
        assertNull(cell.bodySim)
        assertNull(cell.displayType)
        assertEquals(6.5, cell.displaySize)
        assertNull(cell.displayResolution)
        assertNull(cell.featuresSensors)
        assertEquals("iOS 14", cell.platformOs)
    }
}










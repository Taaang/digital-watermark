package priv.taaang.picture.digital.watermark.resource.bytes.types

/**
 * Created by delroy on 2020-02-25.
 */
object ExampleWaterMark {

    val values = listOf(
        "00000000000000000000000000000000000",
        "01111100111110011111001010100111110",
        "01000000100000010000001010100100010",
        "01111100111110011111001010100100010",
        "00000100100000010000001010100100010",
        "01111100111110011111001111100111110",
        "00000000000000000000000000000000000"
    )

    fun getWidth(): Int {
        return values.getOrNull(0) ?.length ?:0
    }

    fun getHeight(): Int {
        return values.size
    }
}
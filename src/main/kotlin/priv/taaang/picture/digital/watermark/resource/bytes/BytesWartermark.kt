package priv.taaang.picture.digital.watermark.resource.bytes

import priv.taaang.picture.digital.watermark.resource.bytes.types.ExampleWaterMark
import java.io.ByteArrayOutputStream
import java.io.File
import java.lang.IllegalArgumentException
import javax.imageio.ImageIO
import kotlin.collections.HashMap

/**
 * Created by delroy on 2020-02-25.
 */
class BytesWartermark {

    companion object {

        val watermark: ExampleWaterMark = ExampleWaterMark

        private fun getWaterMarkValues(): Map<Int, List<Int>> {
            return watermark.values.mapIndexed { rowIndex, row ->
                val rowValues: MutableList<Int> = Array(row.length) { 0 }.toMutableList()
                row.toCharArray().mapIndexed { charIndex, char -> rowValues[charIndex] = (char - '0') }
                rowIndex to rowValues
            }.toMap()
        }

        /**
         * Process jpg
         */
        fun process(imageFile: File): ByteArray {
            //Read image bytes
            val bufferedImage = ImageIO.read(imageFile)
            //Read watermark
            val watermarkValues = getWaterMarkValues()
            //Parameters check
            if (watermarkValues.size > bufferedImage.height || watermarkValues.getValue(0).size > bufferedImage.width) {
                throw IllegalArgumentException("Watermark is larger than the image.(${bufferedImage.width}, ${bufferedImage.height})")
            }

            //LSB
            watermarkValues.forEach { (rowIndex, rowValues) ->
                rowValues.forEachIndexed { valueIndex, value ->
                    val pixelRGB = bufferedImage.getRGB(valueIndex, rowIndex)
                    val fixedPixelRGB = pixelRGB.shr(1).shl(1).or(value)
                    bufferedImage.setRGB(valueIndex, rowIndex, fixedPixelRGB)
                }
            }

            val bytesOutputStream = ByteArrayOutputStream()
            val outputStream = ImageIO.createImageOutputStream(bytesOutputStream)
            ImageIO.write(bufferedImage, "png", outputStream)
            return bytesOutputStream.toByteArray()
        }

        /**
         * Extract watermark
         */
        fun extract(imageFile: File): Map<Int, List<Int>> {
            //Read image bytes
            val bufferedImage = ImageIO.read(imageFile)
            //Read watermark
            val watermarkValues = getWaterMarkValues()
            //Parameters check
            if (watermarkValues.size > bufferedImage.height || watermarkValues.getValue(0).size > bufferedImage.width) {
                throw IllegalArgumentException("Watermark is larger than the image.(${bufferedImage.width}, ${bufferedImage.height})")
            }

            //LSB Extract
            val extractedWaterMark: MutableMap<Int, List<Int>> = HashMap()
            watermarkValues.forEach { (rowIndex, rowValues) ->
                //Row lsb array
                val rowBytes = Array(rowValues.size) { 0 }.toMutableList()
                //Get row lsb
                rowValues.forEachIndexed { valueIndex, _ ->
                    val pixelRGB = bufferedImage.getRGB(valueIndex, rowIndex)
                    val lsb = pixelRGB.and(1)
                    rowBytes[valueIndex] = lsb
                }
                //Save row lsb
                extractedWaterMark[rowIndex] = rowBytes
            }

            return extractedWaterMark
        }
    }
}


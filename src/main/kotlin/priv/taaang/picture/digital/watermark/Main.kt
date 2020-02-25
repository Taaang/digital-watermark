package priv.taaang.picture.digital.watermark

import priv.taaang.picture.digital.watermark.resource.bytes.BytesWartermark
import java.io.File
import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    //Get file path
    val filePath = args.getOrNull(0)
        ?: throw IllegalArgumentException("Need file path plz.")

    //Get file
    val imageFile = File(filePath)
    if (!imageFile.exists()) {
        throw IllegalArgumentException("File not found.($filePath)")
    }

    //Mark
    val fixedImageBytes = BytesWartermark.process(imageFile)

    //Output
    val outputPath = File(imageFile.parent, "result_${System.currentTimeMillis()}")
    outputPath.mkdirs()
    val fixedImageFile = File(outputPath, "fixed_${imageFile.name}")
    fixedImageFile.writeBytes(fixedImageBytes)

    //Read and check
    val bytesWatermark = BytesWartermark.extract(fixedImageFile)
    bytesWatermark.entries.sortedBy { it.key }.forEachIndexed { _, values ->
        values.value.forEach {
            val character = if (it == 1) { "1" } else { " " }
            print(character)
        }
        println("")
    }
}


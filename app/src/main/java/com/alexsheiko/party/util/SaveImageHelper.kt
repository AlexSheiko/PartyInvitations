package com.alexsheiko.party.util

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.support.v4.content.FileProvider
import com.alexsheiko.party.ui.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream

fun MainActivity.saveImage(bitmap: Bitmap) {
    val cachePath = File(cacheDir, "images")
    cachePath.mkdirs()
    val stream = FileOutputStream("$cachePath/image.png")
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    stream.close()
}

fun MainActivity.getImageUri(): Uri {
    val imagePath = File(cacheDir, "images")
    val newFile = File(imagePath, "image.png")
    return FileProvider.getUriForFile(this,
            "com.alexsheiko.party.fileprovider", newFile)
}

fun MainActivity.captureCanvas() {
    toast("Saving...")

    canvas.backgroundColor = Color.WHITE
    canvas.isDrawingCacheEnabled = true
    canvas.buildDrawingCache()
    val bitmap = canvas.drawingCache

    doAsync {
        saveImage(bitmap)

        uiThread {
            shareImage(getImageUri())
            canvas.isDrawingCacheEnabled = false
        }
    }
}
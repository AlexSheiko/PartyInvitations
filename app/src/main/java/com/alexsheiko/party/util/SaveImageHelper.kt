package com.alexsheiko.party.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Color
import android.net.Uri
import android.support.v4.content.FileProvider
import com.alexsheiko.party.ui.MainActivity
import com.alexsheiko.party.ui.PayActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream

fun MainActivity.saveImage(bitmap: Bitmap) {
    val cachePath = File(cacheDir, "images")
    cachePath.mkdirs()
    val stream = FileOutputStream("$cachePath/image.png")
    bitmap.compress(JPEG, 51, stream)
    stream.close()
}

fun MainActivity.getImageUri(): Uri {
    val imagePath = File(cacheDir, "images")
    val newFile = File(imagePath, "image.png")
    return FileProvider.getUriForFile(this,
            "com.alexsheiko.party.fileprovider", newFile)
}

fun MainActivity.captureCanvas() {
    if (!isUserPro()) {
        startActivityForResult(intentFor<PayActivity>(), 123)
    }
    canvas.backgroundColor = Color.WHITE
    canvas.isDrawingCacheEnabled = true
    canvas.buildDrawingCache()
    val bitmap = canvas.drawingCache

    doAsync {
        saveImage(bitmap)
        uiThread {
            canvas.isDrawingCacheEnabled = false

            if (isUserPro()) {
                shareImage(getImageUri())
            }
        }
    }
}
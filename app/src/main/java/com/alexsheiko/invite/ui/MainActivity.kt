package com.alexsheiko.invite.ui

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.Color.WHITE
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView.BufferType.EDITABLE
import com.alexsheiko.invite.R
import com.alexsheiko.invite.util.prefs
import com.alexsheiko.invite.util.shareImage
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onClick
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showImage()
        populateFields()
        setClickListeners()

        reactToInput()
    }

    override fun onStop() {
        super.onStop()
        saveFields()
    }

    private fun setClickListeners() {
        shareButton.onClick { captureCanvas() }
    }

    private fun populateFields() {
        val address = prefs.getString("address",
                getString(R.string.details_default))
        inputField.setText(address, EDITABLE)
        textView.text = address
    }

    private fun saveFields() {
        val details = inputField.text.toString()
        if (!details.startsWith("Enter")) {
            prefs.edit()
                    .putString("address", details)
                    .apply()
        }
    }

    private fun captureCanvas() {
        canvas.backgroundColor = WHITE
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

    private fun saveImage(bitmap: Bitmap) {
        val cachePath = File(cacheDir, "images")
        cachePath.mkdirs()
        val stream = FileOutputStream("$cachePath/image.png")
        bitmap.compress(PNG, 100, stream)
        stream.close()
    }

    private fun getImageUri(): Uri {
        val imagePath = File(cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        return FileProvider.getUriForFile(this,
                "com.alexsheiko.invitationmaker.fileprovider", newFile)
    }

    private fun reactToInput() {
        inputField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textView.text = s.toString()
            }
        })
    }

    private fun showImage() {
        val resId = R.drawable.party_template_5
        Glide.with(this).load(resId).dontAnimate().into(imageView)
    }
}
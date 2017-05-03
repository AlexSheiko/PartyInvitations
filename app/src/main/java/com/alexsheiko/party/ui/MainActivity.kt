package com.alexsheiko.party.ui

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.Color.WHITE
import android.graphics.Typeface.BOLD
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView.BufferType.EDITABLE
import com.alexsheiko.party.R
import com.alexsheiko.party.util.prefs
import com.alexsheiko.party.util.shareImage
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import java.io.File
import java.io.FileOutputStream
import java.util.Collections.shuffle

class MainActivity : BaseActivity() {

    var mShared = false

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
        imageView.onClick { changeImage() }
    }

    private fun populateFields() {
        val title = prefs.getString("title",
                getString(R.string.title_default))
        val address = prefs.getString("address",
                getString(R.string.details_default))

        inputTitle.setTypeface(null, BOLD)
        inputTitle.setText(title, EDITABLE)
        inputText.setText(address, EDITABLE)

        titleView.text = title
        textView.text = address
    }

    private fun saveFields() {
        val title = inputTitle.text.toString()
        val details = inputText.text.toString()

        prefs.edit()
                .putString("address", details)
                .apply()
        prefs.edit()
                .putString("title", title)
                .apply()
    }

    private fun captureCanvas() {
        toast("Saving...")

        mShared = true

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
                "com.alexsheiko.invite.fileprovider", newFile)
    }

    private fun reactToInput() {
        inputTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                titleView.text = s.toString()
            }
        })
        inputText.addTextChangedListener(object : TextWatcher {
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
        val resId = R.drawable.wedding_template_7

        Glide.with(this)
                .load(resId)
                .into(imageView)
    }

    private fun changeImage() {
        val images = listOf(
                R.drawable.party_template_1,
                R.drawable.party_template_2,
                R.drawable.party_template_5,
                R.drawable.party_template_8,
                R.drawable.wedding_template_2,
                R.drawable.wedding_template_3,
                R.drawable.wedding_template_4,
                R.drawable.wedding_template_6)

        shuffle(images)

        Glide.with(this)
                .load(images[0])
                .into(imageView)
    }
}
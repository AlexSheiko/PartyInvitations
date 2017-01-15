package com.alexsheiko.invitationmaker

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.WHITE
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.alexsheiko.invitationmaker.ads.AdProvider
import com.alexsheiko.invitationmaker.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onClick
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EditActivity : BaseActivity() {

    private val mAdProvider = AdProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        setImage()
        setFields()
        setOnClickListeners()

        showTutorialIfNeeded()
        reactToInput()

        doAsync {
            mAdProvider.loadInBackground()
        }
    }

    private fun showTutorialIfNeeded() {

    }

    override fun onStop() {
        super.onStop()
        saveFields()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mAdProvider.show()
        mAdProvider.loadInBackground()
    }

    private fun setOnClickListeners() {
        shareButton.onClick { captureCanvas() }
        backHint.onClick { onBackPressed() }
    }

    private fun setFields() {
        val nameBride = prefs.getString("nameBride", "Leila")
        val nameBroom = prefs.getString("nameGroom", "Markus")
        val address = prefs.getString("address", "2509 Nogales Street, Texas")

        name1Field.setText(nameBride, TextView.BufferType.EDITABLE)
        name2Field.setText(nameBroom, TextView.BufferType.EDITABLE)
        addressField.setText(address, TextView.BufferType.EDITABLE)

        name1Label.text = nameBride
        name2Label.text = nameBroom
        addressLabel.text = formatAddress(address)
    }

    private fun saveFields() {
        prefs.edit()
                .putString("nameBride", name1Field.text.toString())
                .putString("nameGroom", name2Field.text.toString())
                .putString("address", addressField.text.toString())
                .apply()
    }

    private fun captureCanvas() {
        canvas.backgroundColor = WHITE
        canvas.isDrawingCacheEnabled = true
        canvas.buildDrawingCache()
        // TODO: canvas.isDrawingCacheEnabled = false
        val bitmap = canvas.drawingCache

        doAsync {
            saveImage(bitmap)

            uiThread {
                shareImage()
            }
        }
    }

    private fun saveImage(bitmap: Bitmap) {
        try {
            val cachePath = File(cacheDir, "images")
            cachePath.mkdirs()
            val stream = FileOutputStream("$cachePath/image.png")
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getImageUri(): Uri {
        val imagePath = File(cacheDir, "images")
        val newFile = File(imagePath, "image.png")
        return FileProvider.getUriForFile(this,
                "com.alexsheiko.invitationmaker.fileprovider", newFile)
    }

    private fun shareImage() {
        val contentUri = getImageUri()
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
        shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        startActivityForResult(Intent.createChooser(shareIntent,
                resources.getText(R.string.send_to)), 1)
    }

    private fun reactToInput() {
        name1Field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                name1Label.text = "$s &"
            }
        })
        name2Field.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                name2Label.text = s
            }
        })
        addressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addressLabel.text = formatAddress(s.toString())
            }
        })
    }

    private fun setImage() {
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        imageView.setImageURI(imageUri)
    }

    private fun formatAddress(address: String): String {
        return address.replaceFirst(", ", ",\n")
    }
}
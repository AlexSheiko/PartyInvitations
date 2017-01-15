package com.alexsheiko.invitationmaker

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.WHITE
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        setImage()
        setFields()

        reactToInput()
        shareButton.onClick { captureCanvas() }
    }

    override fun onStop() {
        super.onStop()
        saveFields()
    }

    private fun setFields() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        name1Field.setText(prefs.getString("nameBride", "Leila"), TextView.BufferType.EDITABLE)
        name2Field.setText(prefs.getString("nameGroom", "Markus"), TextView.BufferType.EDITABLE)
        addressField.setText(prefs.getString("address", "2509 Nogales Street, Texas"), TextView.BufferType.EDITABLE)
    }

    private fun saveFields() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefs.edit()
                .putString("nameBride", name1Field.text.toString())
                .putString("nameGroom", name2Field.text.toString())
                .putString("address", addressField.text.toString())
                .apply()
    }

    private fun captureCanvas() {
        canvas.isDrawingCacheEnabled = true
        canvas.backgroundColor = WHITE
        canvas.buildDrawingCache()
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
        startActivity(Intent.createChooser(shareIntent, "Choose an app"))
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
                addressLabel.text = s.toString().replaceFirst(", ", ",\n")
            }
        })
    }

    private fun setImage() {
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        val imageView = findViewById(R.id.imageView) as ImageView
        imageView.setImageURI(imageUri)
    }
}

package com.alexsheiko.invitationmaker

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import com.alexsheiko.invitationmaker.base.BaseActivity
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream

class EditActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        setImage()
        setFields()

        reactToInput()
        doneButton.onClick { captureCanvas() }
    }

    private fun setFields() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        name1Field.setText(prefs.getString("nameBride", "Leila"), TextView.BufferType.EDITABLE)
        name2Field.setText(prefs.getString("nameGroom", "Markus"), TextView.BufferType.EDITABLE)
        addressField.setText(prefs.getString("address", "2509 Nogales Street, Corpus Christi, Texas"), TextView.BufferType.EDITABLE)
    }

    private fun captureCanvas() {
        doAsync {
            canvas.isDrawingCacheEnabled = true
            canvas.buildDrawingCache()

            val bitmap = canvas.drawingCache
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)

            uiThread {
                startActivity<ResultActivity>("bytes" to stream.toByteArray())
            }
        }
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

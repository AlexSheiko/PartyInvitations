package com.alexsheiko.invitationmaker

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
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

        setTitle()
        setImage()

        reactToInput()
        doneButton.onClick { captureCanvas() }
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
        nameField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                nameLabel.text = s
            }
        })
    }

    private fun setImage() {
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        val imageView = findViewById(R.id.imageView) as ImageView
        imageView.setImageURI(imageUri)
    }

    private fun setTitle() {
        val title = intent.getStringExtra("title")
        setSupportActionBar(toolbar)
        supportActionBar?.title = title
    }
}

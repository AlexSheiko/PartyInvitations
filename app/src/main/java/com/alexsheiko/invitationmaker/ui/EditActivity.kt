package com.alexsheiko.invitationmaker.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color.WHITE
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.view.View.VISIBLE
import android.widget.TextView
import com.alexsheiko.invitationmaker.R
import com.alexsheiko.invitationmaker.util.AdProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.onClick
import org.jetbrains.anko.uiThread
import java.io.File
import java.io.FileOutputStream

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
        val launchCount = prefs.getInt("launchCount", 0)
        if (launchCount < 3) {
            prefs.edit().putInt("launchCount", launchCount + 1).apply()
            shareHint.visibility = VISIBLE
            backHint.visibility = VISIBLE
        }
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
        // 1. common
        val font = Typeface.createFromAsset(assets, "fonts/CaviarDreams.ttf")
        val address = prefs.getString("address", "2509 Nogales Street, Texas\nSeptember 23, 6:00 PM")
        addressField.setText(address, TextView.BufferType.EDITABLE)
        addressLabel.text = address
        firstLabel.typeface = font
        secondLabel.typeface = font

        // 2. wedding
        val nameBride = prefs.getString("nameBride", "Leila")
        brideField.setText(nameBride, TextView.BufferType.EDITABLE)
        val nameBroom = prefs.getString("nameGroom", "Markus")
        broomField.setText(nameBroom, TextView.BufferType.EDITABLE)

        // 3. birthday
        val titleBirthday = prefs.getString("titleBirthday", "Birthday Party!")
        titleBirthdayField.setText(titleBirthday, TextView.BufferType.EDITABLE)
        val nameAge = prefs.getString("nameAge", "Joey turns 18.")
        nameAgeField.setText(nameAge, TextView.BufferType.EDITABLE)

        // 4. party
        val titleParty = prefs.getString("titleParty", "You are invited!")
        titlePartyField.setText(titleParty, TextView.BufferType.EDITABLE)
        val details = prefs.getString("details", "Jane's tea party")
        detailsField.setText(details, TextView.BufferType.EDITABLE)

        val category = intent.getStringExtra("category")
        when (category) {
            "wedding" -> {
                weddingContainer.visibility = VISIBLE
                firstLabel.text = formatBrideName(nameBride)
                secondLabel.text = nameBroom
            }
            "birthday" -> {
                birthdayContainer.visibility = VISIBLE
                firstLabel.text = titleBirthday
                secondLabel.text = nameAge
            }
            "party" -> {
                partyContainer.visibility = VISIBLE
                firstLabel.text = titleParty
                secondLabel.text = details
            }
        }
    }

    private fun saveFields() {
        prefs.edit()
                .putString("address", addressField.text.toString())
                .putString("nameBride", brideField.text.toString())
                .putString("nameGroom", broomField.text.toString())
                .putString("titleBirthday", titleBirthdayField.text.toString())
                .putString("nameAge", nameAgeField.text.toString())
                .putString("titleParty", titlePartyField.text.toString())
                .putString("details", detailsField.text.toString())
                .apply()
    }

    private fun captureCanvas() {
        canvas.backgroundColor = WHITE
        canvas.isDrawingCacheEnabled = true
        canvas.buildDrawingCache()
        val bitmap = canvas.drawingCache

        doAsync {
            saveImage(bitmap)

            uiThread {
                shareImage()
                canvas.isDrawingCacheEnabled = false
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
        } catch (e: Exception) {
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
        brideField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                firstLabel.text = formatBrideName(s.toString())
            }
        })
        broomField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                secondLabel.text = s
            }
        })
        addressField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addressLabel.text = s.toString()
            }
        })
        titleBirthdayField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                firstLabel.text = s.toString()
            }
        })
        nameAgeField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                secondLabel.text = s.toString()
            }
        })
        titlePartyField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                firstLabel.text = s.toString()
            }
        })
        detailsField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                secondLabel.text = s.toString()
            }
        })
    }

    private fun formatBrideName(s: String): String {
        return "$s &"
    }

    private fun setImage() {
        val resId = intent.getIntExtra("imageResId", -1)
        Glide.with(this).load(resId).dontAnimate().into(imageView)
    }
}
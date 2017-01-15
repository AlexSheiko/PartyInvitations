package com.alexsheiko.invitationmaker.ui

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.widget.CardView
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.alexsheiko.invitationmaker.R
import kotlinx.android.synthetic.main.activity_result.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.File
import java.util.*

class ResultActivity : BaseActivity() {

    private var mStartup = true
    private var mShareUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        setImage()

        sendButton.setOnClickListener { view -> shareImage(Uri.parse("")) }
    }

    private fun setImage() {
        doAsync {
            val bytes = intent.getByteArrayExtra("bytes")
            val bitmap = BitmapFactory.decodeByteArray(
                    bytes, 0, bytes.size)
            uiThread { imageView.setImageBitmap(bitmap) }
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mStartup) {
            showFinishButton()
        }
        mStartup = false
    }

    private fun showFinishButton() {
        findViewById(R.id.finish_container).visibility = View.VISIBLE
        sendButton.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        sendButton.setImageResource(R.drawable.ic_send)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendButton.elevation = 0.0f
            val containerCardView = findViewById(R.id.containerCardView) as CardView
            containerCardView.elevation = 0.0f
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return false
    }

    fun onClickFinish(view: View) {
        recycleImage()
        showCongrats()
        navigateToMainScreen()
    }

    private fun recycleImage() {
        File(mShareUri!!.toString()).delete()
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
    }

    private fun showCongrats() {
        val congratsArray = resources.getStringArray(R.array.congrats)
        val index = Random().nextInt(congratsArray.size)
        Toast.makeText(this, congratsArray[index], Toast.LENGTH_LONG).show()
    }

    private fun shareImage(imageUri: Uri) {
        val fileSD = File(imageUri.toString())
        mShareUri = FileProvider.getUriForFile(this, "com.alexsheiko.invitationmaker.fileprovider", fileSD)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_STREAM, mShareUri)
        shareIntent.type = "image/*"
        startActivity(Intent.createChooser(shareIntent, resources.getText(R.string.send_to)))
    }

    fun onClickImage(view: View) {
        onBackPressed()
    }
}

package com.alexsheiko.invitationmaker

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.FileProvider
import android.support.v7.widget.CardView
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.alexsheiko.invitationmaker.base.BaseActivity
import java.io.File
import java.util.*


class ResultActivity : BaseActivity() {

    private var mStartup = true
    private var mSendFAB: FloatingActionButton? = null
    private var mShareUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        val imageView = findViewById(R.id.imageView) as ImageView
        imageView.setImageURI(imageUri)

        mSendFAB = findViewById(R.id.sendButton) as FloatingActionButton
        mSendFAB!!.setOnClickListener { view -> shareImage(imageUri) }
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
        mSendFAB!!.backgroundTintList = ColorStateList.valueOf(Color.WHITE)
        mSendFAB!!.setImageResource(R.drawable.ic_send)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mSendFAB!!.elevation = 0.0f
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

    override fun onBackPressed() {
        val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
        val intent = Intent()
        intent.putExtra("imageUri", imageUri.toString())
        setResult(Activity.RESULT_CANCELED, intent)
        super.onBackPressed()
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

package com.alexsheiko.invitationmaker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.GridLayoutManager
import android.view.MenuItem
import com.alexsheiko.invitationmaker.ads.CloseListener
import com.alexsheiko.invitationmaker.base.BaseActivity
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.activity_grid.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class GridActivity : BaseActivity(), CloseListener {

    private var mInterstitialAd: InterstitialAd? = null
    private var mShowingAdForId: Int = 0
    private var mSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        title = intent.getStringExtra("title")

        val adapter = GridAdapter(this)
        // TODO: recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        val category = intent.getStringExtra("category")
        val templates = getTemplates(category)
        adapter.addAll(templates)
    }

    private fun recordPurchase(resId: Int) {
        val purchased = prefs.getStringSet("purchased", HashSet<String>())
        purchased.addAll(listOf(resId.toString()))
        prefs.edit().putStringSet("purchased", purchased).apply()
    }

    private fun getPurchasedTemplates(): MutableSet<String>? {
        return prefs.getStringSet("purchased", HashSet<String>())
    }

    fun processClick(resId: Int, options: ActivityOptionsCompat) {
        openEditor(resId, options)
    }

    private fun getTemplates(category: String): List<Int> {
        val templates = ArrayList<Int>()
        for (i in 1..30) {
            var imageName = category.toLowerCase() + "_template_" + i
            imageName = imageName.replace(" ", "_")
            val resId = resources.getIdentifier(imageName, "drawable", packageName)
            if (resId != 0) {
                templates.add(resId)
            } else {
                var imageNamePaid = category.toLowerCase() + "_template_" + i + "_paid"
                imageNamePaid = imageNamePaid.replace(" ", "_")
                val resIdPaid = resources.getIdentifier(imageNamePaid, "drawable", packageName)
                if (resIdPaid != 0) {
                    templates.add(resIdPaid)
                }
            }
        }
        return templates
    }

    private fun openEditor(resId: Int, options: ActivityOptionsCompat) {
        var imageUri: Uri? = null
        doAsync {
            val bitmap = BitmapFactory.decodeResource(resources, resId)
            try {
                val file = convertBitmapToFile(bitmap)
                imageUri = Uri.fromFile(file)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            uiThread {
                openImageEditor(imageUri!!, options)
                dismissSnackbar()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CREATE) {
            if (resultCode == Activity.RESULT_OK) {
                val editedImageUri = data.data
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("imageUri", editedImageUri.toString())
                startActivityForResult(intent, REQUEST_SHARE)
                overridePendingTransition(0, 0)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun openImageEditor(imageUri: Uri, options: ActivityOptionsCompat) {
        startActivity(intentFor<EditActivity>(
                "imageUri" to imageUri.toString(),
                "title" to intent.getStringExtra("title")
        ))
    }

    @Throws(IOException::class)
    private fun convertBitmapToFile(bitmap: Bitmap): File {
        // Create a file to write bitmap data
        val file = File(cacheDir, "image.png")

        // Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        val bytes = bos.toByteArray()

        // Write the bytes in file
        val fos = FileOutputStream(file)
        fos.write(bytes)
        fos.flush()
        fos.close()
        return file
    }

    override fun onClosed() {
        // TODO: remove
    }

    private fun showSnackBar() {
        val parentLayout = findViewById(android.R.id.content)
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(parentLayout, "Opening image...", Snackbar.LENGTH_INDEFINITE)
        }
        mSnackbar!!.show()
    }

    private fun dismissSnackbar() {
        if (mSnackbar != null && mSnackbar!!.isShown) {
            mSnackbar!!.dismiss()
        }
    }

    companion object {
        val REQUEST_CREATE = 101
        private val REQUEST_SHARE = 237
    }
}
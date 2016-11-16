package com.alexsheiko.invitationmaker

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.MenuItem
import android.widget.GridView
import com.adobe.creativesdk.aviary.AdobeImageIntent
import com.adobe.creativesdk.aviary.internal.filters.ToolLoaderFactory
import com.alexsheiko.invitationmaker.ads.AdProviderVideo
import com.alexsheiko.invitationmaker.ads.RewardListener
import com.alexsheiko.invitationmaker.base.BaseActivity
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import hugo.weaving.DebugLog
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class GridActivity : BaseActivity(), RewardListener {
    private var mAdProvider: AdProviderVideo? = null
    private var mShowingAdForId: Int = 0
    private var mSnackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        val category = intent.getStringExtra("category")
        supportActionBar?.title = category

        val gridView = findViewById(R.id.gridView) as GridView
        val adapter = GridAdapter(this)
        gridView.adapter = adapter
        gridView.setOnItemClickListener { parent, view, position, id ->
            val resId = adapter.getItem(position)!!
            val imageName = resources.getResourceEntryName(resId)
            val isImagePaid = imageName.contains("paid")
            if (!isImagePaid) {
                openEditor(resId)
            } else {
                // Show video ad
                mAdProvider!!.onClickShow()
                mShowingAdForId = resId
            }

            Answers.getInstance().logContentView(ContentViewEvent()
                    .putContentType("Template")
                    .putContentId(imageName))
        }

        val templates = getTemplates(category)
        Collections.shuffle(templates)
        adapter.addAll(templates)
    }

    private fun getTemplates(category: String): List<Int> {
        val templates = ArrayList<Int>()
        for (i in 1..299) {
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

    override fun onStart() {
        super.onStart()
        if (mAdProvider != null) {
            mAdProvider!!.showEditorIfNeeded()
            mAdProvider = null
        }
        initAds()
    }

    @DebugLog
    private fun initAds() {
        val activity = this
        doAsync {
            mAdProvider = AdProviderVideo()
            mAdProvider!!.prepare(activity, activity)
        }
    }

    private fun openEditor(resId: Int) {
        showSnackBar()

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
                openImageEditor(imageUri!!)
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
        } else if (requestCode == REQUEST_SHARE) {
            val imageUri = Uri.parse(data.getStringExtra("imageUri"))
            openImageEditor(imageUri)
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

    fun openImageEditor(imageUri: Uri) {
        val tools = arrayOf(ToolLoaderFactory.Tools.TEXT, ToolLoaderFactory.Tools.DRAW, ToolLoaderFactory.Tools.CROP, ToolLoaderFactory.Tools.ENHANCE)
        val imageEditorIntent = AdobeImageIntent.Builder(this)
                .setData(imageUri)
                .withSharedElementTransition(true)
                .withToolList(tools)
                .build()

        /* Start the Image Editor */
        startActivityForResult(imageEditorIntent, GridActivity.REQUEST_CREATE)
    }

    @Throws(IOException::class)
    private fun convertBitmapToFile(bitmap: Bitmap): File {
        //Todo: Show loading snackbar and parse image in background
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

    override fun onRewarded() {
        showSnackBar()
        openEditor(mShowingAdForId)
    }

    private fun showSnackBar() {
        val parentLayout = findViewById(android.R.id.content)
        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(parentLayout, "Loading image...", Snackbar.LENGTH_INDEFINITE)
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
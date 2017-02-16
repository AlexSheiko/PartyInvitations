package com.alexsheiko.invitationmaker.util.extensions

import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import com.alexsheiko.invitationmaker.R

interface ShareExtensions {

    fun Activity.shareImage(imageUri: Uri) {
        val i = Intent()
        i.action = Intent.ACTION_SEND
        i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
        i.setDataAndType(imageUri, contentResolver.getType(imageUri))
        i.putExtra(Intent.EXTRA_STREAM, imageUri)
        startActivityForResult(createChooser(i,
                resources.getText(R.string.send_to)), 1)
    }
}
package com.alexsheiko.party.util

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import com.alexsheiko.party.R
import com.alexsheiko.party.ui.BaseActivity

fun BaseActivity.shareImage(imageUri: Uri) {
    val i = Intent(ACTION_SEND)
    i.addFlags(FLAG_GRANT_READ_URI_PERMISSION)
    i.setDataAndType(imageUri,
            contentResolver.getType(imageUri))
    i.putExtra(EXTRA_STREAM, imageUri)
    startActivityForResult(createChooser(i,
            resources.getText(R.string.send_to)), 1)
}
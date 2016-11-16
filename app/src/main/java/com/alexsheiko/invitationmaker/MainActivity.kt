package com.alexsheiko.invitationmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.alexsheiko.invitationmaker.base.BaseActivity
import org.jetbrains.anko.alert

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickCategory(view: View) {
        val intent = Intent(this, GridActivity::class.java)
        for (i in 0..(view as ViewGroup).childCount - 1) {
            val child = view.getChildAt(i)
            if (child is TextView) {
                val category = child.text.toString()
                        .replace(" ", "").replace("\n", " ")
                intent.putExtra("category", category)
            }
        }
        startActivity(intent)
    }

    override fun onBackPressed() {
        alert("Are you sure you want to exit the app?", "Stay on Invitation Maker") {
            positiveButton("Exit") { super.onBackPressed() }
            negativeButton("Stay on") { }
        }.show()
    }
}

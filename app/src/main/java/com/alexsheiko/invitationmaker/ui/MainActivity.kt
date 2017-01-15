package com.alexsheiko.invitationmaker.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alexsheiko.invitationmaker.R

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.app_name_full)
        supportActionBar?.elevation = 0f
    }

    fun onClickCategory(view: View) {
        val intent = Intent(this, GridActivity::class.java)
        for (i in 0..(view as ViewGroup).childCount - 1) {
            val child = view.getChildAt(i)
            if (child is TextView) {
                val title = child.text.toString()
                        .replace(" ", "").replace("\n", " ")
                intent.putExtra("title", title)
                intent.putExtra("category", child.tag.toString())
            }
        }
        startActivity(intent)
    }
}

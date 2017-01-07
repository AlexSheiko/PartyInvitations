package com.alexsheiko.invitationmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alexsheiko.invitationmaker.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = getString(R.string.app_name_full)
        supportActionBar?.elevation = 0f
    }

    fun onClickCategory(view: View) {
        val intent = Intent(this, GridActivity::class.java)
        (0..(view as ViewGroup).childCount - 1)
                .map { view.getChildAt(it) }
                .filterIsInstance<TextView>()
                .map {
                    it.text.toString()
                            .replace(" ", "").replace("\n", " ")
                }
                .forEach { intent.putExtra("category", it) }
        startActivity(intent)
    }
}

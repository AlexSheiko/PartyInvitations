package com.alexsheiko.invitationmaker

import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.GridLayoutManager
import com.alexsheiko.invitationmaker.base.BaseActivity
import kotlinx.android.synthetic.main.activity_grid.*
import org.jetbrains.anko.intentFor
import java.util.*

class GridActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        title = intent.getStringExtra("title")

        val adapter = GridAdapter(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter

        val category = intent.getStringExtra("category")
        val templates = getTemplates(category)
        adapter.addAll(templates)
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

    fun openEditor(resId: Int, options: ActivityOptionsCompat) {
        startActivity(intentFor<EditActivity>(
                "imageResId" to resId,
                "title" to intent.getStringExtra("title"),
                "category" to intent.getStringExtra("category")
        ))
    }
}
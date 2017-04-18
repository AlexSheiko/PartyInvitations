package com.alexsheiko.invite.ui

import android.app.Activity
import android.content.Context
import android.preference.PreferenceManager
import android.support.v4.app.ActivityOptionsCompat.makeSceneTransitionAnimation
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.alexsheiko.invite.R
import com.bumptech.glide.Glide
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class GridAdapter(context: Context) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    private var mContext: Context = context
    private var mDataset: ArrayList<Int> = ArrayList()
    private val mTemplateNames: ArrayList<String> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_template, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataset.size
    }

    override fun getItemId(position: Int): Long {
        return mDataset[position].toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val resId = mDataset[position]
        Glide.with(mContext).load(resId).into(holder?.mImageView)

        val options = makeSceneTransitionAnimation(
                mContext as Activity, holder?.mImageView, "image")

        holder?.mContainer?.setOnClickListener {
            (mContext as GridActivity).processClick(resId, options)
        }
    }

    // Provide a reference to the views for each data item
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var mImageView: ImageView = v.findViewById(R.id.imageView) as ImageView
        var mContainer: View = v.findViewById(R.id.container)
    }

    fun addAll(templates: List<Int>) {
        doAsync {
            templates.forEachIndexed { i, resId ->
                mDataset.add(templates[i])
                val templateName = mContext.resources.getResourceEntryName(resId)
                mTemplateNames.add(templateName)
            }
            uiThread {
                notifyDataSetChanged()
            }
        }
    }

    private fun getPurchasedTemplates(): MutableSet<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        return prefs.getStringSet("purchased", HashSet<String>())
    }
}

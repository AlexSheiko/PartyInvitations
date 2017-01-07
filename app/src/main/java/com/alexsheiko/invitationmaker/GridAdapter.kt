package com.alexsheiko.invitationmaker

import android.content.Context
import android.preference.PreferenceManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.*


class GridAdapter(context: Context) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private var mContext: Context = context
    private var mDataset: ArrayList<Int> = ArrayList()

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
        Glide.with(mContext).load(resId).fitCenter()
                .listener(object : RequestListener<Int?, GlideDrawable?> {
                    override fun onResourceReady(resource: GlideDrawable?, model: Int?, target: Target<GlideDrawable?>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        val templateName = mContext.resources.getResourceEntryName(resId)
                        val isPurchased = getPurchasedTemplates()!!.contains(resId.toString())

                        if (templateName.contains("paid") && !isPurchased) {
                            holder?.mPriceTag?.visibility = View.VISIBLE
                        } else {
                            holder?.mPriceTag?.visibility = View.GONE
                        }
                        return false
                    }

                    override fun onException(e: Exception?, model: Int?, target: Target<GlideDrawable?>?, isFirstResource: Boolean): Boolean {
                        return false
                    }
                }).into(holder?.mImageView)

        holder?.mContainer?.setOnClickListener {
            (mContext as GridActivity).processClick(resId)
        }
    }

    // Provide a reference to the views for each data item
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {

        var mImageView: ImageView = v.findViewById(R.id.imageView) as ImageView
        var mPriceTag: TextView = v.findViewById(R.id.priceTag) as TextView
        var mContainer: View = v.findViewById(R.id.container)
    }

    fun addAll(templates: List<Int>) {
        templates.forEachIndexed { i, j ->
            mDataset.add(templates[i])
            notifyItemInserted(i)
        }
    }

    private fun getPurchasedTemplates(): MutableSet<String>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(mContext)
        return prefs.getStringSet("purchased", HashSet<String>())
    }
}

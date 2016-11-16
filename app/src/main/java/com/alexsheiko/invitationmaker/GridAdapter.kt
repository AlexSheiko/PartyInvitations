package com.alexsheiko.invitationmaker

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import it.sephiroth.android.library.picasso.Picasso
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
        Picasso.with(mContext).load(resId).into(holder?.mImageView)
    }

    // Provide a reference to the views for each data item
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mImageView: ImageView
        var mPriceTag: TextView

        init {
            mImageView = v.findViewById(R.id.imageView) as ImageView
            mPriceTag = v.findViewById(R.id.priceTag) as TextView
        }
    }

    fun addAll(templates: List<Int>) {
        templates.forEachIndexed { i, j ->
            mDataset.add(templates[i])
            notifyItemInserted(i)
        }
    }
}

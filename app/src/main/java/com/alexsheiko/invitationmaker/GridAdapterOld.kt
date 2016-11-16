package com.alexsheiko.invitationmaker

import android.content.Context
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.util.*


class GridAdapterOld(context: Context) : ArrayAdapter<Int>(context, 0) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view: View
        if (convertView != null) {
            view = convertView
        } else {
            view = inflater.inflate(R.layout.item_template, parent, false)
        }
        val resId = getItem(position)!!
        Glide.with(context)
                .load(resId).fitCenter().centerCrop()
                .listener(object : RequestListener<Int, GlideDrawable> {
                    override fun onException(e: Exception, model: Int?, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                        return false
                    }

                    override fun onResourceReady(resource: GlideDrawable, model: Int?, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                        val templateName = context.resources.getResourceEntryName(resId)
                        val isPurchased = getPurchasedTemplates()!!.contains(resId.toString())

                        if (templateName.contains("paid") && !isPurchased) {
                            view.findViewById(R.id.priceTag).visibility = View.VISIBLE
                        } else {
                            view.findViewById(R.id.priceTag).visibility = View.GONE
                        }
                        return false
                    }
                })
                .into(view.findViewById(R.id.imageView) as ImageView)

        return view
    }

    private fun getPurchasedTemplates(): MutableSet<String>? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getStringSet("purchased", HashSet<String>())
    }
}

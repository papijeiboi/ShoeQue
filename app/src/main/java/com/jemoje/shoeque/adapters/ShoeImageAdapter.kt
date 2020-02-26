package com.jemoje.shoeque.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.jemoje.shoeque.R
import com.jemoje.shoeque.model.ImagesData
import kotlinx.android.synthetic.main.image_slider_item.view.*

class ShoeImageAdapter constructor(private val context: Context, private val images: MutableList<ImagesData>) : PagerAdapter() {

    private var inflater: LayoutInflater? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {

        return view === `object`
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.image_slider_item, null)
//        view.iv_image_slide.setImageResource(images[position].imageUrl)
        Glide.with(context)
            .load(images[position].imageUrl!!)
            .error(R.drawable.ic_no_photo)
            .into(view.iv_image_slide)

        val vp = container as ViewPager
        vp.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val vp = container as ViewPager
        val view = `object` as View
        vp.removeView(view)
    }
}
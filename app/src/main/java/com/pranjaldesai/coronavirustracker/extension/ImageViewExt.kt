package com.pranjaldesai.coronavirustracker.extension

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.extension.ImageViewExt.loadImage
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.koin.core.KoinComponent
import org.koin.core.inject

object ImageViewExt : KoinComponent {
    private val externalImagePicasso: Picasso by inject()

    fun ImageView.loadImage(url: String?, @DrawableRes placeholderResourceId: Int) {
        if (url.isNotNullOrBlank()) {
            externalImagePicasso
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(placeholderResourceId)
                .into(this, object : Callback {
                    override fun onError(e: Exception?) {
                        externalImagePicasso
                            .load(url)
                            .placeholder(placeholderResourceId)
                            .error(placeholderResourceId)
                            .into(this@loadImage, object : Callback {
                                override fun onError(e: Exception?) {
                                    e?.let { loge(e) }
                                }

                                override fun onSuccess() {}
                            })
                    }

                    override fun onSuccess() {}
                })
        } else {
            externalImagePicasso.load(placeholderResourceId).into(this)
        }
    }
}

@BindingAdapter("imageDrawable")
fun ImageView.loadImageDrawable(@DrawableRes drawable: Int) =
    this.setImageResource(drawable)

@BindingAdapter("imageTag")
fun ImageView.loadTag(tag: String) {
    this.tag = tag
}

@BindingAdapter("imageUrl")
fun ImageView.loadImage(url: String?) = loadImage(url, R.drawable.ic_action_placeholder)

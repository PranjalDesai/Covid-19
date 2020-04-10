package com.pranjaldesai.coronavirustracker.ui.shared

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import androidx.annotation.StyleRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pranjaldesai.coronavirustracker.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class ImageFullScreenDialog(
    context: Context,
    @StyleRes styleResId: Int = R.style.FullScreenDialog
) : Dialog(context, styleResId),
    KoinComponent {

    private val fullScreenPhotoView: PhotoView
    private val fullScreenShareButton: FloatingActionButton
    private val fullScreenToolbar: Toolbar
    private lateinit var imageBitmap: Bitmap
    private val externalImagePicasso: Picasso by inject()

    init {
        val layoutView = layoutInflater.inflate(R.layout.view_fullscreen_photo_view, null)
        setContentView(layoutView)
        fullScreenPhotoView = findViewById(R.id.fullscreen_photo_view)
        fullScreenToolbar = findViewById(R.id.fullscreen_toolbar)
        fullScreenShareButton = findViewById(R.id.share_fab)
        tapListener { dismiss() }
    }

    fun imageUrl(imageUrl: String): ImageFullScreenDialog {
        externalImagePicasso.load(imageUrl)
            .placeholder(R.drawable.ic_action_placeholder)
            .into(fullScreenPhotoView)
        externalImagePicasso.load(imageUrl).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    imageBitmap = bitmap
                    fullScreenShareButton.visibility = View.VISIBLE
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                fullScreenShareButton.visibility = View.GONE
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
        fullScreenPhotoView.tag = imageUrl
        return this
    }

    fun imageUrl(imageUrl: String, title: String): ImageFullScreenDialog {
        fullScreenToolbar.title = title
        externalImagePicasso.load(imageUrl)
            .placeholder(R.drawable.ic_action_placeholder)
            .into(fullScreenPhotoView)
        externalImagePicasso.load(imageUrl).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                bitmap?.let {
                    imageBitmap = bitmap
                    fullScreenShareButton.visibility = View.VISIBLE
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                fullScreenShareButton.visibility = View.GONE
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
        fullScreenPhotoView.tag = imageUrl
        return this
    }


    private fun tapListener(listener: (tapOutsideImage: Boolean) -> Unit) {
        fullScreenToolbar.apply {
            navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_home_up_indicator)
            setNavigationOnClickListener { listener(true) }
        }

        fullScreenShareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_STREAM, getBitmapFromView(imageBitmap))
            context.startActivity(Intent.createChooser(intent, "Share Image"))
        }
    }

    private fun getBitmapFromView(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val file =
                File(context.externalCacheDir, System.currentTimeMillis().toString() + ".jpg")
            val out = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(
                context,
                context.applicationContext.packageName + ".provider", file
            )

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri
    }
}
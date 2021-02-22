package com.iab.imagetext.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.ssb.imagetext.R
import com.iab.imagetext.presenter.ImageTextPresenterInterface
import java.lang.ref.WeakReference

class ImageTextView(context: Context, imageTextPresenterInterface: ImageTextPresenterInterface) : ConstraintLayout(context), ImageTextViewInterface {

//    weakReference variable
    var imageTextPresenterInterfaceWeekReference: WeakReference<ImageTextPresenterInterface> = WeakReference(imageTextPresenterInterface)
    private var contextWeakReference: WeakReference<Context> = WeakReference(context)
    private var listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
//            change the visibility of the image View to Gone and change the text according to message
            setImageTextImageViewVisibility(View.GONE)
            setImageTextTextViewText("Loading Failed !!")
            if (imageTextPresenterInterfaceWeekReference.get() != null) {
                imageTextPresenterInterfaceWeekReference.get()!!.errorOnLoadingImage()
            }
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
            if (imageTextPresenterInterfaceWeekReference.get() != null) {
//                imageTextPresenterInterfaceWeekReference.get().stopLoadingImageAnimation(true)
            }
            return false
        }

    }

    init {
        prepareView()
    }

    override fun prepareView() {

//        set Layout Params to this constraint
        var layoutParams:ConstraintLayout.LayoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        this.layoutParams = layoutParams

//        inflate Layout in View
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layoutInflater.inflate(R.layout.image_text_view, this, true)
        this.isLongClickable = true
//          find id and set OnClick listener on them
//        findViewById<AppCompatImageView>(R.id.imageText_imageView).setOnClickListener {
////            on image Click listener
//            imageTextPresenterInterfaceWeekReference.get()?.onImageTextViewClicked()
//        }
//
//        findViewById<AppCompatTextView>(R.id.imageText_textView).setOnClickListener {
////            on TextClickListener
//            imageTextPresenterInterfaceWeekReference.get()?.onImageTextViewClicked()
//        }

        this.setOnClickListener{
            imageTextPresenterInterfaceWeekReference.get()?.onImageTextViewClicked()
        }

        findViewById<RadioButton>(R.id.imageText_radio_button).setOnCheckedChangeListener { buttonView, isChecked ->
            imageTextPresenterInterfaceWeekReference.get()?.onImageTextRadioButtonClicked(isChecked)
        }

    }

    override fun setImageUsingDrawable(drawableId: Int, placeHolderId: Int, errorImageId: Int) {
        if (contextWeakReference.get() != null) {
            (findViewById<View>(R.id.imageText_imageView) as AppCompatImageView).visibility = VISIBLE
            (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).post {
                Glide.with(contextWeakReference.get()!!)
                        .load(drawableId)
                        .placeholder(placeHolderId)
                        .error(errorImageId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).width, (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).height)
                        .listener(listener)
                        .into(findViewById<View>(R.id.imageText_imageView) as AppCompatImageView)
            }
        }
    }

    override fun setImageUsingBitmap(bitmap: Bitmap?, placeHolderId: Int, errorImageId: Int) {
        if (contextWeakReference.get() != null) {
            (findViewById<View>(R.id.imageText_imageView) as AppCompatImageView).visibility = VISIBLE
            (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).post {
                Glide.with(contextWeakReference.get()!!)
                        .load(bitmap)
                        .placeholder(placeHolderId)
                        .error(errorImageId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).width, (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).height)
                        .listener(listener)
                        .into(findViewById<View>(R.id.imageText_imageView) as AppCompatImageView)
            }
        }
    }

    override fun setImageUsingUri(imageUri: Uri?, placeHolderId: Int, errorImageId: Int) {
        if (contextWeakReference.get() != null) {
            (findViewById<View>(R.id.imageText_imageView) as AppCompatImageView).visibility = VISIBLE
            (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).post {
                Glide.with(contextWeakReference.get()!!)
                        .load(imageUri)
                        .placeholder(placeHolderId)
                        .error(errorImageId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).width, (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).height)
                        .listener(listener)
                        .into(findViewById<View>(R.id.imageText_imageView) as AppCompatImageView)
            }
        }
    }

    override fun setImageUsingServerPath(serverPath: String?, placeHolderId: Int, errorImageId: Int) {
        if (contextWeakReference.get() != null) {
            (findViewById<View>(R.id.imageText_imageView) as AppCompatImageView).visibility = VISIBLE
            (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).post {
                Glide.with(contextWeakReference.get()!!)
                        .load(serverPath)
                        .placeholder(placeHolderId)
                        .error(errorImageId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).width, (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).height)
                        .listener(listener)
                        .into(findViewById<View>(R.id.imageText_imageView) as AppCompatImageView)
            }
        }
    }

    override fun setImageUsingLocalPath(localPath: String?, placeHolderId: Int, errorImageId: Int) {
        if (contextWeakReference.get() != null) {
            (findViewById<View>(R.id.imageText_imageView) as AppCompatImageView).visibility = VISIBLE
            (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).post {
                Glide.with(contextWeakReference.get()!!)
                        .load(localPath)
                        .placeholder(placeHolderId)
                        .error(errorImageId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).width, (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).height)
                        .listener(listener)
                        .into(findViewById<View>(R.id.imageText_imageView) as AppCompatImageView)
            }
        }
    }

    override fun setImageUsingRawFolder(rawFolderId: Int, placeHolderId: Int, errorImageId: Int) {
        if (contextWeakReference.get() != null) {
            (findViewById<View>(R.id.imageText_imageView) as AppCompatImageView).visibility = VISIBLE
            (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).post {
                Glide.with(contextWeakReference.get()!!)
                        .load(rawFolderId)
                        .placeholder(placeHolderId)
                        .error(errorImageId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).width, (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).height)
                        .listener(listener)
                        .into(findViewById<View>(R.id.imageText_imageView) as AppCompatImageView)
            }
        }
    }

    override fun setImageUsingBytes(bytes: ByteArray?, placeHolderId: Int, errorImageId: Int) {
        if (contextWeakReference.get() != null) {
            (findViewById<View>(R.id.imageText_imageView) as AppCompatImageView).visibility = VISIBLE
            (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).post {
                Glide.with(contextWeakReference.get()!!)
                        .load(bytes)
                        .placeholder(placeHolderId)
                        .error(errorImageId)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override((findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).width, (findViewById<View>(R.id.imageText_imageView_constraintLayout) as ConstraintLayout).height)
                        .listener(listener)
                        .into(findViewById<View>(R.id.imageText_imageView) as AppCompatImageView)
            }
        }
    }

    override fun setImageTextImageViewBackgroundColor(backgroundColor: Int) {
        (findViewById<View>(R.id.imageText_imageView) as AppCompatImageView).setBackgroundColor(backgroundColor)
    }

    override fun setImageTextTextVisibility(visibility: Int) {
        findViewById<AppCompatTextView>(R.id.imageText_textView).visibility = visibility
    }

    override fun setRadioButtonVisibility(visibility: Int) {
        findViewById<AppCompatRadioButton>(R.id.imageText_radio_button).visibility = visibility
    }

    override fun setRadioButtonChecked() {
        findViewById<AppCompatRadioButton>(R.id.imageText_radio_button).isChecked = true
    }

    override fun setRadioButtonUnChecked() {
        findViewById<AppCompatRadioButton>(R.id.imageText_radio_button).isChecked = false
        findViewById<AppCompatRadioButton>(R.id.imageText_radio_button).toggle()
    }

    override fun setImageTextImageViewVisibility(visibility: Int) {
        findViewById<AppCompatImageView>(R.id.imageText_imageView).visibility = visibility
    }

    override fun setImageTextViewTextSize(sizeInDp: Float) {
        findViewById<AppCompatTextView>(R.id.imageText_textView).setTextSize(TypedValue.COMPLEX_UNIT_DIP, sizeInDp)
    }

    override fun setImageTextViewTextColor(textColor: Int) {
        findViewById<AppCompatTextView>(R.id.imageText_textView).setTextColor(textColor)
    }

    override fun setImageTextViewTextFontFace(typeface: Typeface) {
        findViewById<AppCompatTextView>(R.id.imageText_textView).setTypeface(typeface)
    }

    override fun setImageTextViewTextAllCap(isTextAllCap: Boolean) {
        findViewById<AppCompatTextView>(R.id.imageText_textView).isAllCaps = isTextAllCap
    }

    @SuppressLint("CutPasteId")
    override fun setImageTextTextViewText(text: String) {
        findViewById<AppCompatTextView>(R.id.imageText_textView).text = text
    }

    override fun setImageTextViewPadding(padding: Int) {
        this.setPadding(padding, padding, padding, padding)
    }

    override fun setViewDimensionWidth(viewWidth: Int) {
        this.layoutParams.width = viewWidth
    }

    override fun setViewDimensionHeight(viewHeight: Int) {
        this.layoutParams.height = viewHeight
    }

    override fun setImageTextViewBackgroundColor(backgroundColor: Int) {
        this.setBackgroundColor(backgroundColor)
    }

    override fun setImageTextImageViewPadding(padding: Int) {
        findViewById<AppCompatImageView>(R.id.imageText_imageView).setPadding(padding, padding, padding, padding)
    }

    override fun setImageTextImageViewScaleType(scaleType: ImageView.ScaleType) {
        findViewById<AppCompatImageView>(R.id.imageText_imageView).scaleType = scaleType
    }

    override fun setImageTextRadioButtonPadding(paddingTop: Int, paddingRight: Int, paddingBottom: Int, paddingLeft: Int) {
        findViewById<AppCompatRadioButton>(R.id.imageText_radio_button).setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
    }

    override fun onDestroy() {

    }

}

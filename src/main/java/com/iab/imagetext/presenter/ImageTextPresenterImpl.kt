package com.iab.imagetext.presenter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView.ScaleType
import com.iab.imagetext.model.ImageTextDataModel
import com.iab.imagetext.model.ImageType
import com.iab.imagetext.util.ImageUtils
import com.iab.imagetext.view.ImageTextView
import com.iab.imagetext.view.ImageTextViewInterface
import java.io.IOException
import java.lang.ref.WeakReference

class ImageTextPresenterImpl private constructor(builder: Builder) : ImageTextPresenterInterface {

    //    weak references
    private var contextWeakReference: WeakReference<Context> = WeakReference(builder.context)
    private var imageTextListenerWeakReference: WeakReference<ImageTextPresenterInterface.ImageTextListener> = WeakReference(builder.imageTextListener)
    private var imageTextViewLongClickListenerLWeakReference: WeakReference<ImageTextPresenterInterface.ImageTextViewLongClickListener>? = null
    private var imageTextViewRadioButtonClickListenerWeakReference: WeakReference<ImageTextPresenterInterface.ImageTextViewRadioButtonClickListener>? = null
    private var imageTextViewErrorListenerWeakReference: WeakReference<ImageTextPresenterInterface.ImageTextErrorListener>? = null

    //    strong reference
    private var imageTextViewInterface: ImageTextViewInterface
    private var imageTextDataModel: ImageTextDataModel = ImageTextDataModel()

    //    local variable
    private var placeHolderId: Int = 0
    private var errorImageId: Int = 0
    private var imageTextTextViewVisibility = View.VISIBLE

    class Builder(
        //        Required Field
        val context: Context,
        val imageTextListener: ImageTextPresenterInterface.ImageTextListener) {

        //        textView Variables and Properties
        var textSize = 12f
        var textColor = Color.BLACK
        var textFontName = ""
        var textAllCaps = false
        var textMarginInDp = 0
        var selectedTextColor = Color.BLACK
        var imageTextTextViewVisibility: Int = View.VISIBLE

        //        Complete View Variables and Properties
        var viewHeight = ViewGroup.LayoutParams.MATCH_PARENT
        var viewWidth = ViewGroup.LayoutParams.MATCH_PARENT
        var viewPaddingInDP = 0
        var viewMarginInDP = 0
        var viewBackgroundColor = Color.TRANSPARENT
        var selectedViewBackgroundColor = Color.TRANSPARENT

        //        RadioButton properties
        var radioButtonVisibilityIs = View.GONE
        var radioButtonPaddingTopInDp = 0
        var radioButtonPaddingRightInDp = 0
        var radioButtonPaddingBottomInDp = 0
        var radioButtonPaddingLeftInDp = 0

        //        imageView Properties
        var imageTextImageViewPaddingInDp = 0;
        var imageTextImageViewMarginInDp = 0;
        var imageTextImageViewScaleType = ScaleType.CENTER_CROP
        var imageTextImageViewVisibility: Int = View.VISIBLE
        var loadingImageFromDrawable: String = "loading_1"
        var errorImageFromDrawable: String = "error_1"
        var imageTextImageViewBackgroundColor = Color.TRANSPARENT

        //        Listeners
        var imageTextViewLongClickListener: ImageTextPresenterInterface.ImageTextViewLongClickListener? = null
        var imageTextViewRadioButtonClickListener: ImageTextPresenterInterface.ImageTextViewRadioButtonClickListener? = null
        var imageTextViewErrorListener: ImageTextPresenterInterface.ImageTextErrorListener? = null

        //        TextView Properties
        fun withTextSizeInSP(textSize: Float): Builder {
            this.textSize = textSize
            return this
        }

        fun withTextFontName(textFontName: String): Builder {
            this.textFontName = textFontName
            return this
        }

        fun withTextAllCaps(textAllCaps: Boolean): Builder {
            this.textAllCaps = textAllCaps
            return this
        }

        fun withTextColor(textColor: Int, selectedTextColor: Int): Builder {
            this.textColor = textColor
            this.selectedTextColor = selectedTextColor
            return this
        }

        fun withTextMarginInDp(margin: Int): Builder {
            this.textMarginInDp = margin
            return this
        }

        fun withImageTextTextViewVisibility(visibility: Int): Builder {
            this.imageTextTextViewVisibility = visibility
            return this
        }

        //        View Properties
        fun withViewHeightInDP(viewHeight: Int): Builder {
            this.viewHeight = viewHeight
            return this
        }

        fun withViewWidthInDP(viewWidth: Int): Builder {
            this.viewWidth = viewWidth
            return this
        }

        fun withViewMarginInDP(viewMarginInDP: Int): Builder {
            this.viewMarginInDP = viewMarginInDP
            return this
        }

        fun withViewPaddingInDP(viewPaddingInDP: Int): Builder {
            this.viewPaddingInDP = viewPaddingInDP
            return this
        }

        fun withViewBackgroundColor(viewBackgroundColor: Int, selectedViewBackgroundColor: Int): Builder {
            this.viewBackgroundColor = viewBackgroundColor
            this.selectedViewBackgroundColor = selectedViewBackgroundColor
            return this
        }

        //        ImageView Properties
        fun withImageTextImageViewScaleType(scaleType: ScaleType): Builder {
            this.imageTextImageViewScaleType = scaleType
            return this
        }

        fun withImageTextImageViewPaddingInDp(paddingInDp: Int): Builder {
            this.imageTextImageViewPaddingInDp = paddingInDp
            return this
        }


        fun withImageTextImageViewMarginInDp(margin: Int): Builder {
            this.imageTextImageViewMarginInDp = margin
            return this
        }

        fun withImageViewLoadingDrawableImage(loadingDrawableImageName: String): Builder {
            this.loadingImageFromDrawable = loadingImageFromDrawable
            return this
        }

        fun withImageViewErrorDrawableImage(errorDrawableImage: String): Builder {
            this.errorImageFromDrawable = errorImageFromDrawable
            return this
        }

        fun withImageTextImageViewVisibility(visibility: Int): Builder{
            this.imageTextImageViewVisibility = visibility
            return this
        }

        fun withImageTextImageViewBackgroundColor(backgroundColor: Int) : Builder{
            this.imageTextImageViewBackgroundColor = backgroundColor
            return this
        }

        //        RadioButton Properties
        fun withRadioButtonVisibilityAndPadding(radioButtonVisibility: Int, radioButtonMarginTop: Int, radioButtonMarginTopInDp: Int, radioButtonMarginRightInDp: Int, radioButtonMarginBottomInDp: Int, radioButtonMarginLeftInDp: Int): Builder {
            this.radioButtonVisibilityIs = radioButtonVisibility
            this.radioButtonPaddingTopInDp = radioButtonMarginTopInDp
            this.radioButtonPaddingRightInDp = radioButtonMarginRightInDp
            this.radioButtonPaddingBottomInDp = radioButtonMarginBottomInDp
            this.radioButtonPaddingLeftInDp = radioButtonMarginLeftInDp
            return this
        }


        fun withImageTextViewLongClickListener(imageTextViewLongClickListener: ImageTextPresenterInterface.ImageTextViewLongClickListener?): Builder {
            this.imageTextViewLongClickListener = imageTextViewLongClickListener
            return this
        }

        fun withImageTextViewRadioButtonClickListener(imageTextViewButtonClickListener: ImageTextPresenterInterface.ImageTextViewRadioButtonClickListener?): Builder {
            this.imageTextViewRadioButtonClickListener = imageTextViewButtonClickListener
            return this
        }
        fun withImageTextViewErrorListener(imageTextErrorListener: ImageTextPresenterInterface.ImageTextErrorListener): Builder {
            this.imageTextViewErrorListener = imageTextErrorListener
            return this
        }

        fun build(): ImageTextPresenterImpl {
            return ImageTextPresenterImpl(this)
        }
    }

    //    creating companion object
    companion object ImageTextPresenterImplCompanion {
        fun newBuilder(context: Context, imageTextListener: ImageTextPresenterInterface.ImageTextListener): Builder {
            return Builder(context, imageTextListener)
        }
    }

    //    init block
    init {
        if (builder.imageTextViewLongClickListener != null) {
            imageTextViewLongClickListenerLWeakReference = WeakReference(builder.imageTextViewLongClickListener)
        }
        if (builder.imageTextViewRadioButtonClickListener != null) {
            imageTextViewRadioButtonClickListenerWeakReference = WeakReference(builder.imageTextViewRadioButtonClickListener)
        }
        if(builder.imageTextViewErrorListener != null){
            imageTextViewErrorListenerWeakReference = WeakReference(builder.imageTextViewErrorListener)
        }

//        creating View reference
        imageTextViewInterface = ImageTextView(contextWeakReference.get()!!, this)

//        setting TextView Properties
        imageTextViewInterface.setImageTextViewTextSize(builder.textSize)
        imageTextViewInterface.setImageTextViewTextColor(builder.textColor)
        imageTextViewInterface.setImageTextViewTextAllCap(builder.textAllCaps)
        if (builder.textFontName != "") {
            val typeFace: Typeface = Typeface.createFromAsset(contextWeakReference.get()?.assets, builder.textFontName)
            imageTextViewInterface.setImageTextViewTextFontFace(typeFace)
        }
        imageTextViewInterface.setImageTextImageViewVisibility(builder.imageTextImageViewVisibility)
        imageTextViewInterface.setImageTextTextVisibility(builder.imageTextTextViewVisibility)
        this.imageTextTextViewVisibility = builder.imageTextTextViewVisibility

        var viewWidth = 0
        var viewHeight = 0
//        Full View Properties
        if (builder.viewWidth != ViewGroup.LayoutParams.MATCH_PARENT && builder.viewWidth != ViewGroup.LayoutParams.WRAP_CONTENT) {
//            convert View Width from Dp to Pix and assign to viewWidth
//            imageTextViewInterface.setViewDimensionWidth(ImageUtils.dpToPx(contextWeakReference.get(), builder.viewWidth))
            viewWidth = ImageUtils.dpToPx(contextWeakReference.get(), builder.viewWidth)
        } else {
//            imageTextViewInterface.setViewDimensionWidth(builder.viewWidth)
            viewWidth = builder.viewWidth
        }

        if (builder.viewHeight != ViewGroup.LayoutParams.MATCH_PARENT && builder.viewHeight != ViewGroup.LayoutParams.WRAP_CONTENT) {
//            convert View Width from Dp to Pix and assign to viewHeight
//            imageTextViewInterface.setViewDimensionHeight(ImageUtils.dpToPx(contextWeakReference.get(), builder.viewHeight))
            viewHeight = ImageUtils.dpToPx(contextWeakReference.get(), builder.viewHeight)
        } else {
            viewHeight = builder.viewHeight
        }

        imageTextViewInterface.setViewHeightWidth(viewWidth, viewHeight)

        imageTextViewInterface.setImageTextViewPadding(ImageUtils.dpToPx(contextWeakReference.get(), builder.viewPaddingInDP))

        imageTextViewInterface.setImageTextViewBackgroundColor(builder.viewBackgroundColor)

//        setting RadioButton Properties
        imageTextViewInterface.setRadioButtonVisibility(builder.radioButtonVisibilityIs)
        imageTextViewInterface.setImageTextRadioButtonPadding(
            ImageUtils.dpToPx(contextWeakReference.get(), builder.radioButtonPaddingTopInDp),
            ImageUtils.dpToPx(contextWeakReference.get(), builder.radioButtonPaddingRightInDp),
            ImageUtils.dpToPx(contextWeakReference.get(), builder.radioButtonPaddingBottomInDp),
            ImageUtils.dpToPx(contextWeakReference.get(), builder.radioButtonPaddingLeftInDp),
        )


//        checking and getting id of loading image and error image from drawable
        if (contextWeakReference.get() != null) {
            placeHolderId = contextWeakReference.get()!!.resources.getIdentifier(builder.loadingImageFromDrawable, "drawable", contextWeakReference.get()!!.packageName)
            errorImageId = contextWeakReference.get()!!.resources.getIdentifier(builder.errorImageFromDrawable, "drawable", contextWeakReference.get()!!.packageName)
        }

//        setting ImageView Properties
        imageTextViewInterface.setImageTextImageViewPadding(ImageUtils.dpToPx(contextWeakReference.get(), builder.imageTextImageViewPaddingInDp))
        imageTextViewInterface.setImageTextImageViewScaleType(builder.imageTextImageViewScaleType)


    }

    override fun createView(imageTextDataModel: ImageTextDataModel) {
        this.imageTextDataModel.copyImageTextDataModel(imageTextDataModel)
        if (imageTextDataModel.text != null && !imageTextDataModel.text.equals("") && imageTextTextViewVisibility != View.GONE) {
            imageTextViewInterface.setImageTextTextVisibility(View.VISIBLE)
            imageTextViewInterface.setImageTextTextViewText(imageTextDataModel.text!!)
        }

        when (imageTextDataModel.imageType) {
            ImageType.NO_IMAGE -> {
                imageTextViewInterface.setImageTextImageViewVisibility(View.GONE)
            }
            ImageType.DRAWABLE -> {
                if (contextWeakReference.get() != null) {
                    val drawableId = contextWeakReference.get()!!.resources.getIdentifier(imageTextDataModel.imageInDrawable, "drawable", contextWeakReference.get()!!.packageName)
                    if (drawableId != 0) {
                        imageTextViewInterface.setImageUsingDrawable(drawableId, placeHolderId, errorImageId)
                    } else {
//                        checking for error listener is not null if null then do nothing is yes then send error to main context
                        if (imageTextViewErrorListenerWeakReference != null) {
                            if (imageTextViewErrorListenerWeakReference!!.get() != null) {
                                imageTextViewErrorListenerWeakReference!!.get()!!.onImageTextErrorThrows("DrawableNotFound", "Drawable not found in ImageTextPresenterImpl while finding image for Image Type Drawable")
                            }

                        }
                    }
                }
            }
            ImageType.LOCAL_PATH -> {
                imageTextViewInterface.setImageUsingLocalPath(imageTextDataModel.imageInLocalPath, placeHolderId, errorImageId)
            }
            ImageType.URI -> {
                imageTextViewInterface.setImageUsingUri(imageTextDataModel.imageUri, placeHolderId, errorImageId)
            }
            ImageType.BITMAP -> {
                imageTextViewInterface.setImageUsingBitmap(imageTextDataModel.imageBitmap, placeHolderId, errorImageId)
            }
            ImageType.ASSETS -> {
                if (contextWeakReference.get() != null) {
                    try {
                        val assetManager = contextWeakReference.get()!!.assets
                        val inputStream = assetManager.open(imageTextDataModel.imageInAssetFolder!!)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        if (bitmap != null) {
                            imageTextViewInterface.setImageUsingBitmap(bitmap, placeHolderId, errorImageId)
                        }
                    } catch (e: IOException) {
                        if (imageTextViewErrorListenerWeakReference != null) {
                            if (imageTextViewErrorListenerWeakReference!!.get() != null) {
                                imageTextViewErrorListenerWeakReference!!.get()!!.onImageTextErrorThrows("AssetNotFound", "Asset not found in ImageTextPresenterImpl while finding image for Image Type Assets")
                            }

                        }
                        e.printStackTrace()
                    }
                }
                imageTextViewInterface.setImageUsingUri(imageTextDataModel.imageUri, placeHolderId, errorImageId)
            }
            ImageType.RAW -> {
                if (contextWeakReference.get() != null) {
                    val rawId = contextWeakReference.get()!!.resources.getIdentifier(imageTextDataModel.imageInRawFolder, "raw", contextWeakReference.get()!!.packageName)
                    if (rawId != 0) {
                        imageTextViewInterface.setImageUsingRawFolder(rawId, placeHolderId, errorImageId)
                    } else {
//                        checking for error listener is not null if null then do nothing is yes then send error to main context
                        if (imageTextViewErrorListenerWeakReference != null) {
                            if (imageTextViewErrorListenerWeakReference!!.get() != null) {
                                imageTextViewErrorListenerWeakReference!!.get()!!.onImageTextErrorThrows("DrawableNotFound", "Drawable not found in ImageTextPresenterImpl while finding image for Image Type Drawable")
                            }

                        }
                    }
                }
            }
            ImageType.SERVER_PATH -> {
                imageTextViewInterface.setImageUsingServerPath(imageTextDataModel.imageServerPath, placeHolderId, errorImageId)
            }
//            if non of them then set Visibility of image GONE
            else -> {
                imageTextViewInterface.setImageTextImageViewVisibility(View.GONE)
            }
        }

//        setRadio Button Visibility
        if (imageTextDataModel.isCheckBoxVisible) {
            imageTextViewInterface.setRadioButtonVisibility(View.VISIBLE)
        } else {
            imageTextViewInterface.setRadioButtonVisibility(View.GONE)
        }

//      check for radio button for check
        if (imageTextDataModel.isChecked) {
            imageTextViewInterface.setRadioButtonChecked()
        } else {
            imageTextViewInterface.setRadioButtonUnChecked()
        }
    }

    override fun onImageTextViewClicked() {
        if(imageTextListenerWeakReference.get() != null && imageTextDataModel != null){
            imageTextListenerWeakReference!!.get()!!.onImageTextViewClicked(imageTextDataModel!!.id, imageTextDataModel!!.isChecked)
        }
    }

    override fun onImageTextViewLongClicked() {

    }

    override fun onImageTextRadioButtonClicked(isChecked: Boolean) {
        imageTextDataModel?.isChecked = isChecked
        if (imageTextDataModel!!.isChecked) {
            imageTextViewInterface.setRadioButtonChecked()
        } else {
            imageTextViewInterface.setRadioButtonUnChecked()
        }
        if(imageTextViewRadioButtonClickListenerWeakReference?.get() != null){
            imageTextViewRadioButtonClickListenerWeakReference?.get()?.onImageTextViewRadioButtonClicked(imageTextDataModel!!.id, imageTextDataModel!!.isChecked)
        }
    }

    override fun errorOnLoadingImage() {
        if(errorImageId != 0 && contextWeakReference.get() != null){
            val bitmap: Bitmap = ImageUtils.decodeSampledBitmapFromResource(contextWeakReference.get()!!.resources, errorImageId, 500, 500)
//            imageTextViewInterface.setErrorImage(bitmap)
        }
    }

    override fun getView(): View {
        return imageTextViewInterface as View
    }

    override fun setSelected(isSelected: Boolean) {
        if (isSelected) {
//            select radio button and change the in data model isRadioButtonChecked true
            imageTextViewInterface.setRadioButtonChecked()

        } else {
            imageTextViewInterface.setRadioButtonUnChecked()
        }
        imageTextDataModel?.isChecked = isSelected
    }

    override fun getIsSelected(): Boolean {
        return imageTextDataModel!!.isChecked
    }

    override fun showRadioButton() {
        imageTextDataModel?.isCheckBoxVisible = true
        imageTextViewInterface.setRadioButtonVisibility(View.VISIBLE)
    }

    override fun hideRadioButton() {
        imageTextDataModel?.isCheckBoxVisible = false
        imageTextViewInterface.setRadioButtonVisibility(View.GONE)
    }

    override fun getId(): Int {
        return imageTextDataModel!!.id
    }

    override fun onDestroy() {
        imageTextViewInterface.onDestroy()
    }
}
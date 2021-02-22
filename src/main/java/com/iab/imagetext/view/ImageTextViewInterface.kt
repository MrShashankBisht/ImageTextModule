package com.iab.imagetext.view

import android.graphics.Bitmap
import android.graphics.Typeface
import android.net.Uri
import android.widget.ImageView

interface ImageTextViewInterface {
    fun prepareView();

    //    Text View Properties
    fun setImageTextViewTextSize(sizeInDp: Float)
    fun setImageTextViewTextColor(textColor: Int)
    fun setImageTextViewTextFontFace(typeface: Typeface)
    fun setImageTextViewTextAllCap(isTextAllCap: Boolean)
    fun setImageTextTextViewText(text: String)
    fun setImageTextTextVisibility(visibility: Int)

    //    View Properties
    fun setImageTextViewPadding(padding: Int)
    fun setViewDimensionWidth(viewWidth: Int)
    fun setViewDimensionHeight(viewHeight: Int)
    fun setViewHeightWidth(viewWidth: Int, viewHeight: Int)
    fun setImageTextViewBackgroundColor(backgroundColor: Int)

    //    ImageView Properties
    fun setImageUsingDrawable(drawableId: Int, placeHolderId: Int, errorImageId: Int)
    fun setImageUsingBitmap(bitmap: Bitmap?, placeHolderId: Int, errorImageId: Int)
    fun setImageUsingUri(imageUri: Uri?, placeHolderId: Int, errorImageId: Int)
    fun setImageUsingServerPath(serverPath: String?, placeHolderId: Int, errorImageId: Int)
    fun setImageUsingLocalPath(localPath: String?, placeHolderId: Int, errorImageId: Int)
    fun setImageUsingRawFolder(rawFolderId: Int, placeHolderId: Int, errorImageId: Int)
    fun setImageUsingBytes(bytes: ByteArray?, placeHolderId: Int, errorImageId: Int)
    fun setImageTextImageViewBackgroundColor(backgroundColor: Int)


    fun setImageTextImageViewPadding(padding: Int)
    //    fun setImageTextImageViewMargin(margin: Int)
    fun setImageTextImageViewScaleType(scaleType: ImageView.ScaleType)
    fun setImageTextImageViewVisibility(visibility: Int)

    //    RadioButton Properties
    fun setImageTextRadioButtonPadding(paddingTop: Int, paddingRight: Int, paddingBottom: Int, paddingLeft: Int)
    fun setRadioButtonVisibility(visibility: Int)
    fun setRadioButtonChecked();
    fun setRadioButtonUnChecked();

    fun onDestroy()

}
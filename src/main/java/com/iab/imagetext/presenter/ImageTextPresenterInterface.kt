package com.iab.imagetext.presenter

import android.view.View
import com.iab.imagetext.model.ImageTextDataModel

interface ImageTextPresenterInterface {

    fun createView(imageTextDataModel: ImageTextDataModel)
    fun onImageTextViewClicked()
    fun onImageTextViewLongClicked()
    fun onImageTextRadioButtonClicked(isChecked: Boolean)
    fun errorOnLoadingImage()
    fun getView(): View?
    fun setSelected(isSelected: Boolean)
    fun getIsSelected(): Boolean

    // These Are Required so that Recycler Or Parent Can change how this ImageText will look without changing the data
    fun showRadioButton()
    fun hideRadioButton()
    fun getId(): Int
    fun onDestroy()

//    fun stopLoadingImageAnimation(loadedSuccessfully: Boolean)

    interface
    ImageTextListener {
        fun onImageTextViewClicked(imageTextDataModel: ImageTextDataModel)

    }

    interface ImageTextViewLongClickListener {
        fun onImageTextViewLongClicked(id: Int, isSelected: Boolean)
    }

    interface ImageTextViewRadioButtonClickListener {
        fun onImageTextViewRadioButtonClicked(id: Int, selected: Boolean)
    }

    interface ImageTextErrorListener {
        fun onImageTextErrorThrows(errorName: String, errorMessage: String)
    }

}
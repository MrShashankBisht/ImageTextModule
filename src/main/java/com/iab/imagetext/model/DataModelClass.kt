package com.iab.imagetext.model

import android.graphics.Bitmap
import android.net.Uri
class ImageTextDataModel(){
    constructor(imageTextDataModel: ImageTextDataModel) : this() {
        this.id = imageTextDataModel.id
        this.text = imageTextDataModel.text
        this.bucketName = imageTextDataModel.bucketName
        this.imageType = imageTextDataModel.imageType
        this.imageInDrawable = imageTextDataModel.imageInDrawable
        this.imageInLocalPath = imageTextDataModel.imageInLocalPath
        this.imageUri = imageTextDataModel.imageUri
        this.imageBitmap = imageTextDataModel.imageBitmap
        this.imageInAssetFolder = imageTextDataModel.imageInAssetFolder
        this.imageInRawFolder = imageTextDataModel.imageInRawFolder
        this.imageServerPath = imageTextDataModel.imageServerPath
        this.showAsThumbnail = imageTextDataModel.showAsThumbnail
        this.isChecked = imageTextDataModel.isChecked
        this.isCheckBoxVisible = imageTextDataModel.isCheckBoxVisible

    }
    var id = 0
    var text: String? = null
    var bucketName:String? = null
    var imageType: ImageType = ImageType.NO_IMAGE
    var imageInDrawable: String? = null
    var imageInLocalPath: String? = null
    var imageUri: Uri? = null
    var imageBitmap: Bitmap? = null
    var imageInAssetFolder: String? = null
    var imageInRawFolder: String? = null
    var imageServerPath: String? = null
    var showAsThumbnail = false

    //    required state
    var isChecked = false
    var isCheckBoxVisible = false

    fun copyImageTextDataModel(imageTextDataModel: ImageTextDataModel): ImageTextDataModel{
        this.id = imageTextDataModel.id
        this.text = imageTextDataModel.text
        this.bucketName = imageTextDataModel.bucketName
        this.imageType = imageTextDataModel.imageType
        this.imageInDrawable = imageTextDataModel.imageInDrawable
        this.imageInLocalPath = imageTextDataModel.imageInLocalPath
        this.imageUri = imageTextDataModel.imageUri
        this.imageBitmap = imageTextDataModel.imageBitmap
        this.imageInAssetFolder = imageTextDataModel.imageInAssetFolder
        this.imageInRawFolder = imageTextDataModel.imageInRawFolder
        this.imageServerPath = imageTextDataModel.imageServerPath
        this.showAsThumbnail = imageTextDataModel.showAsThumbnail
        this.isChecked = imageTextDataModel.isChecked
        this.isCheckBoxVisible = imageTextDataModel.isCheckBoxVisible
        return this
    }

    fun resetData(){
        this.id = 0
        this.text = null
        this.bucketName = null
        this.imageType = ImageType.NO_IMAGE
        this.imageInDrawable = null
        this.imageInLocalPath = null
        this.imageUri = null
        this.imageBitmap = null
        this.imageInAssetFolder = null
        this.imageInRawFolder = null
        this.imageServerPath = null
        this.showAsThumbnail = false
    }
}

enum class ImageType{
    NO_IMAGE, DRAWABLE, LOCAL_PATH, URI, BITMAP, ASSETS, RAW, SERVER_PATH
}

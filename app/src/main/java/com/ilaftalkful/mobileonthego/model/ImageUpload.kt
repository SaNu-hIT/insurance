package com.ilaftalkful.mobileonthego.model

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName

data class ImageUpload(val name:String?="image upload") : BaseObservable() {
    @SerializedName("FileTypeExtension")
    var fileTypeExtension:String?=null
    @SerializedName("ImageData")
    var imageData:String?=null
    @SerializedName("OldFileUri")
    var oldFileUri:String?=null
    @SerializedName("FileUri")
    var fileUri:String?=null
    @SerializedName("RelativePath")
    var relativePath:String?=null
    @SerializedName("Module")
    var module:Int?=null
}
package com.ilaftalkful.mobileonthego.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.databinding.BaseObservable

data class AccidentPhotosModel(val name:String?=null) : BaseObservable() {

    var id:Int?=null
    var imageIcon:Drawable?=null
    var imageBitmap: Bitmap?=null
    var sideName:String?=null
    var picFormat:String?=null
    var isImageUploaded:Boolean?=false
}

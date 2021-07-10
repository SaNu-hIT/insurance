package com.ilaftalkful.mobileonthego.customcomponents

import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.databinding.ImagePickDialogAlertBinding
import kotlinx.android.synthetic.main.image_pick_dialog_alert.*
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.databinding.DataBindingUtil

class IlafImagePickAlert : Dialog {

    interface ImageSelectionListener {
        fun imagePickFromCamera()
        fun imagePickFromPhoneGallery()
    }
    constructor(
            context: Context?,
            listener: ImageSelectionListener?,
    ) : super(context!!, R.style.customAlertDialog) {
        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initView()
        setListners(listener)
    }

    private fun setListners(listener: ImageSelectionListener?) {
        tv_cancel.setOnClickListener {
            if (listener != null) {
                dismiss()
            }

        }

        tv_phone_gallery.setOnClickListener {
            if (listener != null) {
                listener.imagePickFromPhoneGallery()
            }
        }
        tv_camera.setOnClickListener {
            if (listener != null) {
                listener.imagePickFromCamera()
            }
        }
    }

    private fun initView() {
        var  imagePickDialogAlertBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.image_pick_dialog_alert,
                null,
                false
        ) as ImagePickDialogAlertBinding
        imagePickDialogAlertBinding.root.let { setContentView(it) }
        setCancelable(false)
    }
}
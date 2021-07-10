package com.ilaftalkful.mobileonthego.customcomponents

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.databinding.AudioRecordViewBinding

class IlafAudioRecorderDialog: Dialog {
    private var posText: String? = null
    private var mContext: Context? = null
    private var msg: String? = null
    private var title: String? = null

    interface IlafDialogListener {
        fun onStopClicked()
        fun onStartClicked()
    }

    /**
     *
     * @param context
     * @param msg
     * @param positiveText
     * @param negativeText
     * @param emerDialogListener
     */
    constructor(
        context: Context?,
        emerDialogListener: IlafDialogListener?
    ) : super(context!!) {
        this.mContext = context
        initView()
        setListners(emerDialogListener)
    }



    /**
     * method to init view
     */
    private fun initView() {
        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val audioRecordViewBinding = DataBindingUtil.inflate<AudioRecordViewBinding>(
            LayoutInflater.from(context),
            R.layout.audio_record_view,
            null,
            false
        )

        setContentView(audioRecordViewBinding.getRoot())
        val positiveBUtton =
            findViewById<View>(R.id.ok_btn) as TextView
        findViewById<View>(R.id.ok_btn).visibility=View.VISIBLE
        findViewById<View>(R.id.cancel_btn).visibility =View.GONE
        setCancelable(false)
    }

    /**
     * method to set listeners
     * @param dialogListener
     */
    private fun setListners(dialogListener: IlafDialogListener?) {
        findViewById<View>(R.id.ok_btn).setOnClickListener {
            if (dialogListener != null) {
                dialogListener.onStartClicked()
                findViewById<View>(R.id.ok_btn).visibility=View.GONE
                findViewById<View>(R.id.cancel_btn).visibility =View.VISIBLE
            }
            getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        findViewById<View>(R.id.cancel_btn).setOnClickListener {
            if (dialogListener != null) {
                dialogListener.onStopClicked()
            }
            getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dismiss()
        }

    }

    override fun show() {
        if (context != null) {
            if (context is Activity) {
                if (!(context as Activity).isFinishing && !(context as Activity).isDestroyed) {
                    super.show()
                }
            } else {
                super.show()
            }
        }
    }
}
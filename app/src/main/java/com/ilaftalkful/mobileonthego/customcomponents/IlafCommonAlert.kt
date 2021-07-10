package com.ilaftalkful.mobileonthego.customcomponents

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.databinding.CustomDialogAlertBinding

class IlafCommonAlert : Dialog {
    private var posText: String? = null
    private var negText: String? = null
    private var mContext: Context? = null
    private var msg: String? = null
    private var title: String? = null

    interface IlafDialogListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
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
        msg: String?,
        positiveText: String?,
        negativeText: String?,
        emerDialogListener: IlafDialogListener?
    ) : super(context!!) {
        this.mContext = context
        posText = positiveText
        negText = negativeText
        this.msg = msg
        initView()
        setListners(emerDialogListener)
    }

    /**
     *
     * @param context
     * @param msg
     * @param positiveText
     * @param negativeText
     * @param emerDialogListener
     * @param title
     */
    constructor(
        context: Context?,
        msg: String?,
        title: String?,
        positiveText: String?,
        negativeText: String?,
        emerDialogListener: IlafDialogListener?
    ) : super(context!!) {
        this.mContext = context
        posText = positiveText
        negText = negativeText
        this.msg = msg
        this.title = title
        initView()
        setListners(emerDialogListener)
    }

    constructor(
        context: Context?,
        positiveText: String?,
        negativeText: String?
    ) : super(context!!) {
        posText = positiveText
        negText = negativeText
        initView()
        setListners(null)
    }


    /**
     * method to init view
     */
    private fun initView() {
        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        val customDialogAlertBinding = DataBindingUtil.inflate<CustomDialogAlertBinding>(
            LayoutInflater.from(context),
            R.layout.custom_dialog_alert,
            null,
            false
        )

        setContentView(customDialogAlertBinding.getRoot())
        val headerTxt =
            findViewById<View>(R.id.header_txt) as TextView
        val mgs_tv =
            findViewById<View>(R.id.msg_text) as TextView
        val positiveBUtton =
            findViewById<View>(R.id.ok_btn) as TextView
        if(title == null) {
            headerTxt.visibility = View.GONE
        }else{
            headerTxt.text = title
        }
        mgs_tv.text = msg
        setCancelable(false)
        positiveBUtton.text = posText
        val negativeBUtton =
            findViewById<View>(R.id.cancel_btn) as TextView
        if (!TextUtils.isEmpty(negText)) {
            negativeBUtton.text = negText
        } else {
            findViewById<View>(R.id.view_divider).visibility=View.GONE
            negativeBUtton.visibility = View.GONE
        }
    }

    /**
     * method to set listeners
     * @param dialogListener
     */
    private fun setListners(dialogListener: IlafDialogListener?) {
        findViewById<View>(R.id.ok_btn).setOnClickListener {
            if (dialogListener != null) {
                dialogListener.onDialogPositiveClick()
            }
            getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dismiss()
        }
        findViewById<View>(R.id.cancel_btn).setOnClickListener {
            if (dialogListener != null) {
                dialogListener.onDialogNegativeClick()
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
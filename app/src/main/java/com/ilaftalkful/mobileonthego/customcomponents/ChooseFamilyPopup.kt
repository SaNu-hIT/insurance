package com.ilaftalkful.mobileonthego.customcomponents

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.databinding.ChooseFamilyLayoutBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.family.Datum
import com.ilaftalkful.mobileonthego.view.payment.RelationShipAdapter

class ChooseFamilyPopup : Dialog , OnItemClickListener {

    private var posText: String? = null
    private var mContext: Context? = null
    var familyList:List<Datum> = ArrayList<Datum>()
    var chooseFamilyLayoutBinding: ChooseFamilyLayoutBinding?=null
    var adapter:RelationShipAdapter?=null
    var familyListSelected = ArrayList<Datum>(1)

    interface IlafDialogListener {
        fun onDialogPositiveClick(familyListSelected: ArrayList<Datum>)
        fun onDialogNegativeClick()
        fun onEditClicked()
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
            emerDialogListener: IlafDialogListener?,
            list: List<Datum>
    ) : super(context!!, R.style.customAlertDialog) {
        this.mContext = context
        this.familyList =list
        initView()
        setListners(emerDialogListener)
    }



    /**
     * method to init view
     */
    private fun initView() {
        getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

         chooseFamilyLayoutBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            R.layout.choose_family_layout,
            null,
            false
        ) as ChooseFamilyLayoutBinding
        setContentView(chooseFamilyLayoutBinding?.root!!)

        setCancelable(false)
        setAdapter()
    }

    private fun setAdapter() {
       adapter = RelationShipAdapter()
        adapter?.onClick = this
        adapter?.setFamilyData(familyList?: emptyList())
        chooseFamilyLayoutBinding?.recyclerView2?.adapter = adapter
    }

    fun updateFamilyList(familyList: ArrayList<Datum>){
        this.familyList=familyList
        adapter?.setFamilyData(familyList)
    }
    /**
     * method to set listeners
     * @param dialogListener
     */
    private fun setListners(dialogListener: IlafDialogListener?) {
        findViewById<View>(R.id.txt_ok).setOnClickListener {
            if (dialogListener != null) {
                familyListSelected.clear()
                for(item in familyList){
                    if(item.isSelected)
                    familyListSelected.add(item)
                }
                dialogListener.onDialogPositiveClick(familyListSelected)
            }
            getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dismiss()
        }
        findViewById<View>(R.id.txt_cancel).setOnClickListener {
            if (dialogListener != null) {
                dialogListener.onDialogNegativeClick()
            }
            getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dismiss()
        }

        findViewById<View>(R.id.txt_edit).setOnClickListener {
            if (dialogListener != null) {
                dialogListener.onEditClicked()
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

    override fun onItemClick(v: View, obj: Any) {
        var data = obj as Datum
        if(data.isSelected){
            data.isSelected=false
            familyListSelected.remove(data)
        }else{
            data.isSelected=true
            familyListSelected.add(data)
        }
        adapter?.notifyDataSetChanged()
    }

}
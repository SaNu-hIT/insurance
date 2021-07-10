package com.ilaftalkful.mobileonthego.view.family

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.bindingAdaptor.BaseViewHolder
import com.ilaftalkful.mobileonthego.databinding.FamilyMemberItemBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.family.Datum
import com.ilaftalkful.mobileonthego.utilities.DateUtil
import kotlin.collections.ArrayList

class FamilyMemberAdapter internal constructor(context: Context?) :
    RecyclerView.Adapter<BaseViewHolder>() {
    var data: List<Datum> = ArrayList<Datum>(1)
    val mInflater: LayoutInflater
    var onClick: OnItemClickListener? = null
    var relations:MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()

    init {
        mInflater = LayoutInflater.from(context)


    }

    var binding: FamilyMemberItemBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.family_member_item, parent, false
        )
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        data.get(position).memberList =relations
        (holder as HeaderViewHolder).bind(data.get(position),onClick!!)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setPlansData(plansDta: ArrayList<Datum>) {
        data = plansDta
        notifyDataSetChanged()
    }

    fun setRelationShip(it: ArrayList<String>) {
        relations.postValue(it)
        notifyDataSetChanged()
    }


    class HeaderViewHolder(binding: FamilyMemberItemBinding?) : BaseViewHolder(binding!!) {
        var itemSimpleBinding = binding
        var name = MutableLiveData<String>()
        fun bind(get: Datum, onItemClickListener: OnItemClickListener) {
            itemSimpleBinding?.data = get
            name.postValue(get.fullName)
            var date =DateUtil.getStringDateToFromat(get.dob)
            itemSimpleBinding?.date=date
            itemSimpleBinding?.callback = onItemClickListener
            if(get.memberList.value!=null){
            itemSimpleBinding?.spinnerview?.setSelection(getSelectionPosition(get))
            }
            itemSimpleBinding?.spinnerview?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    get.relationDescription =  itemSimpleBinding?.spinnerview?.selectedItem.toString()
                    get.relationID = position
                }

            }
        }

        private fun getSelectionPosition(get: Datum): Int {
            var i=0
            if(get.memberList!=null) {
                while (i <= get.memberList?.value!!.size) {
                    if (get.memberList?.value!!.get(i).equals(get.relationDescription)) {
                        return i
                    }
                    i++
                }
            }
            return 0
        }


    }

}

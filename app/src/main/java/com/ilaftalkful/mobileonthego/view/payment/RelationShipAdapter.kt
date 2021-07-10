package com.ilaftalkful.mobileonthego.view.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.databinding.FamilyMemberListItemBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.family.Datum

class RelationShipAdapter : RecyclerView.Adapter<FamilyMemberViewHolder>() {

    var data :List<Datum> = ArrayList<Datum>(1)
    var onClick:OnItemClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamilyMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.family_member_list_item, parent, false) as FamilyMemberListItemBinding
        return FamilyMemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamilyMemberViewHolder, position: Int) {
        (holder as FamilyMemberViewHolder).bind(data.get(position),onClick)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setFamilyData(List: List<Datum>) {
        data=List
        notifyDataSetChanged()

    }
}
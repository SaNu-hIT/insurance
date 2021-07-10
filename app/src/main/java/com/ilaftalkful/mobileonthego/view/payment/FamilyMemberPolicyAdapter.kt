package com.ilaftalkful.mobileonthego.view.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.databinding.FamilyMemberPolicyItemBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.family.Datum

class FamilyMemberPolicyAdapter :RecyclerView.Adapter<FamilyMemberPolicyViewHolder>(){
    var data:List<Datum> =  ArrayList<Datum>(1)
    var onClick:OnItemClickListener?=null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FamilyMemberPolicyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            R.layout.family_member_policy_item, parent, false
        ) as FamilyMemberPolicyItemBinding
        return FamilyMemberPolicyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamilyMemberPolicyViewHolder, position: Int) {
        (holder as FamilyMemberPolicyViewHolder).bind(data.get(position),onClick)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    fun setFamilyMembersSelectedListData(it: List<Datum>) {
        data=it
        notifyDataSetChanged()

    }

}

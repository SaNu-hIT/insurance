package com.ilaftalkful.mobileonthego.view.payment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.databinding.FamilyMemberPolicyItemBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.family.Datum
import com.ilaftalkful.mobileonthego.utilities.DateUtil

class FamilyMemberPolicyViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    var binding = binding as FamilyMemberPolicyItemBinding
    fun bind(get: Datum, onClick: OnItemClickListener?) {
        binding.data =get
        binding.callback=onClick
        binding.date = DateUtil.getStringDateToFromat(get.dob)
    }

}

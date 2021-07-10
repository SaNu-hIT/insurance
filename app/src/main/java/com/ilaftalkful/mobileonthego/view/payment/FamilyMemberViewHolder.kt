package com.ilaftalkful.mobileonthego.view.payment

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.databinding.FamilyMemberListItemBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.family.Datum

class FamilyMemberViewHolder(itemView: ViewDataBinding) : RecyclerView.ViewHolder(itemView.root) {
    var binding = itemView as FamilyMemberListItemBinding
    fun bind(get: Datum, onClick: OnItemClickListener?) {
        binding.data = get
        binding.onClick =onClick
    }

}

package com.ilaftalkful.mobileonthego.view.healthinsurence

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.bindingAdaptor.BaseViewHolder
import com.ilaftalkful.mobileonthego.databinding.AssistanceClaimItemViewBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.health.claimassistance.Datum

class AssistanceClaimAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    var data: List<Datum> =  ArrayList<Datum>(1)
    var onClick:OnItemClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.assistance_claim_item_view, parent, false) as AssistanceClaimItemViewBinding
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder  as HeaderViewHolder).bind(data.get(position),onClick)
    }

    override fun getItemCount(): Int {
      return data.size
    }

    fun setPlansData(plansDta: ArrayList<Datum>) {
        data= plansDta
        notifyDataSetChanged()
    }


     class  HeaderViewHolder(binding: ViewDataBinding): BaseViewHolder(binding) {
       var itemSimpleBinding= binding as  AssistanceClaimItemViewBinding

        fun bind(get: Datum, onClick: OnItemClickListener?) {
            itemSimpleBinding?.data=get
            itemSimpleBinding.onClick =onClick
        }

    }

}

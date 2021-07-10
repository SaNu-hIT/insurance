package com.ilaftalkful.mobileonthego.view.buyinsurance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.bindingAdaptor.BaseViewHolder
import com.ilaftalkful.mobileonthego.databinding.TravelPlansItemViewBinding
import com.ilaftalkful.mobileonthego.model.TravelPlansResponse

class TravelPlansAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    var data: List<TravelPlansResponse> =  ArrayList<TravelPlansResponse>(1)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.travel_plans_item_view, parent, false) as TravelPlansItemViewBinding
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder  as HeaderViewHolder).bind(data.get(position))
    }

    override fun getItemCount(): Int {
      return data.size
    }

    fun setPlansData(plansDta: ArrayList<TravelPlansResponse>) {
        data= plansDta
        notifyDataSetChanged()
    }


     class  HeaderViewHolder(binding: ViewDataBinding): BaseViewHolder(binding) {
       var itemSimpleBinding= binding as  TravelPlansItemViewBinding

        fun bind(get: TravelPlansResponse) {
            itemSimpleBinding?.data=get
        }

    }

}

package com.ilaftalkful.mobileonthego.view.healthinsurence

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.bindingAdaptor.BaseViewHolder
import com.ilaftalkful.mobileonthego.databinding.HospitalNetworkItemViewBinding
import com.ilaftalkful.mobileonthego.model.health.hospital.HospitalNetwork

class HospitalNetworkAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    var data: List<HospitalNetwork> =  ArrayList<HospitalNetwork>(1)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.hospital_network_item_view, parent, false) as HospitalNetworkItemViewBinding
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder  as HeaderViewHolder).bind(data.get(position))
    }

    override fun getItemCount(): Int {
      return data.size
    }

    fun setPlansData(plansDta: ArrayList<HospitalNetwork>) {
        data= plansDta
        notifyDataSetChanged()
    }


     class  HeaderViewHolder(binding: ViewDataBinding): BaseViewHolder(binding) {
       var itemSimpleBinding= binding as  HospitalNetworkItemViewBinding

        fun bind(get: HospitalNetwork) {
            itemSimpleBinding?.data=get
        }

    }

}

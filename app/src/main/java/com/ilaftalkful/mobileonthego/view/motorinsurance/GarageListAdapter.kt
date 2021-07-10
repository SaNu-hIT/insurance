package com.ilaftalkful.mobileonthego.view.motorinsurance

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.bindingAdaptor.BaseViewHolder
import com.ilaftalkful.mobileonthego.databinding.GarageListItemBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.Garage

class GarageListAdapter : RecyclerView.Adapter<GarageListAdapter.GarageListViewwHolder>() {

    var data :List<Garage> = ArrayList<Garage>(1)
    var onClick:OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GarageListViewwHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
            R.layout.garage_list_item, parent, false) as GarageListItemBinding
        return GarageListViewwHolder(binding)
    }

    override fun onBindViewHolder(holder: GarageListViewwHolder, position: Int) {
        (holder as GarageListViewwHolder).bind(data.get(position),onClick)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setDataValues(garages: List<Garage>) {
        data= garages
        notifyDataSetChanged()
    }

    class GarageListViewwHolder(binding: ViewDataBinding) : BaseViewHolder(binding) {
        var bindig =  binding as GarageListItemBinding
        fun bind(get: Garage, onClick: OnItemClickListener?) {
            bindig.data = get
            bindig.onClick=onClick
            bindig.ivIcon.setImageURI(Uri.parse(get.logoFilePath))
        }

    }
}


package com.ilaftalkful.mobileonthego.view.mylog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.bindingAdaptor.BaseViewHolder
import com.ilaftalkful.mobileonthego.databinding.MyLogItemBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.log.ModuleDetail

class MyLogAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    var onClick:OnItemClickListener?=null
    var data: List<ModuleDetail> =  ArrayList<ModuleDetail>(1)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.my_log_item, parent, false) as MyLogItemBinding
        return HeaderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder  as HeaderViewHolder).bind(data.get(position),onClick)
    }

    override fun getItemCount(): Int {
      return data.size
//      return 10
    }

    fun setPlansData(plansDta: List<ModuleDetail>) {
        data= plansDta
        notifyDataSetChanged()
    }


     class  HeaderViewHolder(binding: ViewDataBinding): BaseViewHolder(binding) {
       var itemSimpleBinding= binding as  MyLogItemBinding

        fun bind(get: ModuleDetail, onClick: OnItemClickListener?) {
            itemSimpleBinding.data = get
            itemSimpleBinding.onClick =onClick
        }

    }

}

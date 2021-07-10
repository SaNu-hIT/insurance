package com.ilaftalkful.mobileonthego.bindingAdaptor

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    fun addItems(items: ArrayList<T>) {

    }


}

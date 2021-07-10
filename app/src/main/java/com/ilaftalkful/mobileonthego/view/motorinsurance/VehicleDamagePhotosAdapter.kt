package com.ilaftalkful.mobileonthego.view.motorinsurance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ilaftalkful.mobileonthego.R
import com.ilaftalkful.mobileonthego.bindingAdaptor.BaseViewHolder
import com.ilaftalkful.mobileonthego.databinding.MotorClaimImagesViewItemBinding
import com.ilaftalkful.mobileonthego.listener.OnItemClickListener
import com.ilaftalkful.mobileonthego.model.AccidentPhotosModel

class VehicleDamagePhotosAdapter : RecyclerView.Adapter<VehicleDamagePhotosAdapter.VehicleDamagePhotoViewHolder>() {
    var data: List<AccidentPhotosModel> =  ArrayList<AccidentPhotosModel>(1)
    var onClick:OnItemClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleDamagePhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater,
                R.layout.motor_claim_images_view_item, parent, false) as MotorClaimImagesViewItemBinding
        return VehicleDamagePhotoViewHolder(binding)
    }
    override fun onBindViewHolder(holder: VehicleDamagePhotoViewHolder, position: Int) {
        (holder  as VehicleDamagePhotoViewHolder).bind(data.get(position),onClick)
    }
    override fun getItemCount(): Int {
        return data.size
    }
    fun setPlansData(accidentPhotosModel: ArrayList<AccidentPhotosModel>) {
        data= accidentPhotosModel
        notifyDataSetChanged()
    }
    open class VehicleDamagePhotoViewHolder(binding: ViewDataBinding) :BaseViewHolder(binding) {
        var binding = binding as MotorClaimImagesViewItemBinding
        fun bind(get: AccidentPhotosModel, onClick: OnItemClickListener?) {
            binding.data = get
            if(get.imageBitmap!=null){
                    Glide.with(binding.imgCam.context)
                        .load(get.imageBitmap)
                        .into(binding.imgCam)
            }else{
                binding.imgCam.setImageDrawable(get.imageIcon)
            }
            binding.onClick = onClick
        }

    }
}

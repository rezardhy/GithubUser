package com.reza.favouriteapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.target.Target
import com.reza.favouriteapp.databinding.RvLayoutBinding
import com.reza.favouriteapp.model.FavUser

class FavAdapter(private val favUser: ArrayList<FavUser>):RecyclerView.Adapter<FavAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavUser)
    }

    inner class ListViewHolder(private val binding: RvLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(favUser: FavUser) {
            with(binding){
                Glide.with(itemView.context)
                        .load(favUser.photo)
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL)
                        .into(imgItemPhoto)
                tvItemName.text = favUser.username
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(favUser) }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = RvLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) = holder.bind(favUser[position])
    override fun getItemCount(): Int = favUser.size


}
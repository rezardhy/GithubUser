package com.reza.submission2bfaa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.request.target.Target
import com.reza.submission2bfaa.model.User
import com.reza.submission2bfaa.databinding.RvLayoutBinding

class RVAdapter(private val listUser: ArrayList<User>): RecyclerView.Adapter<RVAdapter.ListViewHolder>() {



    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }

    inner class ListViewHolder(private val binding: RvLayoutBinding ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            with(binding){
                Glide.with(itemView.context)
                        .load(user.photo)
                        .fitCenter()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(Target.SIZE_ORIGINAL)
                        .into(imgItemPhoto)
                tvItemName.text = user.username
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }

                }
            }

    }




    fun addItemFav(user: User) {
        this.listUser.add(user)
        notifyItemInserted(this.listUser.size - 1)
    }

    fun removeItemFav(position: Int) {
        this.listUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUser.size)
    }




    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = RvLayoutBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ListViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) = holder.bind(listUser[position])
    override fun getItemCount(): Int = listUser.size

}

package com.app.mobiletestsuitmedia.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.mobiletestsuitmedia.data.response.DataItem
import com.app.mobiletestsuitmedia.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class UserAdapter(private val listener: OnUserClickListener) : PagingDataAdapter<DataItem, UserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user,listener)
        }
    }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataItem,listener: OnUserClickListener){
            binding.tvName.text = buildString {
                append(user.firstName)
                append(" ")
                append(user.lastName)
            }
            binding.tvEmail.text = user.email.toString()
            Glide.with(itemView.context)
                .load(user.avatar)
                .centerCrop()
                .into(binding.ivAvatarUser)

            itemView.setOnClickListener {
                listener.onUserClick(user)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
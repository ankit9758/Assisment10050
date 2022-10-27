package com.neostardemo.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.service.autofill.UserData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.neostardemo.R
import com.neostardemo.databinding.ItemUserListBinding
import com.neostardemo.models.User

class UserListAdapter(private val userInteraction: UserInteraction) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {
    private var listItems = ArrayList<User>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val binding = DataBindingUtil.inflate<ItemUserListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_user_list, parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listItems[position])
        holder.itemView.setOnClickListener {
            userInteraction.onUserClicked(listItems[position])
        }


    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUserList(listItems: List<User>) {
        this.listItems = listItems as ArrayList<User> /* = java.util.ArrayList<com.neostardemo.models.User> */
        notifyDataSetChanged()
    }

    class UserViewHolder(private val itemBinding: ItemUserListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: User) {
            itemBinding.user=item
            itemBinding.ivUser.setImageURI(Uri.parse(item.avatar))
            itemBinding.executePendingBindings()

        }
    }
    interface UserInteraction {
        fun onUserClicked(user: User)
    }
}
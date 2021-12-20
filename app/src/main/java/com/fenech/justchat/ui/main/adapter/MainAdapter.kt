package com.fenech.justchat.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.fenech.justchat.R
import com.fenech.justchat.data.model.DataChat
import com.fenech.justchat.databinding.ItemMainFragmentBinding
import kotlinx.android.synthetic.main.item_main_fragment.view.*

class MainAdapter(private val dataChats: ArrayList<DataChat>) :
    RecyclerView.Adapter<MainAdapter.DataChatHolder>() {

    companion object {
        private lateinit var clickListener: ClickListener
        private lateinit var longClickListener: ClickListener
    }

    class DataChatHolder(private val binding: ItemMainFragmentBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {
        fun bind(dataChat: DataChat) {
            binding.dataChat = dataChat
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
            binding.executePendingBindings()
        }

        override fun onClick(v: View?) {
            clickListener.onItemClick(adapterPosition, v)
        }

        override fun onLongClick(v: View?): Boolean {
            longClickListener.onItemClick(adapterPosition, v)
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataChatHolder {
        return DataChatHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_main_fragment, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: DataChatHolder, position: Int) {
        holder.bind(dataChats[position])
    }

    override fun getItemCount(): Int {
        return dataChats.size
    }

    fun addData(list: List<DataChat>) {
        dataChats.clear()
        dataChats.addAll(list)
    }

    fun getId(position: Int): String {
        return dataChats[position].id
    }

    fun getAuthor(position: Int): String {
        return dataChats[position].author
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        MainAdapter.clickListener = clickListener
    }

    fun setOnItemLongClickListener(clickListener: ClickListener) {
        longClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
    }
}
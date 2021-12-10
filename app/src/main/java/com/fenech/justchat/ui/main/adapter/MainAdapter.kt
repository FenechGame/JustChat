package com.fenech.justchat.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fenech.justchat.R
import com.fenech.justchat.data.model.Chat
import kotlinx.android.synthetic.main.item_main_fragment.view.*

class MainAdapter(private val chats: ArrayList<Chat>) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chat: Chat) {
            itemView.tvNameChat.text = chat.name
            itemView.tvTextChat.text = chat.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_main_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    fun addData(list: List<Chat>) {
        chats.clear()
        chats.addAll(list)
    }
}
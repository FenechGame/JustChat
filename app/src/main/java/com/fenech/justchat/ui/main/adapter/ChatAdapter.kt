package com.fenech.justchat.ui.main.adapter

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fenech.justchat.R
import com.fenech.justchat.data.model.DataChat
import com.fenech.justchat.data.model.DataMessage
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_chat_fragment.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(private val dataChat: ArrayList<DataMessage>) :
    RecyclerView.Adapter<ChatAdapter.DataViewHolder>() {

    companion object {
        private lateinit var clickListener: ClickListener
        private lateinit var longClickListener: ClickListener

        @SuppressLint("SimpleDateFormat")
        private val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
        View.OnLongClickListener {
        fun bind(dataMessage: DataMessage) {
            itemView.tvMessage.text = dataMessage.text
            itemView.tvTime.text = simpleDateFormat.format(dataMessage.timestamp.toLong())
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
            if (dataMessage.userId == FirebaseAuth.getInstance().uid) {
                itemView.tvMessage.gravity = Gravity.END
                itemView.tvTime.gravity = Gravity.END
            } else {
                itemView.tvMessage.gravity = Gravity.START
                itemView.tvTime.gravity = Gravity.START
            }
        }

        override fun onClick(v: View?) {
            clickListener.onItemClick(adapterPosition, v)
        }

        override fun onLongClick(v: View?): Boolean {
            longClickListener.onItemClick(adapterPosition, v)
            return true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat_fragment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataChat[position])
    }

    override fun getItemCount(): Int {
        return dataChat.size
    }

    fun addData(list: List<DataMessage>) {
        dataChat.clear()
        dataChat.addAll(list)
    }

    fun getId(position: Int): String {
        return dataChat[position].messageId
    }

    fun getAuthor(position: Int): String {
        return dataChat[position].userId
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        ChatAdapter.clickListener = clickListener
    }

    fun setOnItemLongClickListener(clickListener: ClickListener) {
        longClickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(position: Int, v: View?)
    }
}
package com.fenech.justchat.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenech.justchat.R
import com.fenech.justchat.data.api.FirebaseApi
import com.fenech.justchat.data.model.DataChat
import com.fenech.justchat.data.model.DataMessage
import com.fenech.justchat.ui.base.ViewModelFactory
import com.fenech.justchat.ui.main.adapter.ChatAdapter
import com.fenech.justchat.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.chat_fragment.*
import kotlinx.android.synthetic.main.main_fragment.rvMessages

class ChatFragment : Fragment() {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel =
            ViewModelProvider(this, ViewModelFactory(FirebaseApi())).get(MainViewModel::class.java)
        if (requireActivity().intent.hasExtra("ID"))
            mainViewModel.openChat(requireActivity().intent.getStringExtra("ID").toString())
        setupUI()
        setupObserver()
        addButtonListeners()
    }

    private fun setupUI() {
        rvMessages.layoutManager = LinearLayoutManager(requireContext())
        adapter = ChatAdapter(arrayListOf())
        adapter.setOnItemClickListener(object : ChatAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {

            }
        })
        rvMessages.addItemDecoration(
            DividerItemDecoration(
                rvMessages.context,
                (rvMessages.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvMessages.adapter = adapter
    }

    private fun setupObserver() {
        mainViewModel.getChatLiveData().observe(viewLifecycleOwner, {
            renderList(it)
        })
    }

    private fun renderList(dataMessage: List<DataMessage>) {
        adapter.addData(dataMessage)
        adapter.notifyDataSetChanged()
    }

    private fun addButtonListeners() {
        btSend.setOnClickListener {
            mainViewModel.outputMessage(etTextMessage.text.toString())
            etTextMessage.text.clear()
        }
    }
}
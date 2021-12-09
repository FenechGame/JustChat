package com.fenech.justchat.ui.main.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenech.justchat.R
import com.fenech.justchat.data.api.FirebaseApi
import com.fenech.justchat.data.model.Chat
import com.fenech.justchat.ui.base.ViewModelFactory
import com.fenech.justchat.ui.main.adapter.MainAdapter
import com.fenech.justchat.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(FirebaseApi()))
            .get(MainViewModel::class.java)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        rvChats.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(arrayListOf())
        rvChats.addItemDecoration(
            DividerItemDecoration(
                rvChats.context,
                (rvChats.layoutManager as LinearLayoutManager).orientation
            )
        )
        rvChats.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.getChats().observe(viewLifecycleOwner, {
            renderList(it)
        })
    }

    private fun renderList(chats: List<Chat>) {
        adapter.addData(chats)
        adapter.notifyDataSetChanged()
    }
}
package com.fenech.justchat.ui.main.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fenech.justchat.R
import com.fenech.justchat.data.api.FirebaseApi
import com.fenech.justchat.data.model.DataChat
import com.fenech.justchat.ui.base.ViewModelFactory
import com.fenech.justchat.ui.main.adapter.MainAdapter
import com.fenech.justchat.ui.main.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel =
            ViewModelProvider(this, ViewModelFactory(FirebaseApi())).get(MainViewModel::class.java)
        setupUI()
        setupObserver()
        addButtonListeners()
    }

    private fun setupUI() {
        rvMessages.layoutManager = LinearLayoutManager(requireContext())
        adapter = MainAdapter(arrayListOf())
        adapter.setOnItemClickListener(object : MainAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                val intent = Intent(activity, ChatActivity::class.java)
                intent.putExtra("ID", adapter.getId(position))
                startActivity(intent)
            }
        })
        adapter.setOnItemLongClickListener(object : MainAdapter.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                val builder = AlertDialog.Builder(v!!.context)
                builder.setTitle("Удалить чат?")
                    .setMessage("Подтвердите удаление чата")
                    .setPositiveButton("Да") { dialog, id ->
                        mainViewModel.deleteChat(adapter.getId(position))
                        dialog.cancel()
                    }
                    .setNegativeButton("Нет") { dialog, id ->
                        dialog.cancel()
                    }
                builder.create()
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
        mainViewModel.getChatsListLiveData().observe(viewLifecycleOwner, {
            renderList(it)
        })
    }

    private fun renderList(dataChats: List<DataChat>) {
        adapter.addData(dataChats)
        adapter.notifyDataSetChanged()
    }

    private fun addButtonListeners() {
        btNewChat.setOnClickListener {
            frameNewChat.visibility = View.VISIBLE
            btNewChat.visibility = View.INVISIBLE
        }
        btCreateChat.setOnClickListener {
            val nameChat = etNameChat.text.toString()
            if (nameChat.isEmpty())
                return@setOnClickListener
            frameNewChat.visibility = View.GONE
            btNewChat.visibility = View.VISIBLE
            mainViewModel.createNewChat(nameChat)
        }
    }
}
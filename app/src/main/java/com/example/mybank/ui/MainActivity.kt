package com.example.mybank.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybank.data.model.Account
import com.example.mybank.databinding.ActivityMainBinding
import com.example.mybank.databinding.DialogAddBinding
import com.example.mybank.ui.viewModel.AccountViewModel
import com.example.mybank.ui.viewModel.adapter.AcсountsAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AcсountsAdapter

    private val viewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        subscribeToLiveData()

        binding.bthAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun subscribeToLiveData() {
        viewModel.accounts.observe(this) { accounts ->
            adapter = AcсountsAdapter(accounts)
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter
        }
    }

    private fun showAddDialog() {
        val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
        with(binding) {
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Добавление нового счета")
                .setView(binding.root)
                .setPositiveButton("Добавить") { _, _ ->
                    val account = Account(
                        name = etName.text.toString(),
                        balance = etBalance.text.toString().toInt(),
                        currency = etCurrency.text.toString()
                    )
                    viewModel.addAccount(account)
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadAccounts()
    }

    private fun initAdapter() {
        adapter = AcсountsAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}
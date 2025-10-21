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
import com.example.mybank.ui.viewModel.adapter.AcoountsAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AcoountsAdapter
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

    private fun subscribeToLiveData(){
        viewModel.accounts.observe(this){
            adapter.submitList(it)
        }
    }

    private fun showAddDialog(){
        val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
        with(binding){
            AlertDialog.Builder(this@MainActivity)
                .setTitle("Добовление нового счета")
                .setView(binding.root)
                .setPositiveButton("Добавить") {_,_ ->
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

    private fun initAdapter() = with(binding){
        adapter = AcoountsAdapter(
            onEdit = {
                showEditDialog(it)
            },
            onSwitchToggle = { id, isChecked ->
                viewModel.updateAccountPartially(id, isChecked)
            },
            onDelete = {
                showDeleteDialog(it)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView.adapter = adapter
    }

    private fun showDeleteDialog(id: String){
        AlertDialog.Builder(this)
            .setTitle("Вы уверены?")
            .setMessage("Удалить счет с идентификатором - $id?")
            .setPositiveButton("Удалить"){_,_ ->
                viewModel.deleteAccount(id)
            }
            .setNegativeButton("Отмена"){_,_ ->

            }.show()
    }

    private fun showEditDialog(account: Account) {
        val binding = DialogAddBinding.inflate(LayoutInflater.from(this))
        with(binding){

            account.run {

                etName.setText(name)
                etBalance.setText(balance.toString())
                etCurrency.setText(currency)

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Изменение счета")
                    .setView(binding.root)
                    .setPositiveButton("Изменить") {_,_ ->

                        val updatedAccount = account.copy(
                            name = etName.text.toString(),
                            balance = etBalance.text.toString().toInt(),
                            currency = etCurrency.text.toString()
                        )
                        viewModel.updateAccountFully(updatedAccount)
                    }
                    .show()
            }
        }

    }
}
package com.example.mybank.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mybank.data.model.Account
import com.example.mybank.databinding.ActivityMainBinding
import com.example.mybank.databinding.DialogAddBinding
import com.example.mybank.domain.presenter.AccountContracts
import com.example.mybank.domain.presenter.AccountPresenter
import com.example.mybank.ui.adapter.AcoountsAdapter

class MainActivity : AppCompatActivity(), AccountContracts.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: AcoountsAdapter
    private lateinit var presenter: AccountPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdapter()
        presenter = AccountPresenter(this)

        binding.bthAdd.setOnClickListener {
            showAddDialog()
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
                    presenter.addAccount(account)
                }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadAccounts()
    }

    private fun initAdapter() = with(binding){
        adapter = AcoountsAdapter(
            onEdit = {
                showEditDialog(it)
            },
            onSwitchToggle = {id, isChecked ->
                presenter.updateAccountPartially(id, isChecked)
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
                presenter.deleteAccount(id)
            }
            .setNegativeButton("Отмена"){_,_ ->

            }.show()
    }

    private fun showEditDialog(account:Account) {
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
                        presenter.updateAccountFully(updatedAccount)
                    }
                    .show()
            }
        }

    }

    override fun showAccounts(accountList: List<Account>) {
        adapter.submitList(accountList)
    }

}
package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mybank.AccountDetailsViewModel
import com.example.mybank.databinding.ActivityAccountDetailsBinding
import com.example.mybank.databinding.DialogAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountDetailsBinding
    private val viewModel: AccountDetailsViewModel by viewModels()
    private var accountId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        accountId = intent.getStringExtra("account_id") ?: ""

        observeViewModel()
        viewModel.loadAccount(accountId)

        binding.btnEdit.setOnClickListener { showEditDialog() }
        binding.btnDelete.setOnClickListener { showDeleteDialog() }
    }

    private fun observeViewModel() {
        viewModel.account.observe(this) { account ->
            binding.tvName.text = account.name
            binding.tvBalance.text = "${account.balance} ${account.currency}"
        }

        viewModel.error.observe(this) { msg ->
            Toast.makeText(this, msg ?: "Ошибка", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditDialog() {
        val dialogBinding = DialogAddBinding.inflate(layoutInflater)
        viewModel.account.value?.let { account ->
            dialogBinding.etName.setText(account.name)
            dialogBinding.etBalance.setText(account.balance.toString())
            dialogBinding.etCurrency.setText(account.currency)

            AlertDialog.Builder(this)
                .setTitle("Изменение счёта")
                .setView(dialogBinding.root)
                .setPositiveButton("Изменить") { _, _ ->
                    val updated = account.copy(
                        name = dialogBinding.etName.text.toString(),
                        balance = dialogBinding.etBalance.text.toString().toInt(),
                        currency = dialogBinding.etCurrency.text.toString()
                    )

                    viewModel.updateAccount(updated)
                    Toast.makeText(this, "Счёт обновлён", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle("Удаление счёта")
            .setMessage("Вы уверены, что хотите удалить этот счёт?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteAccount(accountId) {
                    Toast.makeText(this, "Счёт удалён", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
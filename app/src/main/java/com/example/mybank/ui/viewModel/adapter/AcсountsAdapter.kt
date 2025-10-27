package com.example.mybank.ui.viewModel.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.AccountDetailsActivity
import com.example.mybank.data.model.Account
import com.example.mybank.databinding.ItemAccountBinding

class AcсountsAdapter(
    private val accounts: List<Account>
): RecyclerView.Adapter<AcсountsAdapter.AccountViewHolder>(){

    inner class AccountViewHolder(private val binding: ItemAccountBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(account: Account) = with(binding) {
            tvName.text = account.name
            tvBalance.text = "${account.balance} ${account.currency}"

            root.setOnClickListener {
                val context = it.context
                val intent = Intent(context, AccountDetailsActivity::class.java)
                intent.putExtra("account_id", account.id)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(accounts[position])
    }

    override fun getItemCount() = accounts.size
}
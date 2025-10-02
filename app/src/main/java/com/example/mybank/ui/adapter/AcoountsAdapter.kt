package com.example.mybank.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mybank.data.model.Account
import com.example.mybank.databinding.ItemAccountBinding

class AcoountsAdapter: RecyclerView.Adapter<AcoountsAdapter.AccountViewHolder>(){

    private val items = arrayListOf<Account>()

    fun submitList(data: List<Account>){
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AcoountsAdapter.AccountViewHolder {
        val binding = ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AcoountsAdapter.AccountViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class AccountViewHolder(private val binding: ItemAccountBinding):
        RecyclerView.ViewHolder(binding.root){
        fun  bind(account: Account) = with(binding){
            tvName.text = account.name
            val text = "${account.balance.toString()} ${account.currency}"
            tvBalance.text = text
        }
    }
}

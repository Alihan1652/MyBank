package com.example.mybank.domain.presenter

import com.example.mybank.data.model.Account

interface AccountContracts {
    interface View {
        fun showAccounts(accountList: List<Account>)
    }
    interface Presenter{
        fun loadAccounts()
    }
}
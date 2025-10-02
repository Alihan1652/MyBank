package com.example.mybank.domain.presenter

import com.example.mybank.data.model.Account

class AccountPresenter(private val view: AccountContracts.View): AccountContracts.Presenter {
    override fun loadAccounts() {
        val testMockList = arrayListOf<Account>()
        testMockList.add(Account(
            name = "o!Bank",
            balance = 1000,
            currency = "KGS"
        ))
        testMockList.add(Account(
            name = "m Bank",
            balance = 100,
            currency = "USD"
        ))
        testMockList.add(Account(
            name = "optima Bank",
            balance = 10,
            currency = "EUR"
        ))
        view.showAccounts(testMockList)
    }
}
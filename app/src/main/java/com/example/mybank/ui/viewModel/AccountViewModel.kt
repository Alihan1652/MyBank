package com.example.mybank.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybank.data.model.Account
import com.example.mybank.data.model.AccountState
import com.example.mybank.data.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountViewModel(): ViewModel() {

    private val _accounts = MutableLiveData<List<Account>>()
    val accounts: LiveData<List<Account>> = _accounts

    fun loadAccounts() {
        ApiClient.accountsApi.getAccounts()
            .handleAccountResponse(
                onSuccess = { _accounts.value = it
                }
            )

        }

    fun addAccount(account: Account) {
        ApiClient.accountsApi.addAccount(account).handleAccountResponse()
    }

    fun updateAccountFully(updatedAccount: Account) {
        updatedAccount.id?.let {
            ApiClient.accountsApi.updateAccountFully(it, updatedAccount).handleAccountResponse()
        }
    }

    fun updateAccountPartially(id: String, isChecked: Boolean) {
        ApiClient.accountsApi
            .updateAccountPartially(id, AccountState(isChecked)).handleAccountResponse()
    }

     fun deleteAccount(id: String) {
         ApiClient.accountsApi.deleteAccount(id).handleAccountResponse()
    }
    private fun <T>Call<T>?.handleAccountResponse(
        onSuccess: (T) -> Unit = { loadAccounts() },
        onError: (String) -> Unit = {}
    ) {
        this?.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val result = response.body()
                if (result != null && response.isSuccessful) {
                    onSuccess(result)
                } else {
                    onError(response.code().toString())
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onError(t.message.toString())
            }

        }

        )
    }
}
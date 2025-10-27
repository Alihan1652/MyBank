package com.example.mybank

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mybank.data.model.Account
import com.example.mybank.data.network.AccountDetailsApi
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountDetailsViewModel @Inject constructor(
    private val api: AccountDetailsApi
) : ViewModel() {

    private val _account = MutableLiveData<Account>()
    val account: LiveData<Account> get() = _account

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun loadAccount(id: String) {
        api.getAccountById(id).enqueue(object : retrofit2.Callback<Account> {
            override fun onResponse(
                call: Call<Account?>,
                response: retrofit2.Response<Account?>
            ) {
                if (response.isSuccessful) {
                    _account.postValue(response.body())
                } else {
                    _error.postValue("Ошибка при загрузке данных")
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                _error.postValue(t.localizedMessage)
            }
        })
    }

    fun updateAccount(account: Account) {
        account.id?.let { id ->
            api.updateAccount(id, account).enqueue(object : Callback<Account> {
                override fun onResponse(call: Call<Account>, response: Response<Account>) {
                    if (response.isSuccessful) {
                        _account.postValue(response.body())
                    } else {
                        _error.postValue("Ошибка при обновлении")
                    }
                }

                override fun onFailure(call: Call<Account>, t: Throwable) {
                    _error.postValue(t.localizedMessage)
                }
            })
        }
    }

    fun deleteAccount(id: String, onSuccess: () -> Unit){
        api.deleteAccount(id).enqueue(object : Callback<Unit> {
            override fun onResponse(
                call: Call<Unit?>,
                response: Response<Unit?>
            ) {
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    _error.value = "Ошибка удаления: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<Unit?>, t: Throwable) {
                _error.value = "Ошибка сети: ${t.message}"
            }
        })
}
}









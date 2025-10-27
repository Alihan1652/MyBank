package com.example.mybank.data.network

import com.example.mybank.data.model.Account
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface AccountDetailsApi {
    @GET("accounts/{id}")
    fun getAccountById(@Path("id") id: String): Call<Account>

    @PUT("accounts/{id}")
    fun updateAccount(
        @Path("id") id: String,
        @Body account: Account
    ): Call<Account>

    @DELETE("accounts/{id}")
    fun deleteAccount(@Path("id") id: String): Call<Unit>
}
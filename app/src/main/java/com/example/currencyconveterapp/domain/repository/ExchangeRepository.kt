package com.example.currencyconveterapp.domain.repository

import com.example.currencyconveterapp.domain.model.Currency

interface ExchangeRepository {

    suspend fun convert(
        fromCurrency: String,
        toCurrency: String,
        amount : Double
    ):Double

    suspend fun  getAllCurrencies() : List<Currency>
}
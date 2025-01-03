package com.example.currencyconveterapp.presentation.exchange

import com.example.currencyconveterapp.domain.model.Currency

data class ExchangeState(
    val from : Currency =  Currency("United States Dollar", "USD"),
    val to : Currency = Currency("Euro", "EUR"),
    val amount : String = "",
    val result : String = "",
    val allCurrencyList : List<Currency> = mutableListOf(),
    val isInitialState : Boolean = true
)
package com.example.currencyconveterapp.domain.usecase

import com.example.currencyconveterapp.domain.repository.ExchangeRepository

class ConvertUserCase(
    private val exchangeRepository: ExchangeRepository
) {

    suspend operator fun invoke( fromCurrency: String, toCurrency: String, amount: String ): String {

        if (fromCurrency.isBlank() || toCurrency.isBlank() || amount.isBlank()) return ""

        if (fromCurrency == toCurrency) return  amount

        val convertAmountToDouble = amount.toDoubleOrNull() ?: return ""

        return try {
            val result = exchangeRepository.convert(fromCurrency = fromCurrency, toCurrency = toCurrency, amount = convertAmountToDouble)
            result.toString()
        }catch (e:Exception){
            ""
        }
    }
}
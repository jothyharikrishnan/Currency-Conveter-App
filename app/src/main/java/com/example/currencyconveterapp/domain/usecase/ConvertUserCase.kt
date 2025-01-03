package com.example.currencyconveterapp.domain.usecase

import com.example.currencyconveterapp.domain.repository.ExchangeRepository

class ConvertUserCase(
    private val exchangeRepository: ExchangeRepository
) {

    suspend operator fun invoke( fromCurrency: String, toCurrency: String, amount: String ): String {

        if (fromCurrency.isBlank() || toCurrency.isBlank() || amount.isBlank()) return ""

        if (fromCurrency == toCurrency) return  amount

        val convertAmountToDouble = amount.toDoubleOrNull() ?: return ""

        println("Converting convertAmountToDouble:$convertAmountToDouble")

        return try {
            val result = exchangeRepository.convert(fromCurrency = fromCurrency, toCurrency = toCurrency, amount = convertAmountToDouble)
            println("Converting result: $result")
            result.toString()
        }catch (e:Exception){
            return if (e.message!=null)
                e.message.toString()
            else
                ""
        }
    }
}
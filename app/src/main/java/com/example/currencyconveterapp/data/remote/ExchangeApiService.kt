package com.example.currencyconveterapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeApiService {

    @GET("{apiKey}/pair/{from}/{to}/{amount}")
    suspend fun convert(
        @Path("apiKey") apiKey: String,
        @Path("from") fromCurrency: String,
        @Path("to") toCurrency: String,
        @Path("amount") amount: Double
    ): ExchangeDto

    // Define other endpoints if necessary
}

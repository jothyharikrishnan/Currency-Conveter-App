package com.example.currencyconveterapp.data.repository
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.currencyconveterapp.data.remote.ExchangeApiService
import com.example.currencyconveterapp.data.remote.ExchangeDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class ExchangeRepositoryImplTest {

    private lateinit var repository: ExchangeRepositoryImpl
    private lateinit var apiService: ExchangeApiService

    @BeforeEach
    fun setup() {
        apiService = mockk()
        repository = ExchangeRepositoryImpl(apiService)
    }

    @ParameterizedTest
    @CsvSource(
        "USD, EUR, 100.0, 96.49",
        "EUR, USD, 100.0, 103.64",
        "EUR, INR, 100.0, 8868.93",
        "EUR, EUR, 100.0, 100.0"
    )
     fun `testing currency converter success`(
        fromCurrency: String,
        toCurrency: String,
        amount: Double,
        expectedConversionResult: Double
    ) = runTest {
        // Define the mock behavior of ExchangeApiService
        coEvery { apiService.convert(any(), any(), any(), any()) } returns response(expectedConversionResult)

        val result = repository.convert(fromCurrency, toCurrency, amount)

        assertThat(expectedConversionResult).isEqualTo(result)
    }

    @ParameterizedTest
    @CsvSource(
        "USD, EUR, 47583323, 0.0",
        "EUR, USD, 32345434, 0.0",
        "EUR, INR, 47583323, 0.0",
        "EUR, EUR, 4758332, 4758332"
    )
    fun `testing currency converter error`(
        fromCurrency: String,
        toCurrency: String,
        amount: Double,
        expectedConversionResult: Double
    ) = runTest {
        // Define the mock behavior of ExchangeApiService
        coEvery { apiService.convert(any(), any(), any(), any()) } returns response(expectedConversionResult)

        val result = repository.convert(fromCurrency, toCurrency, amount)

        assertThat(expectedConversionResult).isEqualTo(result)
    }


    @Test
    fun `testing to getAllCurrencies`() = runBlocking {

        val result = repository.getAllCurrencies()

        assertNotNull(result)

        assertTrue(result.isNotEmpty())

        assertThat(result.size).isEqualTo(161)
    }

    private fun response(expectedConversionResult: Double) = ExchangeDto(
        base_code = "USD",
        conversion_rate = 1.0,
        conversion_result = expectedConversionResult,
        documentation = "https://www.exchangerate-api.com/docs/",
        result = expectedConversionResult.toString(),
        target_code = "EUR",
        terms_of_use = "https://www.exchangerate-api.com/terms/",
        time_last_update_unix = 1662019200,
        time_last_update_utc = "2022-09-01 00:00:00",
        time_next_update_unix = 1662105600,
        time_next_update_utc = "2022-09-01 08:00:00"
    )
}

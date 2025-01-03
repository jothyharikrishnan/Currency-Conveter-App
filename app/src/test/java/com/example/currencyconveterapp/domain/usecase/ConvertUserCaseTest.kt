package com.example.currencyconveterapp.domain.usecase

import com.example.currencyconveterapp.domain.repository.ExchangeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ConvertUserCaseTest {

    private lateinit var convertUserCase: ConvertUserCase
    private lateinit var exchangeRepository: ExchangeRepository

    @BeforeEach
    fun setup() {
        exchangeRepository = mockk()
        convertUserCase = ConvertUserCase(exchangeRepository)
    }

    @Test
    fun `invoke should return empty string when inputs are blank`() = runTest {
        // Act
        val result = convertUserCase("", "EUR", "100.0")
        val result2 = convertUserCase("USD", "", "100.0")
        val result3 = convertUserCase("USD", "EUR", "")

        // Assert
        assertEquals("", result)
        assertEquals("", result2)
        assertEquals("", result3)
    }

    @Test
    fun `invoke should return the same amount when fromCurrency and toCurrency are the same`() = runTest {
        // Act
        val result = convertUserCase("USD", "USD", "100.0")

        // Assert
        assertEquals("100.0", result)
    }

    @Test
    fun `invoke should return empty string when amount is invalid`() = runTest {
        // Act
        val result = convertUserCase("USD", "EUR", "invalid")

        // Assert
        assertEquals("", result)
    }

    @Test
    fun `invoke should return correct result on successful conversion`() = runTest {
        // Arrange
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val amount = "100.0"
        val expectedConversionResult = 96.49

        coEvery { exchangeRepository.convert(any(), any(), any()) } returns expectedConversionResult

        // Act
        val result = convertUserCase(fromCurrency, toCurrency, amount)

        // Assert
        assertEquals(expectedConversionResult.toString(), result)
    }

    @Test
    fun `invoke should return exception message on failure`() = runTest {
        // Arrange
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val amount = "100.0"
        val exceptionMessage = "Conversion error"

        coEvery { exchangeRepository.convert(any(), any(), any()) } throws Exception(exceptionMessage)

        // Act
        val result = convertUserCase(fromCurrency, toCurrency, amount)
        println("convertUserCase :$result")
        // Assert
        assertEquals(exceptionMessage, result)
    }
}

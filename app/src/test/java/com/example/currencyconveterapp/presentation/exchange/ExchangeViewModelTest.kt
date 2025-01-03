package com.example.currencyconveterapp.presentation.exchange

import com.example.currencyconveterapp.domain.model.Currency
import com.example.currencyconveterapp.domain.repository.ExchangeRepository
import com.example.currencyconveterapp.domain.usecase.ConvertUserCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue

@ExperimentalCoroutinesApi
class ExchangeViewModelTest {

    private lateinit var viewModel: ExchangeViewModel
    private lateinit var convertUserCase: ConvertUserCase
    private lateinit var exchangeRepository: ExchangeRepository

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        convertUserCase = mockk()
        exchangeRepository = mockk()
        coEvery { exchangeRepository.getAllCurrencies() } returns listOf(Currency("USD","USD"), Currency("EUR","EUR"))
        viewModel = ExchangeViewModel(convertUserCase, exchangeRepository)
    }

    @Test
    fun `initial state is set correctly and convert is called`() = runTest {
        val state = viewModel.state.first()
        assertTrue(state.allCurrencyList.isNotEmpty())
        coVerify { convertUserCase(any(), any(), any()) }
    }

    @Test
    fun `onAction Clear resets amount and result`() = runTest {
        viewModel.onAction(ExchangeAction.Input("123"))
        viewModel.onAction(ExchangeAction.Clear)

        val state = viewModel.state.first()
        assertEquals("", state.amount)
        assertEquals("", state.result)
    }

    @Test
    fun `onAction Delete removes last character from amount and calls convert`() = runTest {
        viewModel.onAction(ExchangeAction.Input("123"))
        viewModel.onAction(ExchangeAction.Delete)

        val state = viewModel.state.first()
        assertEquals("12", state.amount)
        coVerify { convertUserCase(any(), any(), any()) }
    }

    @Test
    fun `onAction Input appends value to amount and calls convert`() = runTest {
        viewModel.onAction(ExchangeAction.Input("1"))
        viewModel.onAction(ExchangeAction.Input("2"))
        val state = viewModel.state.first()
        assertEquals("12", state.amount)
        coVerify { convertUserCase(any(), any(), any()) }
    }


    @Test
    fun `onAction SelectedFrom updates from currency and calls convert`() = runTest {
        viewModel.onAction(ExchangeAction.SelectedFrom(1))

        val state = viewModel.state.first()
        assertEquals("EUR", state.from.code)
        coVerify { convertUserCase(any(), any(), any()) }
    }

    @Test
    fun `onAction SelectedTo updates to currency and calls convert`() = runTest {
        viewModel.onAction(ExchangeAction.SelectedTo(1))

        val state = viewModel.state.first()
        assertEquals("EUR", state.to.code)
        coVerify { convertUserCase(any(), any(), any()) }
    }

    @Test
    fun `convert updates state with result or error`() = runTest {
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val amount = "1.0"
        val expectedResult = "96.49"

        coEvery { convertUserCase(fromCurrency, toCurrency, amount) } returns expectedResult

        // Set initial state
        viewModel.onAction(ExchangeAction.SelectedFrom(0)) // Assuming "USD" is at index 0
        viewModel.onAction(ExchangeAction.SelectedTo(1)) // Assuming "EUR" is at index 1
        viewModel.onAction(ExchangeAction.Input(amount))

        viewModel.convert()

        val state = viewModel.state.first()
        assertEquals(expectedResult, state.result)
    }

    @Test
    fun `convert updates state with error message on exception`() = runTest {
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val amount = "1.0"
        val exceptionMessage = "Conversion error"

        coEvery { convertUserCase(fromCurrency, toCurrency, amount) } throws Exception(exceptionMessage)

        // Set initial state
        viewModel.onAction(ExchangeAction.SelectedFrom(0)) // Assuming "USD" is at index 0
        viewModel.onAction(ExchangeAction.SelectedTo(1)) // Assuming "EUR" is at index 1
        viewModel.onAction(ExchangeAction.Input(amount))

        viewModel.convert()

        val state = viewModel.state.first()
        println("errorMsg : ${state.result}")
        assertEquals("Error: $exceptionMessage", state.result)
    }


}

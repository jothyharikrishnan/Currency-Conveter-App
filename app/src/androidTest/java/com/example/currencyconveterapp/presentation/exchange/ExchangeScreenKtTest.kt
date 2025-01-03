package com.example.currencyconveterapp.presentation.exchange

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.currencyconveterapp.domain.model.Currency
import com.example.currencyconveterapp.domain.repository.ExchangeRepository
import com.example.currencyconveterapp.domain.usecase.ConvertUserCase
import com.example.currencyconveterapp.presentation.theme.CurrencyConveterAppTheme
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ExchangeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val convertUserCase: ConvertUserCase = mockk()
    private val exchangeRepository: ExchangeRepository = mockk()

    private lateinit var viewModel: ExchangeViewModel

    @Before
    fun setup() {
        coEvery { exchangeRepository.getAllCurrencies() } returns listOf(Currency("USD","USD"), Currency("EUR","EUR"))
        viewModel = ExchangeViewModel(convertUserCase, exchangeRepository)
        composeTestRule.setContent {
            CurrencyConveterAppTheme {
                ExchangeScreenCore(viewModel = viewModel)
            }
        }
    }

    @Test
    fun testConversionSuccess() = runTest {
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val amount = "100.0"
        val expectedResult = "96.49"

        coEvery { convertUserCase(fromCurrency, toCurrency, amount) } returns expectedResult

        // Set input values
        composeTestRule.onNodeWithTag("InputItem_1").performClick()
        composeTestRule.onNodeWithTag("InputItem_0").performClick()
        composeTestRule.onNodeWithTag("InputItem_0").performClick()
        composeTestRule.onNodeWithTag("InputItem_Dot").performClick()
        composeTestRule.onNodeWithTag("InputItem_0").performClick()

        composeTestRule.onNodeWithTag("FromCurrency").performClick()
        composeTestRule.onNodeWithTag("CurrencyItem_0").performClick()

        composeTestRule.onNodeWithTag("ToCurrency").performClick()
        composeTestRule.onNodeWithTag("CurrencyItem_1").performClick()

        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithTag("ResultText").assertTextEquals("96.49")
    }

    @Test
    fun testConversionError() = runTest {
        val fromCurrency = "USD"
        val toCurrency = "EUR"
        val amount = "123354322"
        val expectedResult = "0.0"

        coEvery { convertUserCase(fromCurrency, toCurrency, amount) } returns expectedResult

        // Set input values
        composeTestRule.onNodeWithTag("InputItem_1").performClick()
        composeTestRule.onNodeWithTag("InputItem_2").performClick()
        composeTestRule.onNodeWithTag("InputItem_3").performClick()
        composeTestRule.onNodeWithTag("InputItem_3").performClick()
        composeTestRule.onNodeWithTag("InputItem_5").performClick()
        composeTestRule.onNodeWithTag("InputItem_4").performClick()
        composeTestRule.onNodeWithTag("InputItem_3").performClick()
        composeTestRule.onNodeWithTag("InputItem_2").performClick()
        composeTestRule.onNodeWithTag("InputItem_2").performClick()

        composeTestRule.onNodeWithTag("FromCurrency").performClick()
        composeTestRule.onNodeWithTag("CurrencyItem_0").performClick()

        composeTestRule.onNodeWithTag("ToCurrency").performClick()
        composeTestRule.onNodeWithTag("CurrencyItem_1").performClick()

        composeTestRule.awaitIdle()

        composeTestRule.onNodeWithTag("ResultText").assertTextEquals("0.0")
    }

}

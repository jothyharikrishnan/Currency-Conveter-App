package com.example.currencyconveterapp.presentation.exchange

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconveterapp.domain.usecase.ConvertUserCase
import com.example.currencyconveterapp.domain.repository.ExchangeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val convertUserCase: ConvertUserCase,
    private val exchangeRepository: ExchangeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ExchangeState())
    val state: StateFlow<ExchangeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            convert()
            _state.value = _state.value.copy(
                allCurrencyList = exchangeRepository.getAllCurrencies()
            )
        }
    }

    fun onAction(action: ExchangeAction) {
        when (action) {
            ExchangeAction.Clear -> {
                _state.value = _state.value.copy(
                    amount = "",
                    result = "",
                )
            }

            ExchangeAction.Delete -> {
                if (_state.value.amount.isBlank()) return

                _state.value = _state.value.copy(
                    amount = _state.value.amount.dropLast(1)
                )

                convert()
            }

            is ExchangeAction.Input -> {
                _state.value = _state.value.copy(
                    amount = _state.value.amount + action.value
                )

                convert()
            }

            is ExchangeAction.SelectedFrom -> {
                _state.value = _state.value.copy(
                    from = _state.value.allCurrencyList[action.index]
                )

                convert()
            }

            is ExchangeAction.SelectedTo -> {
                _state.value = _state.value.copy(
                    to = _state.value.allCurrencyList[action.index]
                )

                convert()
            }
        }
    }

    private fun convert() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(
                    result = convertUserCase(
                        fromCurrency = _state.value.from.code,
                        toCurrency = _state.value.to.code,
                        amount = _state.value.amount
                    )
                )
            } catch (e: Exception) {
                // Handle error (e.g., log it, show a message to the user)
            }
        }
    }
}

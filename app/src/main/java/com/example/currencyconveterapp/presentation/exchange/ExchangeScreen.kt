package com.example.currencyconveterapp.presentation.exchange

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.automirrored.rounded.Backspace
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.currencyconveterapp.presentation.theme.CurrencyConveterAppTheme

@Composable
fun ExchangeScreenCore(
    viewModel: ExchangeViewModel = hiltViewModel()
){
    ExchangeScreen(
        state = viewModel.state.collectAsState().value,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ExchangeScreen(
    state: ExchangeState,
    onAction: (ExchangeAction) -> Unit
){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("ExchangeScreen"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Background circle shape at the right top
        Box(modifier = Modifier
            .weight(0.7f)
            .fillMaxSize()){

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(200.dp, (-40).dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiary.copy(0.1f))
            )

            // Background circle shape at the left bottom
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset((-280).dp, 150.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.inversePrimary.copy(0.3f)
                    )
            )

            var isDropDownOpen by rememberSaveable {
                mutableStateOf(false)
            }
            var isFromCurrencyPicker by rememberSaveable {
                mutableStateOf(false)
            }

            ExchangeSection(
                state = state,
                onOpenCurrencyPicker = { isFromCurrency ->
                    isFromCurrencyPicker = isFromCurrency
                    isDropDownOpen = true
                }
            )

            DropdownMenu(
                modifier = Modifier.heightIn(max = 600.dp),
                expanded = isDropDownOpen,
                onDismissRequest = {
                    isDropDownOpen = false
                }
            ) {

                state.allCurrencyList.forEachIndexed { index, currency ->
                    Column {
                        if (index == 0) {
                            HorizontalDivider()
                        }

                        Text(
                            text = currency.code + " - " + currency.name,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .clickable {
                                    isDropDownOpen = false
                                    if (isFromCurrencyPicker) {
                                        onAction(ExchangeAction.SelectedFrom(index))
                                    } else {
                                        onAction(ExchangeAction.SelectedTo(index))
                                    }
                                }
                                .padding(16.dp)
                                .testTag("CurrencyItem_$index")
                        )
                        HorizontalDivider()
                    }
                }

            }


        }

        InputSection(
            modifier = Modifier
                .weight(1.3f)
                .testTag("InputSection"),
            onAction = onAction
        )
    }
}

@Composable
fun ExchangeSection(
    modifier: Modifier = Modifier,
    state: ExchangeState,
    onOpenCurrencyPicker: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("ExchangeSection"),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .clickable { onOpenCurrencyPicker(true) }
                    .testTag("FromCurrency"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.from.code,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.width(3.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = "Selected from currency"
                )
            }

            Text(
                text = state.amount.ifBlank { "1" },
                fontFamily = FontFamily.Monospace,
                fontSize = 30.sp,
                textAlign = TextAlign.End,
                maxLines = 1,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("AmountText")
            )

        }

        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier
                    .clickable { onOpenCurrencyPicker(false) }
                    .testTag("ToCurrency"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = state.to.code,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 30.sp
                )

                Spacer(modifier = Modifier.width(3.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                    contentDescription = "Selected to currency"
                )
            }

            Text(
                text = state.result,
                fontFamily = FontFamily.Monospace,
                fontSize = 30.sp,
                textAlign = TextAlign.End,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.testTag("ResultText")
            )

        }
    }
}

@Composable
fun InputSection(
    modifier: Modifier = Modifier,
    onAction: (ExchangeAction) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(MaterialTheme.colorScheme.background)
            .testTag("InputSection")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Input(text = "7", modifier = Modifier.testTag("InputItem_7")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(text = "8", modifier = Modifier.testTag("InputItem_8")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(text = "9", modifier = Modifier.testTag("InputItem_9")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(
                text = "C",
                color = Color.Red,
                modifier = Modifier.testTag("InputItem_C")
            ) {
                onAction(ExchangeAction.Clear)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Input(text = "4", modifier = Modifier.testTag("InputItem_4")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(text = "5", modifier = Modifier.testTag("InputItem_5")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(text = "6", modifier = Modifier.testTag("InputItem_6")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(modifier = Modifier.testTag("InputItem_Empty_1"))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Input(text = "1", modifier = Modifier.testTag("InputItem_1")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(text = "2", modifier = Modifier.testTag("InputItem_2")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(text = "3", modifier = Modifier.testTag("InputItem_3")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(modifier = Modifier.testTag("InputItem_Empty_2"))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Input(text = "00", modifier = Modifier.testTag("InputItem_00")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(text = "0", modifier = Modifier.testTag("InputItem_0")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(text = ".", modifier = Modifier.testTag("InputItem_Dot")) {
                onAction(ExchangeAction.Input(it))
            }
            Input(
                icon = Icons.AutoMirrored.Rounded.Backspace,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.testTag("InputItem_Backspace")
            ) {
                onAction(ExchangeAction.Delete)
            }
        }
    }
}


@Composable
fun RowScope.Input(
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: ImageVector? = null,
    color: Color = MaterialTheme.colorScheme.onBackground,
    onClick: (String) -> Unit = {}
) {
    Box(
        modifier = modifier
            .weight(1f)
            .fillMaxHeight()
            .clickable { onClick(text ?: "") }
            .testTag(text ?: icon?.name ?: "InputItem"),
        contentAlignment = Alignment.Center
    ) {
        if (text != null) {
            Text(
                text = text,
                fontSize = 35.sp,
                fontFamily = FontFamily.Monospace,
                color = color
            )
        } else if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}


@Preview
@Composable
fun PreviewExchangeScreen(){
    CurrencyConveterAppTheme {
        ExchangeScreen(
            state = ExchangeState(),
            onAction ={} )
    }
}

@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.calculatorapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.calculatorapp.ui.theme.CalculatorAPpTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorAPpTheme {
                var text by remember {
                    mutableStateOf("")
                }
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background
                        )
                        .fillMaxSize()
                        .padding(32.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                    InputPhoneNumberView(
                        label = "Phone Number",
                        text = text
                    ) {
                        text = it
                        println(text)
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputPhoneNumberView(label: String, text: (String), modifier: Modifier = Modifier,
                         onValueChange: (String) -> Unit) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    var isBottomSheetVisible by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Surface() {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 12.dp,
                            topEnd = 12.dp,
                            bottomStart = 2.dp,
                            bottomEnd = 2.dp,
                        )
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surfaceContainer
                    )
            ) {
                CountryCodeView(
                    modifier = Modifier
                        .clickable {
                            scope.launch {
                                isBottomSheetVisible = true
                                sheetState.expand()
                            }
                        }
                        .padding(
                            top = 8.dp,
                            bottom = 8.dp,
                            start = 12.dp,
                        )
                    ,
                )
                VerticalDivider(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .height(40.dp)
                        .width(2.dp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                )

                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,
                    modifier = modifier
                        .padding(horizontal = 12.dp)
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            isFocused = focusState.hasFocus
                        },
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                        lineHeight = MaterialTheme.typography.titleMedium.lineHeight,
                        fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Phone
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                        }),
                    decorationBox = { innerTextField ->
                        SharedTransitionLayout(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Box {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    horizontalAlignment = Alignment.Start,
                                    verticalArrangement = Arrangement.spacedBy(
                                        0.dp
                                    ),

                                    ) {
                                    AnimatedVisibility(isFocused || text != "") {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(
                                                alpha = 0.8f
                                            ),
                                            modifier = Modifier
                                                .sharedBounds(
                                                    rememberSharedContentState(key = "labelAnimation"),
                                                    animatedVisibilityScope = this

                                                ),
                                            textAlign = TextAlign.Start
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            text = "+62",
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier
                                                .padding(end = 8.dp),
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                        if (isFocused || text != "") {
                                            innerTextField()
                                        }
                                        AnimatedVisibility(!isFocused && text == "") {
                                            Text(
                                                text = label,
                                                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                                color = MaterialTheme.colorScheme.onBackground.copy(
                                                    alpha = 0.5f
                                                ),
                                                modifier = Modifier
                                                    .sharedBounds(
                                                        rememberSharedContentState(key = "labelAnimation"),
                                                        animatedVisibilityScope = this

                                                    ),
                                                textAlign = TextAlign.Start
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    singleLine = true,
                )
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                thickness = 1.dp
            )
            AnimatedVisibility(isFocused) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp
                )
            }

        }

        BottomSheetView(
            isBottomSheetVisible = isBottomSheetVisible,
            sheetState = sheetState,
            onDismissRequest = {
                scope.launch { sheetState.hide() }
                    .invokeOnCompletion { isBottomSheetVisible = false }
            },
        ) {
            Box(
                contentAlignment = Alignment.Center
            ){
                CountryCodeSelectorView()
            }

        }
    }
}

@Composable
fun CountryCodeView(modifier: Modifier = Modifier){
    Row(
        modifier = modifier
            .padding(end = 8.dp)
            .height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Image(
            painter = painterResource(R.drawable.country_id),
            contentDescription = "country_id",
            modifier = Modifier
                .padding(end = 8.dp)
        )
        Icon(
            Icons.Rounded.KeyboardArrowDown,
            contentDescription = "chevron_down",
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        )
    }
}

@Composable
fun CountryCodeSelectorView(){
    val country = arrayOf(
        "id", "sg", "th", "in"
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        for (i in country) {
            CountryCodeListView(isSelected = false, countryId = i)

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun CountryCodeListView(countryId: String, isSelected:Boolean){
    val countryFlag = when (countryId) {
        "id" -> R.drawable.country_id
        "sg" -> R.drawable.country_sg
        "th" -> R.drawable.country_th
        "in" -> R.drawable.country_in
        else -> R.drawable.country_id
    }

    val countryName = when (countryId) {
        "id" -> "Indonesia(+62)"
        "sg" -> "Singapore(+65)"
        "th" -> "Thailand(+66)"
        "in" -> "India(+91)"
        else -> "Indonesia(+62)"
    }

    Row(
        modifier = Modifier
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(countryFlag),
            contentDescription = "country_id",
            modifier = Modifier
                .padding(end = 8.dp)
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(2.dp)
                )
                .height(16.dp)
                .width(20.dp),
            contentScale = ContentScale.FillWidth
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = countryName,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground)

        RadioButton(
            modifier = Modifier
                .size(24.dp)
                .offset(x = (-12).dp)
            ,
            selected = isSelected,
            onClick = null,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetView(
    isBottomSheetVisible: Boolean,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
){
    if (isBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = Modifier,
            sheetState = sheetState,
            shape = RectangleShape,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
            scrimColor = Color.Black.copy(alpha = 0.6f),
            dragHandle = null,
            windowInsets = WindowInsets(0, 0, 0, 0),
        ) {
            Box(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                FilledIconButton(
                    modifier =
                    Modifier.size(40.dp),
                    onClick = onDismissRequest,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {

                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Hide the dialog."
                    )

                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            topStart = 28.dp,
                            topEnd = 28.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp,
                        )
                    )
                    .background(
                        color = MaterialTheme.colorScheme.background
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                content()
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CalculatorAPpTheme {
        CountryCodeSelectorView()
    }
}
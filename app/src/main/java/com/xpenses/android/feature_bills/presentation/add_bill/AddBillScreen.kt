package com.xpenses.android.feature_bills.presentation.add_bill

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.xpenses.android.R
import com.xpenses.android.core.ui.components.*
import com.xpenses.android.core.ui.navigation.screen_specs.AddBillScreenSpec
import com.xpenses.android.core.ui.theme.*
import com.xpenses.android.core.util.DateUtil.dayOfMonth
import com.xpenses.android.core.util.DateUtil.month
import com.xpenses.android.core.util.DateUtil.year
import com.xpenses.android.core.util.exhaustive
import com.xpenses.android.feature_bills.domain.model.BillCategory
import com.xpenses.android.feature_bills.presentation.components.CategoryIcon
import java.util.*

@Composable
fun AddBillScreen(navController: NavController) {
    val viewModel: AddBillViewModel = hiltViewModel()
    val description by viewModel.name.observeAsState("")
    val amount by viewModel.amount.observeAsState("")
    val category by viewModel.category.observeAsState(BillCategory.OTHERS)
    val payByDate by viewModel.payByDate.observeAsState("")

    val context = LocalContext.current
    val snackbarController = rememberSnackbarController()

    LaunchedEffect(context) {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewModel.events.collect { event ->
            when (event) {
                AddBillViewModel.AddBillEvent.BillAdded -> {
                    navController.previousBackStackEntry?.savedStateHandle
                        ?.set(ADD_BILL_RESULT, RESULT_BILL_ADDED)
                    navController.popBackStack()
                }
                is AddBillViewModel.AddBillEvent.ShowSnackbar -> {
                    snackbarController.showSnackbar(event.message.asString(context))
                }
            }.exhaustive
        }
    }

    ScreenContent(
        name = description,
        amount = amount,
        category = category,
        payByDate = payByDate,
        context = context,
        snackbarController = rememberSnackbarController(),
        actions = viewModel,
        navigateUp = navController::popBackStack,
        recurring = true
    )
}

@Composable
private fun ScreenContent(
    name: String,
    amount: String,
    recurring: Boolean,
    category: BillCategory,
    payByDate: String,
    context: Context,
    snackbarController: SnackbarController,
    actions: AddBillActions,
    navigateUp: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val currentDateCalendar = remember {
        Calendar.getInstance()
    }
    val payByDatePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selectedDateCalendar = Calendar.getInstance().also {
                    it.set(Calendar.YEAR, year)
                    it.set(Calendar.MONTH, month)
                    it.set(Calendar.DAY_OF_MONTH, day)
                }
                actions.onPayByDateChange(selectedDateCalendar.timeInMillis)
            },
            currentDateCalendar.year, currentDateCalendar.month, currentDateCalendar.dayOfMonth
        ).also {
            it.datePicker.apply {
                minDate = currentDateCalendar.timeInMillis
            }
        }
    }

    Scaffold(
        topBar = {
            TransparentSmallTopAppBar(
                title = AddBillScreenSpec.label,
                navigationIcon = { BackArrowButton(onClick = navigateUp) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = actions::onSave) {
                Icon(
                    imageVector = Icons.Rounded.Save,
                    contentDescription = stringResource(R.string.content_description_save)
                )
            }
        },
        snackbarHost = { XTSnackbarHost(snackbarController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { payByDatePicker.show() }
                    .padding(vertical = PaddingSmall, horizontal = PaddingMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryIcon(category = category)
                Spacer(Modifier.width(SpacingMedium))
                Column {
                    Text(
                        text = stringResource(category.label),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.click_to_select_category),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingMedium)
                    .background(
                        color = MaterialTheme.colorScheme.surface
                            .copy(alpha = ContentAlpha.PERCENT_16),
                        shape = MaterialTheme.shapes.large
                    )
                    .padding(PaddingMedium)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        bottom = ListPaddingLarge
                    ),
                    verticalArrangement = Arrangement.spacedBy(SpacingMedium)
                ) {
                    item {
                        LabelAndInput(
                            label = R.string.name,
                            value = name,
                            onValueChange = actions::onNameChange,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Next
                            )
                        )
                    }
                    item {
                        LabelAndInput(
                            label = R.string.amount,
                            value = amount,
                            onValueChange = actions::onAmountChange,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                }
                            )
                        )
                    }
                    item {
                        LabelFirstCheckbox(
                            label = R.string.mark_bill_as_recurring,
                            isChecked = recurring,
                            onCheckedChange = actions::onMarkAsRecurringCheckChange
                        )
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = stringResource(R.string.label_pay_by_date),
                                style = MaterialTheme.typography.labelMedium
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = payByDate,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                IconButton(onClick = { payByDatePicker.show() }) {
                                    Icon(
                                        imageVector = Icons.Outlined.CalendarToday,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LabelAndInput(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(SpacingSmall))
        TextField(
            value = value,
            onValueChange = onValueChange,
            shape = MaterialTheme.shapes.small,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewScreenContent() {
    XpenseTrackerTheme {
        ScreenContent(
            name = "",
            amount = "",
            snackbarController = rememberSnackbarController(),
            actions = object : AddBillActions {
                override fun onNameChange(value: String) {}
                override fun onAmountChange(value: String) {}
                override fun onMarkAsRecurringCheckChange(isChecked: Boolean) {}
                override fun onCategoryClick() {}
                override fun onCategorySelect(category: BillCategory) {}
                override fun onPayByDateChange(dateMillis: Long) {}
                override fun onSave() {}
            },
            navigateUp = {},
            recurring = true,
            category = BillCategory.SUBSCRIPTION,
            context = LocalContext.current,
            payByDate = "10th Jan"
        )
    }
}
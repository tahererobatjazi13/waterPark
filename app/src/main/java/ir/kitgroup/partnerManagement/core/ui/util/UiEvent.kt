package ir.kitgroup.partnerManagement.core.ui.util

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.kitgroup.partnerManagement.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed interface UiEvent {
    data class ShowMessage(val message: UiText) : UiEvent
    data class ShowError(val error: UiText) : UiEvent
}
fun <T> ViewModel.execute(
    stateFlow: MutableStateFlow<DataState<T>>,
    eventFlow: MutableSharedFlow<UiEvent>,
    @StringRes successMessageRes: Int? = null,
    @StringRes defaultErrorMessageRes: Int = R.string.error_unknown_error,
    onSuccess: ((T) -> Unit)? = null,
    action: suspend () -> Result<T>
) {
    viewModelScope.launch {
        stateFlow.value = DataState.Loading
        val result = action()

        result.fold(
            onSuccess = { data ->
                stateFlow.value = DataState.Success(data)
                successMessageRes?.let {
                    eventFlow.emit(UiEvent.ShowMessage(UiText.StringResource(it)))
                }
                onSuccess?.invoke(data)
            },
            onFailure = { throwable ->
                val errorUiText = throwable.message?.let {
                    UiText.DynamicString(it)
                } ?: UiText.StringResource(defaultErrorMessageRes)

                stateFlow.value = DataState.Error(errorUiText)
                eventFlow.emit(UiEvent.ShowError(errorUiText))
            }
        )
    }
}
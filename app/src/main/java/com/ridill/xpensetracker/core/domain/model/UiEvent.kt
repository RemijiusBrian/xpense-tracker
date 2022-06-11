package com.ridill.xpensetracker.core.domain.model

import com.ridill.xpensetracker.core.domain.util.BaseUiEvent

sealed class UiEvent : BaseUiEvent() {
    data class ShowMessage(val uiText: UiText) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class NavigateBackWithResult(val result: String) : UiEvent()
}
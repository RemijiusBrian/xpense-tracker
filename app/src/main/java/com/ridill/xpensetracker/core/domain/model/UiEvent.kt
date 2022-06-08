package com.ridill.xpensetracker.core.domain.model

import com.ridill.xpensetracker.core.domain.util.BaseUiEvent

sealed class UiEvent : BaseUiEvent() {
    data class ShowMessage(val uiText: UiText) : UiEvent()
}
package com.ridill.xpensetracker.feature_expenses.presentation.expense_tags

import androidx.lifecycle.*
import com.ridill.xpensetracker.core.util.exhaustive
import com.ridill.xpensetracker.feature_expenses.domain.model.ExpenseTag
import com.ridill.xpensetracker.feature_expenses.domain.repository.TagsRepository
import com.zhuinden.flowcombinetuplekt.combineTuple
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagsViewModel @Inject constructor(
    private val repo: TagsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel(), TagsActions {

    // Tags With Expenses
    private val tagsWithExpenses = repo.getTagsWithExpenses()

    // Show Delete Tag Dialog
    private val showDeleteTagDialog = savedStateHandle.getLiveData("showDeleteTagDialog", false)

    // Active Tag
    private val activeTag = savedStateHandle.getLiveData<ExpenseTag>("activeTag")

    // UI State
    val tagsListState = combineTuple(
        tagsWithExpenses,
        showDeleteTagDialog.asFlow(),
        activeTag.asFlow()
    ).map { (
                tagsWithExpenses,
                showDeleteTagDialog,
                activeTag
            ) ->

        TagsListState(
            tagsWithExpense = tagsWithExpenses,
            showDeleteTagDialog = showDeleteTagDialog,
            activeTag = activeTag
        )
    }.asLiveData()

    override fun onTagMenuOptionSelect(tagOptions: TagOptions) {
        when (tagOptions) {
            is TagOptions.DeleteTag -> {
                onDeleteTagOptionClick(tagOptions.tag)
            }
            is TagOptions.EditTag -> {
                // TODO: Edit Option Click
            }
        }.exhaustive
    }

    override fun onAddTagClick() {
        activeTag.value = ExpenseTag.DEFAULT
    }

    private fun onDeleteTagOptionClick(tag: ExpenseTag) {
        activeTag.value = tag
        showDeleteTagDialog.value = true
    }

    override fun onDeleteTagDismiss() {
        activeTag.value = null
        showDeleteTagDialog.value = false
    }

    override fun onDeleteTagConfirm() {
        viewModelScope.launch {
            activeTag.value?.let {
                repo.deleteTag(it)
                showDeleteTagDialog.value = false
            }
        }
    }
}
package alex.com.taskrsschool.ui.human

import alex.com.taskrsschool.R
import alex.com.taskrsschool.common.ADD_HUMAN_RESULT_OK
import alex.com.taskrsschool.common.EDIT_HUMAN_RESULT_OK
import alex.com.taskrsschool.data.DatabaseRepository
import alex.com.taskrsschool.data.room.Human
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HumansViewModel @Inject constructor(
    private val repository: DatabaseRepository
) :
    ViewModel() {

    val humans = repository.humansSortedFlow.asLiveData()

    fun onHumanSelected(human: Human) = viewModelScope.launch {
        humansEventChannel.send(HumansEvent.NavigateToEditHumanScreen(human))
    }

    fun onAddNewHumanClick() = viewModelScope.launch {
        humansEventChannel.send(HumansEvent.NavigateToAddHumanScreen)
    }

    private fun showHumanSavedConfirmationMessage(message: Int) = viewModelScope.launch {
        humansEventChannel.send(HumansEvent.ShowHumanSavedConfirmationMessage(message))
    }

    fun onHumanSwiped(human: Human) = viewModelScope.launch {
        repository.delete(human)
    }

    fun onFilterClicked() = viewModelScope.launch {
        humansEventChannel.send(HumansEvent.NavigateToSortSettingsFragment)
    }

    private val humansEventChannel = Channel<HumansEvent>()
    val humansEvent = humansEventChannel.receiveAsFlow()

    sealed class HumansEvent {
        object NavigateToAddHumanScreen : HumansEvent()
        object NavigateToSortSettingsFragment : HumansEvent()
        data class NavigateToEditHumanScreen(val human: Human) : HumansEvent()
        data class ShowHumanSavedConfirmationMessage(val message: Int) : HumansEvent()
    }

    fun onEditorResult(result: Int) {
        when (result) {
            ADD_HUMAN_RESULT_OK -> showHumanSavedConfirmationMessage(R.string.humanAdded)
            EDIT_HUMAN_RESULT_OK -> showHumanSavedConfirmationMessage(R.string.humanUpdated)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}



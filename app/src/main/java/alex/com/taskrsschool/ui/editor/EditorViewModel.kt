package alex.com.taskrsschool.ui.editor

import alex.com.taskrsschool.R
import alex.com.taskrsschool.common.*
import alex.com.taskrsschool.data.room.Human
import alex.com.taskrsschool.data.DatabaseRepository
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val repository: DatabaseRepository,
    private val state: SavedStateHandle
) :
    ViewModel() {

    fun onSavedClick() {
        if (humanName.isBlank()) {
            showInvalidInputMessage(R.string.nameValidation)
            return
        }
        if (humanAge.isBlank()) {
            showInvalidInputMessage(R.string.ageValidation)
            return
        }

        if (humanProfession.isBlank()) {
            showInvalidInputMessage(R.string.professionValidation)
            return
        }

        if (human != null) {
            val updatedHuman =
                human.copy(
                    name = humanName,
                    age = humanAge.toInt(),
                    profession = humanProfession,
                    color = humanCardColor
                )
            updateHuman(updatedHuman)
        } else {
            val newHuman = Human(
                name = humanName,
                age = humanAge.toInt(),
                profession = humanProfession,
                color = humanCardColor
            )
            createHuman(newHuman)
        }
    }

    private fun showInvalidInputMessage(errorMessage: Int) = viewModelScope.launch {
        editorEventChannel.send(EditorEvent.ShowInvalidInputMessage(errorMessage))
    }

    private fun createHuman(newHuman: Human) = viewModelScope.launch {
        repository.insert(newHuman)
        editorEventChannel.send(EditorEvent.NavigateBackWithResult(ADD_HUMAN_RESULT_OK))
    }

    private fun updateHuman(updatedHuman: Human) = viewModelScope.launch {
        repository.update(updatedHuman)
        editorEventChannel.send(EditorEvent.NavigateBackWithResult(EDIT_HUMAN_RESULT_OK))
    }

    private val editorEventChannel = Channel<EditorEvent>()
    val editorEvent = editorEventChannel.receiveAsFlow()

    sealed class EditorEvent {
        data class ShowInvalidInputMessage(val message: Int) : EditorEvent()
        data class NavigateBackWithResult(val result: Int) : EditorEvent()
    }

    val human = state.get<Human>(KEY_HUMAN)

    var humanName = state.get<String>(KEY_HUMAN_NAME) ?: human?.name ?: EMPTY_STRING
        set(value) {
            field = value
            state.set(KEY_HUMAN_NAME, value)
        }

    var humanAge = state.get<String>(KEY_HUMAN_AGE) ?: human?.age?.toString() ?: EMPTY_STRING
        set(value) {
            field = value
            state.set(KEY_HUMAN_AGE, value)
        }

    var humanProfession =
        state.get<String>(KEY_HUMAN_PROFESSION) ?: human?.profession ?: EMPTY_STRING
        set(value) {
            field = value
            state.set(KEY_HUMAN_PROFESSION, value)
        }

    var humanCardColor =
        state.get<Int>(KEY_HUMAN_CARD_COLOR) ?: human?.color ?: DEFAULT_HUMAN_CARD_COLOR
        set(value) {
            field = value
            state.set(KEY_HUMAN_CARD_COLOR, value)
        }
}


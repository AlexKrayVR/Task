package alex.com.taskrsschool.ui.settings

import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.common.TypeDB
import alex.com.taskrsschool.common.logDebug
import alex.com.taskrsschool.data.preferences.PreferencesManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val preferencesManager: PreferencesManager) :
    ViewModel() {

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onTypeDBSelected(typeDB: TypeDB) = viewModelScope.launch {
        preferencesManager.updateTypeDB(typeDB)
    }
}
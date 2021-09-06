package alex.com.taskrsschool.ui.settings

import alex.com.taskrsschool.R
import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.common.TypeDB
import alex.com.taskrsschool.common.logDebug
import alex.com.taskrsschool.data.preferences.TYPE_DB_KEY
import android.R.attr.key
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }

    //was first decision
    override fun onStop() {
        val pref: Preference? = findPreference(TYPE_DB_KEY)
        if (pref is ListPreference) {
            when (pref.entry) {
                resources.getStringArray(R.array.db_values)[0] -> viewModel.onTypeDBSelected(TypeDB.ROOM)
                resources.getStringArray(R.array.db_values)[1] -> viewModel.onTypeDBSelected(TypeDB.SQL_LITE)
            }
        }
        super.onStop()
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "sortByName" -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                return true
            }
            "sortByAge" -> {
                viewModel.onSortOrderSelected(SortOrder.BY_AGE)
                return true
            }
            "sortByProfession" -> {
                viewModel.onSortOrderSelected(SortOrder.BY_PROFESSION)
                return true
            }
        }
        return false
    }
}
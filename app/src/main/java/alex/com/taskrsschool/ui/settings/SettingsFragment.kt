package alex.com.taskrsschool.ui.settings

import alex.com.taskrsschool.R
import alex.com.taskrsschool.common.EMPTY_STRING
import alex.com.taskrsschool.common.SortOrder
import alex.com.taskrsschool.common.TypeDB
import alex.com.taskrsschool.common.logDebug
import alex.com.taskrsschool.data.preferences.TYPE_DB_KEY
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val viewModel: SettingsViewModel by viewModels()
    private var toast: Toast? = null
    private lateinit var preferences: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    //change type of DB: second decision
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key.equals(TYPE_DB_KEY)) {
            val typeDB = sharedPreferences?.getString(key, EMPTY_STRING)
            logDebug("onSharedPreferenceChanged - typeDB: $typeDB")
            when (typeDB) {
                resources.getStringArray(R.array.db_values)[0] -> viewModel.onTypeDBSelected(TypeDB.ROOM)
                resources.getStringArray(R.array.db_values)[1] -> viewModel.onTypeDBSelected(TypeDB.SQL_LITE)
            }
        }
    }

    //change type of DB: first decision
//    override fun onStop() {
//        val pref: Preference? = findPreference(TYPE_DB_KEY)
//        if (pref is ListPreference) {
//            when (pref.entry) {
//                resources.getStringArray(R.array.db_values)[0] -> viewModel.onTypeDBSelected(TypeDB.ROOM)
//                resources.getStringArray(R.array.db_values)[1] -> viewModel.onTypeDBSelected(TypeDB.SQL_LITE)
//            }
//        }
//        super.onStop()
//    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        when (preference.key) {
            "sortByName" -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                showSortStatus(getString(R.string.settingsSortByName))
                return true
            }
            "sortByAge" -> {
                viewModel.onSortOrderSelected(SortOrder.BY_AGE)
                showSortStatus(getString(R.string.settingsSortByAge))
                return true
            }
            "sortByProfession" -> {
                viewModel.onSortOrderSelected(SortOrder.BY_PROFESSION)
                showSortStatus(getString(R.string.settingsSortByProfession))
                return true
            }
        }
        return false
    }

    private fun showSortStatus(message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast?.show()
    }

    override fun onDestroy() {
        toast?.cancel()
        preferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}
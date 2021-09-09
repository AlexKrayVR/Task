package alex.com.taskrsschool.ui.host

import alex.com.taskrsschool.R
import alex.com.taskrsschool.data.preferences.TYPE_DB_KEY
import alex.com.taskrsschool.databinding.ActivityMainBinding
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        pref = PreferenceManager.getDefaultSharedPreferences(this)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.humansFragment) {
                val title =
                    pref.getString(TYPE_DB_KEY, resources.getStringArray(R.array.db_values)[0])
                supportActionBar?.title = getString(R.string.titleHumanFragment, title)
            }
        }

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()
}
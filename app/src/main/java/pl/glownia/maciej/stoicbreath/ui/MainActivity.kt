package pl.glownia.maciej.stoicbreath.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import pl.glownia.maciej.stoicbreath.R
import pl.glownia.maciej.stoicbreath.databinding.ActivityMainBinding
import pl.glownia.maciej.stoicbreath.ui.fragments.QuotesListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.findNavController()

        // Handle all fragments from bottom navigation menu as equal to each other
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.quoteListFragment, R.id.favoritesQuoteFragment, R.id.randomQuoteFragment)
        )

        setSupportActionBar(binding.toolbar)
        // Make sure actions in the ActionBar get propagated to the NavController
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    /**
     * Creates an option menu
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    /**
     * Enables back button support. Simply navigates one element up on the stack.
     */
    override fun onSupportNavigateUp(): Boolean {
        QuotesListFragment()
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
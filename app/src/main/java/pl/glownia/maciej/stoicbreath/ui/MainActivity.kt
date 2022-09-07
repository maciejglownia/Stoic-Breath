package pl.glownia.maciej.stoicbreath.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import pl.glownia.maciej.stoicbreath.R
import pl.glownia.maciej.stoicbreath.databinding.ActivityMainBinding
import pl.glownia.maciej.stoicbreath.ui.fragments.QuotesListFragment
import pl.glownia.maciej.stoicbreath.ui.fragments.FavoriteQuotesFragment
import pl.glownia.maciej.stoicbreath.ui.fragments.RandomQuoteFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.quoteListFragment -> replaceFragment(QuotesListFragment())
                R.id.savedQuoteFragment -> replaceFragment(FavoriteQuotesFragment())
                R.id.randomQuoteFragment -> replaceFragment(RandomQuoteFragment())
                else -> {}
            }
            true
        }
    }

    /**
     * This is how we replace frame layout with the fragment
     */
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragment, fragment)
        fragmentTransaction.commit()
    }
}
package pl.glownia.maciej.stoicbreath.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import pl.glownia.maciej.stoicbreath.NavGraphDirections
import pl.glownia.maciej.stoicbreath.QuoteApplication
import pl.glownia.maciej.stoicbreath.adapters.QuoteListAdapter
import pl.glownia.maciej.stoicbreath.data.QuoteDatabase
import pl.glownia.maciej.stoicbreath.databinding.FragmentFavoriteQuotesBinding
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository
import pl.glownia.maciej.stoicbreath.ui.QuoteListViewModel
import pl.glownia.maciej.stoicbreath.ui.QuoteListViewModelProviderFactory

class FavoriteQuotesFragment : Fragment() {

    private val viewModel: QuoteListViewModel by activityViewModels() {
        QuoteListViewModelProviderFactory(
            (activity?.application as QuoteApplication).database.quoteDao(),
            QuoteRepository(QuoteDatabase.getDatabase(requireContext().applicationContext))
        )
    }

    private var _binding: FragmentFavoriteQuotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteQuotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            val adapter = QuoteListAdapter() {
                this.findNavController().navigate(NavGraphDirections.globalActionToSingleQuote(it))
        }

        /**
         * Handle deleting quote from favorites
         */
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val quote = adapter.currentList[position]
                viewModel.deleteQuoteFromFavorites(quote)
                Snackbar.make(view, "Deleted quote from favorites", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.addQuoteToFavorites(quote)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedQuote)
        }

        /**
         * Display favorites quotes from database
         */
        viewModel.favoritesQuotes.observe(viewLifecycleOwner) { quotes ->
            if (quotes.isEmpty()) {
                binding.tvNoQuotesInFavoritesYet.visibility = View.VISIBLE
                binding.rvSavedQuote.visibility = View.GONE
            } else {
                adapter.submitList(quotes)
                binding.rvSavedQuote.adapter = adapter
                binding.rvSavedQuote.layoutManager = LinearLayoutManager(this.context)
                binding.tvNoQuotesInFavoritesYet.visibility = View.GONE
                binding.rvSavedQuote.visibility = View.VISIBLE
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
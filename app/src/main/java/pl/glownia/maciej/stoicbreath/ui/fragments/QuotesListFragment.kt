package pl.glownia.maciej.stoicbreath.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.glownia.maciej.stoicbreath.QuoteApplication
import pl.glownia.maciej.stoicbreath.adapters.QuoteListAdapter
import pl.glownia.maciej.stoicbreath.data.QuoteDatabase
import pl.glownia.maciej.stoicbreath.databinding.FragmentQuotesListBinding
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository
import pl.glownia.maciej.stoicbreath.ui.QuoteListViewModel
import pl.glownia.maciej.stoicbreath.ui.QuoteListViewModelProviderFactory

class QuotesListFragment : Fragment() {

    private val viewModel: QuoteListViewModel by activityViewModels() {
        QuoteListViewModelProviderFactory(
            (activity?.application as QuoteApplication).database.quoteDao(),
            QuoteRepository(QuoteDatabase.getDatabase(requireContext().applicationContext))
        )
    }

    private var _binding: FragmentQuotesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuotesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = QuoteListAdapter() { quote ->
            val action =
                QuotesListFragmentDirections.actionQuoteListFragmentToSingleQuoteFragment(quote)
            this.findNavController().navigate(action)
        }
        viewModel.quotes.observe(this.viewLifecycleOwner) { quotes ->
            quotes.let {
                // Needs to be shuffled to display different quotes every time user want to see them
                adapter.submitList(it.shuffled())
            }
        }
        binding.rvQuoteList.adapter = adapter
        binding.rvQuoteList.layoutManager = LinearLayoutManager(this.context)
    }
}
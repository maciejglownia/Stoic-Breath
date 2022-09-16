package pl.glownia.maciej.stoicbreath.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.glownia.maciej.stoicbreath.NavGraphDirections
import pl.glownia.maciej.stoicbreath.QuoteApplication
import pl.glownia.maciej.stoicbreath.R
import pl.glownia.maciej.stoicbreath.adapters.QuoteListAdapter
import pl.glownia.maciej.stoicbreath.data.QuoteDatabase
import pl.glownia.maciej.stoicbreath.databinding.FragmentQuotesListBinding
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository
import pl.glownia.maciej.stoicbreath.ui.QuoteApiStatus
import pl.glownia.maciej.stoicbreath.ui.QuoteListViewModel
import pl.glownia.maciej.stoicbreath.ui.QuoteListViewModelProviderFactory
import pl.glownia.maciej.stoicbreath.utils.Constants.Companion.GETTING_DATA_FROM_API
import pl.glownia.maciej.stoicbreath.utils.Constants.Companion.NO_INTERNET_CONNECTION

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

        val adapter = QuoteListAdapter() {
            this.findNavController().navigate(NavGraphDirections.globalActionToSingleQuote(it))
        }
        /**
         * Happens once at the beginning after installation the app
         * Shows user that data is loading and saving into database
         */
        if (viewModel.getSizeOfTable() == 0) {
            viewModel.status.observe(this.viewLifecycleOwner) { status ->
                when (status) {
                    QuoteApiStatus.LOADING -> {
                        binding.ivStatusImage.visibility = View.VISIBLE
                        binding.tvStatusText.text = GETTING_DATA_FROM_API
                        binding.tvStatusText.visibility = View.VISIBLE
                        binding.ivStatusImage.setImageResource(R.drawable.ic_load_data_from_api)
                    }
                    QuoteApiStatus.SUCCESS -> {
                        binding.ivStatusImage.visibility = View.GONE
                        binding.tvStatusText.visibility = View.GONE
                    }
                    QuoteApiStatus.ERROR -> {
                        binding.ivStatusImage.visibility = View.VISIBLE
                        binding.tvStatusText.text = NO_INTERNET_CONNECTION
                        binding.tvStatusText.visibility = View.VISIBLE
                        binding.ivStatusImage.setImageResource(R.drawable.ic_no_internet_connection)
                    }
                    else -> {}
                }
            }
        }
        /**
         * Displays shuffled list of quotes
         */
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
package pl.glownia.maciej.stoicbreath.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import pl.glownia.maciej.stoicbreath.QuoteApplication
import pl.glownia.maciej.stoicbreath.R
import pl.glownia.maciej.stoicbreath.data.QuoteDatabase
import pl.glownia.maciej.stoicbreath.databinding.FragmentSingleQuoteBinding
import pl.glownia.maciej.stoicbreath.models.Quote
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository
import pl.glownia.maciej.stoicbreath.ui.QuoteListViewModel
import pl.glownia.maciej.stoicbreath.ui.QuoteListViewModelProviderFactory

class SingleQuoteFragment : Fragment() {
    private val viewModel: QuoteListViewModel by activityViewModels() {
        QuoteListViewModelProviderFactory(
            (activity?.application as QuoteApplication).database.quoteDao(),
            QuoteRepository(QuoteDatabase.getDatabase(requireContext().applicationContext))
        )
    }
    private val args: SingleQuoteFragmentArgs by navArgs()

    private var _binding: FragmentSingleQuoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSingleQuoteBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quote = args.quote
        binding.cvSingleQuote.apply {
            bind(quote)
        }
        binding.fabAddToFavorite.setOnClickListener {
            viewModel.addQuoteToFavorites(quote)
            Snackbar.make(view, "Quote added to favorites", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun bind(quote: Quote) {
        binding.apply {
            when (quote.author) {
                "Marcus Aurelius" -> ivAuthorImage.setImageResource(R.drawable.stoic_marcus_aurelius_image)
                "Epictetus" -> ivAuthorImage.setImageResource(R.drawable.stoic_epictetus_image)
                "Seneca" -> ivAuthorImage.setImageResource(R.drawable.stoic_seneca_image)
            }
            val sentenceToDisplay = "''${quote.body}''"
            tvSentence.text = sentenceToDisplay
            val authorNameToDisplay = "~ ${quote.author} ~"
            tvAuthor.text = authorNameToDisplay
            tvSource.text = quote.quotesource
        }
    }
}
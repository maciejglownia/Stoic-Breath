package pl.glownia.maciej.stoicbreath.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository

class QuoteListViewModelProviderFactory(
    val quoteRepository: QuoteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return QuoteListViewModel(quoteRepository) as T
    }
}
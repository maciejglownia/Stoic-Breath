package pl.glownia.maciej.stoicbreath.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.glownia.maciej.stoicbreath.data.QuoteDao
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository

class QuoteListViewModelProviderFactory(
    val quoteDao: QuoteDao,
    val repository: QuoteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuoteListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
        return QuoteListViewModel(quoteDao, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
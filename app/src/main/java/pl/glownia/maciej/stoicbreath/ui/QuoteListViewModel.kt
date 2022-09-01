package pl.glownia.maciej.stoicbreath.ui

import androidx.lifecycle.ViewModel
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository

class QuoteListViewModel(
    val quoteRepository: QuoteRepository
): ViewModel() {
}
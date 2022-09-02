package pl.glownia.maciej.stoicbreath.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.glownia.maciej.stoicbreath.data.QuoteDao
import pl.glownia.maciej.stoicbreath.models.Quote
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository

class QuoteListViewModel(
    private val quoteDao: QuoteDao,
    repository: QuoteRepository,
) : ViewModel() {

    private val quoteRepository: QuoteRepository = repository
    val quotes: LiveData<List<Quote>> = quoteDao.getAllQuotes().asLiveData()

    init {
        getQuotes()
    }

    private fun getQuotes() {
        viewModelScope.launch {
            val response = quoteRepository.getQuotes()
            quoteDao.clearQuotes()
            quoteDao.insertAll(response)
        }
    }
}
package pl.glownia.maciej.stoicbreath.ui

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import pl.glownia.maciej.stoicbreath.data.QuoteDao
import pl.glownia.maciej.stoicbreath.models.Quote
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository
import pl.glownia.maciej.stoicbreath.utils.Constants

class QuoteListViewModel(
    private val quoteDao: QuoteDao,
    repository: QuoteRepository,
) : ViewModel() {

    private val quoteRepository: QuoteRepository = repository

    /**
     * Gets list of all quotes
     */
    private val _quotes: LiveData<List<Quote>> = quoteRepository.getQuotes().asLiveData()
    val quotes: LiveData<List<Quote>> = _quotes

    /**
     * Gets list of favorites quotes
     */
    private val _favoritesQuotes: LiveData<List<Quote>> =
        quoteRepository.getFavoriteQuotes().asLiveData()
    val favoritesQuotes: LiveData<List<Quote>> = _favoritesQuotes

    /**
     * Gets status: LOADING, SUCCESS or ERROR when getting data from API
     */
    private val _status = MutableLiveData<QuoteApiStatus>()
    val status: LiveData<QuoteApiStatus> = _status

    init {
        getQuotesFromApi()
    }

    /**
     * Gets random quote from database
     */
    fun getQuoteById(): LiveData<Quote> {
        val randomNumber = getRandomNumberInRangeOfTableSize()
        return quoteRepository.getQuoteById(randomNumber).asLiveData()
    }

    private fun getRandomNumberInRangeOfTableSize() = (0..getSizeOfTable()).random()

    fun getSizeOfTable(): Int = quoteRepository.getSizeOfTable()

    /**
     * Adds quote to favorites
     */
    fun addQuoteToFavorites(quote: Quote) = viewModelScope.launch {
        quote.isFavorite = true
        quoteRepository.update(quote)
    }

    /**
     * Removes quote from favorites
     */
    fun deleteQuoteFromFavorites(quote: Quote) = viewModelScope.launch {
        quote.isFavorite = false
        quoteRepository.update(quote)
    }

    /**
     * Gets quotes from API if there are not in database
     */
    private fun getQuotesFromApi() {
        if (getSizeOfTable() == 0) {
            viewModelScope.launch {
                _status.value = QuoteApiStatus.LOADING
                delay(Constants.GETTING_QUOTES_FROM_API_TIME_DELAY)
                try {
                    val response = quoteRepository.getQuotesFromApi()
                    _status.value = QuoteApiStatus.SUCCESS
                    insertFetchedQuotesIntoDatabase(response)
                } catch (e: Exception) {
                    _status.value = QuoteApiStatus.ERROR
                }
            }
        }
    }

    /**
     * Inserts fetched quotes into database
     */
    private suspend fun insertFetchedQuotesIntoDatabase(response: List<Quote>) {
        quoteDao.clearAll()
        quoteDao.insertAll(response)
    }
}
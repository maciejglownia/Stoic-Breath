package pl.glownia.maciej.stoicbreath.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.glownia.maciej.stoicbreath.data.QuoteDao
import pl.glownia.maciej.stoicbreath.models.Quote
import pl.glownia.maciej.stoicbreath.repository.QuoteRepository
import retrofit2.HttpException
import java.io.IOException

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

    private fun getSizeOfTable(): Int = quoteRepository.getSizeOfTable()

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
     * Gets quotes from Api if there are not in database
     */
    private fun getQuotesFromApi() {
        Log.e(
            "QuoteListViewModel",
            "getQuotesFromApi: Is checking if database contains any data..."
        )
        val sizeOfTable = quoteRepository.getSizeOfTable()
        if (sizeOfTable == 0) {
            viewModelScope.launch {

                Log.e(
                    "QuoteListViewModel",
                    "getQuotesFromApi: Database is empty so it is going to fetch data from Api"
                )
                try {
                    Log.e("QuoteListViewModel", "getQuotesFromApi: Getting quotes from Api...")
                    val response = quoteRepository.getQuotesFromApi()
                    Log.e("QuoteListViewModel", "getQuotesFromApi: Clearing database...")
                    insertFetchedQuotesIntoDatabase(response)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Log.e("QuoteListViewModel", "getQuotesFromApi: IOException")
                } catch (e: HttpException) {
                    e.printStackTrace()
                    Log.e("QuoteListViewModel", "getQuotesFromApi: HttpException")
                }
            }
        }
        Log.e("QuoteListViewModel", "getQuotesFromApi: Database already has data")
    }

    /**
     * Inserts fetched quotes into database
     */
    private suspend fun insertFetchedQuotesIntoDatabase(response: List<Quote>) {
        quoteDao.clearAll()
        Log.e("QuoteListViewModel", "getQuotesFromApi: Inserting data...")
        quoteDao.insertAll(response)
    }
}
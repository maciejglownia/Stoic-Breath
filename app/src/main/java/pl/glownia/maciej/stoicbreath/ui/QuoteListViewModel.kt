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

    init {
        getQuotesFromApi()
    }

    private fun getQuotesFromApi() {
        Log.e("QuoteListViewModel","getQuotesFromApi: Is checking if database contains any data...")
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
                    quoteDao.clearQuotes()
                    Log.e("QuoteListViewModel", "getQuotesFromApi: Inserting data...")
                    quoteDao.insertAll(response)
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
}
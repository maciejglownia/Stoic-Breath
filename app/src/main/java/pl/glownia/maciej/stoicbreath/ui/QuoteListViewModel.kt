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
    val quotes: LiveData<List<Quote>> = quoteDao.getAllQuotes().asLiveData()

    init {
        getQuotes()
    }

    private fun getQuotes() {
        viewModelScope.launch {
            try {
                Log.e("QuoteListViewModel", "getQuotes: Getting quotes from Api...")
                val response = quoteRepository.getQuotes()
                quoteDao.clearQuotes()
                quoteDao.insertAll(response)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("QuoteListViewModel", "getQuotes: IOException")
            } catch (e: HttpException) {
                e.printStackTrace()
                Log.e("QuoteListViewModel", "getQuotes: HttpException")
            }
        }
    }
}
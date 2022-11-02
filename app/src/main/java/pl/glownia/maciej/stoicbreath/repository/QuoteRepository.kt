package pl.glownia.maciej.stoicbreath.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import pl.glownia.maciej.stoicbreath.api.RetrofitInstance
import pl.glownia.maciej.stoicbreath.data.QuoteDatabase
import pl.glownia.maciej.stoicbreath.models.Quote

class QuoteRepository(
    val database: QuoteDatabase
) {
    suspend fun getQuotesFromApi() = RetrofitInstance.api.getQuotes()

    fun getQuotes() = database.quoteDao().loadQuotes()

    fun getQuoteById(id: Int) = database.quoteDao().loadQuoteById(id)

    fun getSizeOfTable(): Int = runBlocking {
        val count = async {
            database.quoteDao().loadSizeOfTable()
        }
        count.start()
        count.await()
    }

    suspend fun update(quote: Quote) = database.quoteDao().update(quote)

    fun getFavoriteQuotes(): Flow<List<Quote>> = database.quoteDao().loadFavoriteQuotes()
}
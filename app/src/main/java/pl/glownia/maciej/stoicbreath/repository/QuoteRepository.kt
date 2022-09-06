package pl.glownia.maciej.stoicbreath.repository

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import pl.glownia.maciej.stoicbreath.api.RetrofitInstance
import pl.glownia.maciej.stoicbreath.data.QuoteDatabase

class QuoteRepository(
    val database: QuoteDatabase
) {
    suspend fun getQuotesFromApi() = RetrofitInstance.api.getQuotes()

    suspend fun getQuoteById(id: Int) = database.quoteDao().getQuoteById(id)

    fun getSizeOfTable(): Int = runBlocking {
        val count = async {
            database.quoteDao().getSizeOfTable()
        }
        count.start()
        count.await()
    }
}
package pl.glownia.maciej.stoicbreath.repository

import pl.glownia.maciej.stoicbreath.api.RetrofitInstance
import pl.glownia.maciej.stoicbreath.data.QuoteDatabase

class QuoteRepository(
    val database: QuoteDatabase
) {
    suspend fun getQuotes() = RetrofitInstance.api.getQuotes()
}
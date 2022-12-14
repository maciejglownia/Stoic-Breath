package pl.glownia.maciej.stoicbreath.api

import pl.glownia.maciej.stoicbreath.models.Quote
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {

    @GET("quotes")
    suspend fun getQuotes(): List<Quote>
}
package pl.glownia.maciej.stoicbreath.api

import pl.glownia.maciej.stoicbreath.QuotesResponse
import retrofit2.Response
import retrofit2.http.GET

interface QuotesApi {

    @GET("quotes")
    suspend fun getQuotes() : Response<QuotesResponse>
}
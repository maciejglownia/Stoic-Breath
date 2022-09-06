package pl.glownia.maciej.stoicbreath.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.glownia.maciej.stoicbreath.models.Quote

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes")
    fun getQuotesFromDatabase(): Flow<List<Quote>>

    @Query("SELECT * FROM quotes WHERE id = :id")
    fun getQuoteById(id: Int): Flow<Quote>

    // Needed to check if database contains any data
    @Query("SELECT COUNT(id) FROM quotes")
    suspend fun getSizeOfTable(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quoteList: List<Quote>)

    @Update
    suspend fun update(quote: Quote)

    @Query("DELETE FROM quotes")
    suspend fun clearQuotes()
}
package pl.glownia.maciej.stoicbreath.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.glownia.maciej.stoicbreath.models.Quote

@Dao
interface QuoteDao {

    @Query("SELECT * FROM quotes")
    fun loadQuotes(): Flow<List<Quote>>

    @Query("SELECT * FROM quotes WHERE id = :id")
    fun loadQuoteById(id: Int): Flow<Quote>

    @Query("SELECT COUNT(id) FROM quotes")
    suspend fun loadSizeOfTable(): Int

    @Query("SELECT * FROM quotes WHERE isFavorite = 1")
    fun loadFavoriteQuotes(): Flow<List<Quote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(quoteList: List<Quote>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(quote: Quote)

    @Query("DELETE FROM quotes")
    suspend fun clearAll()
}
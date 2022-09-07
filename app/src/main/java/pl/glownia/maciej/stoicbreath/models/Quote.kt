package pl.glownia.maciej.stoicbreath.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "quotes"
)
data class Quote(
    val author: String,
    val body: String,
    @PrimaryKey
    val id: Int,
    val quotesource: String,
    var isFavorite: Boolean = false,
) : Serializable
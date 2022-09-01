package pl.glownia.maciej.stoicbreath

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "quotes"
)
data class Quote(
    val author: String,
    val body: String,
    val document_with_weights: String,
    @PrimaryKey
    val id: Int,
    val keywords: List<String>,
    val quotesource: String
)
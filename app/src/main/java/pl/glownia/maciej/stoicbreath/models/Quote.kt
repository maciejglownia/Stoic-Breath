package pl.glownia.maciej.stoicbreath.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "quotes"
)
data class Quote(
    val author: String,
    val body: String,
    @PrimaryKey
    val id: Int,
    val quotesource: String
)
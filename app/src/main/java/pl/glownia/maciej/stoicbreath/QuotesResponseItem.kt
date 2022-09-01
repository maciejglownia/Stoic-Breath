package pl.glownia.maciej.stoicbreath

data class QuotesResponseItem(
    val author: String,
    val body: String,
    val document_with_weights: String,
    val id: Int,
    val keywords: List<String>,
    val quotesource: String
)
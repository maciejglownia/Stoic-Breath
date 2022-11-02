package pl.glownia.maciej.stoicbreath

import android.app.Application
import pl.glownia.maciej.stoicbreath.data.QuoteDatabase

class QuoteApplication : Application() {
    val database: QuoteDatabase by lazy { QuoteDatabase.getDatabase(this) }
}
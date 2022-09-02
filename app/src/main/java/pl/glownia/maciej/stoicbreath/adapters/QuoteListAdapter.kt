package pl.glownia.maciej.stoicbreath.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.glownia.maciej.stoicbreath.databinding.ItemQuoteViewBinding
import pl.glownia.maciej.stoicbreath.models.Quote

class QuoteListAdapter(
    private val onItemClicked: (Quote) -> Unit
) : ListAdapter<Quote, QuoteListAdapter.QuoteViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Quote>() {
            override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
                return oldItem == newItem
            }
        }
    }

    class QuoteViewHolder(
        private var binding: ItemQuoteViewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(quote: Quote) {
            binding.apply {
                tvSentence.text = quote.body
                tvAuthor.text = quote.author
                tvSource.text = quote.quotesource
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return QuoteViewHolder(
            ItemQuoteViewBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: QuoteListAdapter.QuoteViewHolder, position: Int) {
        val quote = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(quote)
        }
        holder.bind(quote)
    }
}
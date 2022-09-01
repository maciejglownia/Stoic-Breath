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
            binding.tvSentence.text = quote.body
            binding.tvAuthor.text = quote.author
            binding.tvSource.text = quote.quotesource
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        val viewHolder = QuoteViewHolder(
            ItemQuoteViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: QuoteListAdapter.QuoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
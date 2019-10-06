package c.m.jeparalanguage.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import c.m.jeparalanguage.R
import c.m.jeparalanguage.data.source.local.entity.ContentEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_word.*

class MainAdapter(private val onClickListener: (ContentEntity) -> Unit) :
    PagedListAdapter<ContentEntity, MainAdapter.MainAdapterViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapterViewHolder =
        MainAdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_word, parent, false)
        )

    override fun onBindViewHolder(holder: MainAdapterViewHolder, position: Int) {
        val contents = getItem(position)
        if (contents != null) holder.bind(contents, onClickListener)
    }

    class MainAdapterViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(contentEntity: ContentEntity, onClickListener: (ContentEntity) -> Unit) {
            item_word.setOnClickListener { onClickListener(contentEntity) }

            tv_word.text = contentEntity.word
            tv_definition.text = contentEntity.definition
        }
    }

    companion object DiffCallBack : DiffUtil.ItemCallback<ContentEntity>() {
        override fun areItemsTheSame(oldItem: ContentEntity, newItem: ContentEntity): Boolean =
            oldItem.word == newItem.word

        override fun areContentsTheSame(oldItem: ContentEntity, newItem: ContentEntity): Boolean =
            oldItem == newItem
    }
}
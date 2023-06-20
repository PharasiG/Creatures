package com.raywenderlich.android.creatures.app

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.CompositeItem
import com.raywenderlich.android.creatures.model.Favorites
import com.raywenderlich.android.creatures.model.Favorites.saveFavorites
import com.raywenderlich.android.creatures.ui.CreatureActivity
import com.raywenderlich.android.creatures.ui.ItemTouchHelperListener
import java.util.Collections

class CreaturesAdapter(
    private val compositeItems: MutableList<CompositeItem>,
    private val context: Context
) :
    RecyclerView.Adapter<CreaturesAdapter.ViewHolder>(), ItemTouchHelperListener {

    class ViewHolder(itemView: View) : View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var composite: CompositeItem
        private val creatureImage: ImageView? = itemView.findViewById(R.id.creature_image)
        private val fullName: TextView? = itemView.findViewById(R.id.fullName)
        private val nickName: TextView? = itemView.findViewById(R.id.nickName)
        private val headerName: TextView? = itemView.findViewById(R.id.headerName)
        private val context = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(composite: CompositeItem) {
            this.composite = composite
            if (composite.isHeader) {
                headerName?.text = composite.toString()
            } else {
                creatureImage?.setImageResource(
                    context.resources.getIdentifier(
                        composite.creature?.uri, null, context.packageName
                    )
                )
                fullName?.text = composite.creature?.fullName
                nickName?.text = composite.creature?.nickname
            }

        }

        override fun onClick(v: View?) {
            composite.creature?.let {
                val intent = CreatureActivity.newIntent(context, it.id)
                context.startActivity(intent)
            }
        }
    }

    enum class ViewType {
        HEADER, CREATURE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.HEADER.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_planet_header, parent, false)
                ViewHolder(view)
            }

            ViewType.CREATURE.ordinal -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.creature_view_holder, parent, false)
                ViewHolder(view)
            }

            else -> throw IllegalArgumentException(
                "The composite item to be shown in the favorites recycler is neither a creature nor a header"
            )
        }

    }

    override fun getItemCount() = compositeItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(compositeItems[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (compositeItems[position].isHeader) ViewType.HEADER.ordinal
        else ViewType.CREATURE.ordinal
    }

    fun updateFavorites(favoriteCreatures: List<CompositeItem>) {
        compositeItems.clear()
        compositeItems.addAll(favoriteCreatures)
        notifyDataSetChanged()
    }

    override fun onItemMove(
        recyclerView: RecyclerView,
        fromPosition: Int,
        toPosition: Int
    ): Boolean {
        if (headerMoved(compositeItems, fromPosition, toPosition))
            return false
        //updating compositeItems data list
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(compositeItems, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(compositeItems, i, i - 1)
            }
        }
        saveFavorites(compositeItems.mapNotNull { it.creature?.id }, context)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    private fun headerMoved(
        compositeItems: MutableList<CompositeItem>, fromPosition: Int, toPosition: Int
    ): Boolean {
        for (i in fromPosition..toPosition) {
            if (compositeItems[i].isHeader) return true
        }
        for (i in fromPosition downTo toPosition) {
            if (compositeItems[i].isHeader) return true
        }
        return false
    }
}


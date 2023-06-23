package com.raywenderlich.android.creatures.app

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.CompositeItem
import com.raywenderlich.android.creatures.model.Favorites.removeFavorite
import com.raywenderlich.android.creatures.model.Favorites.saveFavoritesCustom
import com.raywenderlich.android.creatures.ui.CreatureActivity
import com.raywenderlich.android.creatures.ui.DragListener
import com.raywenderlich.android.creatures.ui.ItemTouchHelperListener
import java.util.Collections
import kotlin.text.Typography.less

class FavoriteCreaturesAdapter(
    private val compositeItems: MutableList<CompositeItem>,
    private val dragListener: DragListener,
    private val context: Context,
) : RecyclerView.Adapter<FavoriteCreaturesAdapter.ViewHolder>(), ItemTouchHelperListener {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private lateinit var composite: CompositeItem
        private val creatureImage: ImageView? = itemView.findViewById(R.id.creature_image)
        private val fullName: TextView? = itemView.findViewById(R.id.fullName)
        private val nickName: TextView? = itemView.findViewById(R.id.nickName)
        private val headerName: TextView? = itemView.findViewById(R.id.headerName)
        private val context = itemView.context
        private val dragHolder: ImageView? = itemView.findViewById(R.id.dragHolder)

        init {
            itemView.setOnClickListener(this)
        }

        @SuppressLint("ClickableViewAccessibility")
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

                dragHolder?.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            itemView.setBackgroundColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.selectedItem
                                )
                            )
                            dragListener.onDragged(this)
                        }
                    }
                    false
                }
            }

        }

        override fun onClick(v: View?) {
            composite.creature?.let {
                val intent = CreatureActivity.newIntent(context, it.id)
                context.startActivity(intent)
            }
        }
    }
    //ViewHolder class END

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

    override fun onItemMove(from: Int, to: Int): Boolean {
        if (headerMoved(compositeItems, from, to))
            return false

        //updating compositeItems data list
        if (from < to) {
            for (i in from until to) {
                Collections.swap(compositeItems, i, i + 1)
            }
        } else {
            for (i in from downTo to + 1) {
                Collections.swap(compositeItems, i, i - 1)
            }
        }
        saveFavoritesCustom(compositeItems.mapNotNull { it.creature?.id }, context)
        notifyItemMoved(from, to)
        return true
    }

    override fun onItemSwipe(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (headerSwiped(viewHolder)) return

        removeFavorite(compositeItems[position].creature!!, context)
        compositeItems.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
        for (i in 0 until compositeItems.size - 2) {
            if (compositeItems[i].isHeader and compositeItems[i + 1].isHeader) {
                compositeItems.removeAt(i)
                notifyItemRemoved(i)
            }
        }
        //this is done because last index was not handled in for loop
        if (compositeItems.last().isHeader) {
            compositeItems.removeAt(compositeItems.lastIndex)
            //.lastIndex will not give one less value as 1 item removed in previous line
            notifyItemRemoved(compositeItems.lastIndex + 1)
        }
    }

    private fun headerMoved(
        compositeItems: MutableList<CompositeItem>, fromPosition: Int, toPosition: Int,
    ): Boolean {
        for (i in fromPosition..toPosition) {
            if (compositeItems[i].isHeader) return true
        }
        for (i in fromPosition downTo toPosition) {
            if (compositeItems[i].isHeader) return true
        }
        return false
    }

    private fun headerSwiped(view: RecyclerView.ViewHolder): Boolean {
        return when (view.itemViewType) {
            ViewType.HEADER.ordinal -> true
            else -> false
        }
    }
}


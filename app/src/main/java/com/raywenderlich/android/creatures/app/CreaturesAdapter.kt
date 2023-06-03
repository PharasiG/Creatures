package com.raywenderlich.android.creatures.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.CompositeItem
import com.raywenderlich.android.creatures.ui.CreatureActivity

class CreaturesAdapter(private val compositeItems: MutableList<CompositeItem>) :
    RecyclerView.Adapter<CreaturesAdapter.ViewHolder>() {

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
                        composite.creature.uri, null, context.packageName
                    )
                )
                fullName?.text = composite.creature.fullName
                nickName?.text = composite.creature.nickname
            }

        }

        override fun onClick(v: View?) {
            val intent = CreatureActivity.newIntent(context, composite.creature.id)
            context.startActivity(intent)
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

}


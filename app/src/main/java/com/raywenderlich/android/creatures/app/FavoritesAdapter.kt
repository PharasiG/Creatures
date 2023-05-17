package com.raywenderlich.android.creatures.app

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.databinding.CreaturesViewHolderBinding
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.ui.CreatureActivity
import com.raywenderlich.android.creatures.ui.FavoritesFragment

class FavoritesAdapter(private val favorites: List<Creature>, private val creatureClickListener: CreatureClicked) :
    RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    interface CreatureClicked {
        fun onCreatureClick(id: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.creatures_view_holder, parent, false)
        return FavoritesViewHolder(view, creatureClickListener)
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount(): Int = favorites.size

    class FavoritesViewHolder(itemView: View, creatureClickListener: CreatureClicked) : RecyclerView.ViewHolder(itemView) {
        private lateinit var favorite: Creature
        private val creatureImage: ImageView = itemView.findViewById(R.id.creature_image)
        private val creatureName: TextView = itemView.findViewById(R.id.creature_name)
        private val creatureNickName: TextView = itemView.findViewById(R.id.creature_nick_name)
        private val context = itemView.context

        init {
            itemView.setOnClickListener {
                creatureClickListener.onCreatureClick(favorite.id)
            }
        }

        fun bind(favorite: Creature) {
            this.favorite = favorite
            creatureImage.setImageResource(
                context.resources.getIdentifier(favorite.uri, null, context.packageName)
            )
            creatureName.text = favorite.fullName
            creatureNickName.text = favorite.nickname
        }
    }
}
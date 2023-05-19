package com.raywenderlich.android.creatures.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import androidx.recyclerview.widget.SnapHelper
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.model.CreatureStore.getFood
import com.raywenderlich.android.creatures.ui.CreatureActivity

class CreaturesCardAdapter(private val creatures: MutableList<Creature>) :
    RecyclerView.Adapter<CreaturesCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.creature_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

//    fun updateFavorites(favoriteCreatures: List<Creature>) {
//        creatures.clear()
//        creatures.addAll(favoriteCreatures)
//        notifyDataSetChanged()
//    }

    class ViewHolder(itemView: View) : View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var creature: Creature
        private val creatureImage: ImageView = itemView.findViewById(R.id.creature_image)
        private val nickName: TextView = itemView.findViewById(R.id.nickname)
        private val context = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            creatureImage.setImageResource(
                context.resources.getIdentifier(creature.uri, null, context.packageName)
            )
            nickName.text = creature.nickname
        }

        override fun onClick(v: View?) {
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }


    }

}


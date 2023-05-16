package com.raywenderlich.android.creatures.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.Creature

class CreaturesAdapter(private val creatures: List<Creature>) :
    RecyclerView.Adapter<CreaturesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.creatures_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val creature_image = itemView.findViewById<ImageView>(R.id.creature_image)
        val creature_name = itemView.findViewById<TextView>(R.id.creature_name)

        fun bind(creature: Creature) {
            val context = itemView.context
            creature_image.setImageResource(
                context.resources.getIdentifier(creature.uri, null, context.packageName))
            creature_name.text = creature.fullName
        }
    }p
}


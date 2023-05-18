package com.raywenderlich.android.creatures.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.model.CreatureStore.getFood
import com.raywenderlich.android.creatures.ui.CreatureActivity

class CreaturesAdapter(private val creatures: MutableList<Creature>) :
    RecyclerView.Adapter<CreaturesAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.creatures_view_holder, parent, false)
        val holder = ViewHolder(view)
        holder.foodRecyclerView.setRecycledViewPool(viewPool)
        LinearSnapHelper().attachToRecyclerView(holder.foodRecyclerView)
        return holder
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    fun updateFavorites(favoriteCreatures: List<Creature>) {
        creatures.clear()
        creatures.addAll(favoriteCreatures)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var creature: Creature
        private val creatureImage: ImageView = itemView.findViewById(R.id.creature_image)
        val foodRecyclerView: RecyclerView = itemView.findViewById(R.id.foods_nested_recycler)
        private val context = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            creatureImage.setImageResource(
                context.resources.getIdentifier(creature.uri, null, context.packageName)
            )
            setUpFoodRecycler(creature)
        }

        private fun setUpFoodRecycler(creature: Creature) {
            foodRecyclerView.adapter = FoodAdapter(getFood(creature).toMutableList())
            foodRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        override fun onClick(v: View?) {
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }
    }

}


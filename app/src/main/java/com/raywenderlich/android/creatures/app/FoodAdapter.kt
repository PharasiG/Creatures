package com.raywenderlich.android.creatures.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.Food

class FoodAdapter(private val foods: MutableList<Food>) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.food_view_holder, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = foods.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(foods[position])
    }

    fun updateFoods(food: List<Food>) {
        foods.clear()
        foods.addAll(food)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage = itemView.findViewById<ImageView>(R.id.food_image)
        val context = itemView.context

        fun bind(food: Food) {
            foodImage.setImageResource(
                context.resources.getIdentifier(
                    food.thumbnail, null, context.packageName
                )
            )
        }
    }
}
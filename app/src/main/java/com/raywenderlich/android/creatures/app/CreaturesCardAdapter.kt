package com.raywenderlich.android.creatures.app

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.ui.CreatureActivity

class CreaturesCardAdapter(private val creatures: MutableList<Creature>) :
    RecyclerView.Adapter<CreaturesCardAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.creature_card_view, parent, false)
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
        private val creatureCard: CardView = itemView.findViewById(R.id.creatureCard)
        private val nicknameHolder: LinearLayout = itemView.findViewById(R.id.nicknameHolder)
        private val creatureImage: ImageView = itemView.findViewById(R.id.creature_image)
        private val nickName: TextView = itemView.findViewById(R.id.nickname)
        private val context = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val imageResource =
                context.resources.getIdentifier(creature.uri, null, context.packageName)
            creatureImage.setImageResource(imageResource)
            nickName.text = creature.nickname
            setBackgroundColors(imageResource)
        }

        override fun onClick(v: View?) {
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }

//        private fun setBackgroundColors(imageResource: Int) {
//            val image = BitmapFactory.decodeResource(context.resources, imageResource)
//            val palette = Palette.from(image).generate { palette ->
//                val dominantSwatch = palette?.dominantSwatch
//                val defColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
//                creatureCard.setBackgroundColor(dominantSwatch?.rgb ?: defColor)
//                nicknameHolder.setBackgroundColor(dominantSwatch?.rgb ?: defColor)
//                nickName.setTextColor(dominantSwatch?.titleTextColor ?: Color.WHITE)
//            }
//        }

        private fun setBackgroundColors(imageresource: Int) {
            val image = BitmapFactory.decodeResource(context.resources, imageresource)
            Palette.from(image).generate { palette ->
                palette?.let {
                    val backgroundColor = it.getDominantColor(
                        ContextCompat.getColor(
                            context, R.color.colorPrimaryDark
                        )
                    )
                    creatureCard.setBackgroundColor(backgroundColor)
                    nicknameHolder.setBackgroundColor(backgroundColor)
                    val textColor = if (isColorDark(backgroundColor)) Color.WHITE else Color.BLACK
                    nickName.setTextColor(textColor)
                }
            }
        }

        private fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color) +
                    0.587 * Color.green(color) +
                    0.114 * Color.blue(color)
                    ) / 255
            return darkness >= 0.5
        }
    }

}


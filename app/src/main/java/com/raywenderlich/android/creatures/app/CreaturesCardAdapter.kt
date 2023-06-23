package com.raywenderlich.android.creatures.app

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Handler
import android.os.Looper
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
import com.raywenderlich.android.creatures.ui.ItemTouchHelperListener
import java.util.Collections

class CreaturesCardAdapter(private val creatures: MutableList<Creature>) :
    RecyclerView.Adapter<CreaturesCardAdapter.ViewHolder>(), ItemTouchHelperListener {

    class ViewHolder(itemView: View) : View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var creature: Creature
        private val creatureCard: CardView = itemView.findViewById(R.id.creatureCard)
        private val nameHolder: LinearLayout = itemView.findViewById(R.id.nameHolder)
        private val creatureImage: ImageView = itemView.findViewById(R.id.creature_image)
        private val fullName: TextView = itemView.findViewById(R.id.fullName)
        private val slogan: TextView? = itemView.findViewById(R.id.slogan)
        private val sloganMars: TextView? = itemView.findViewById(R.id.slogan_mars)
        private val context = itemView.context

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val imageResource =
                context.resources.getIdentifier(creature.uri, null, context.packageName)
            creatureImage.setImageResource(imageResource)
            fullName.text = creature.fullName
            setBackgroundColors(imageResource)
        }

        override fun onClick(v: View?) {
            val intent = CreatureActivity.newIntent(context, creature.id)
            context.startActivity(intent)
        }

        private fun setBackgroundColors(imageresource: Int) {
            //being performed in the background thread, so as to not cause lag, extracting palette from so many images
            //will be a heavy task on the main thread
            Thread {
                val bitmap = BitmapFactory.decodeResource(context.resources, imageresource)
                val palette = Palette.from(bitmap).generate()

                val backgroundColor = palette.getDominantColor(
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
                )

                val handler = Handler(Looper.getMainLooper()) {
                    creatureCard.setBackgroundColor(backgroundColor)
                    nameHolder.setBackgroundColor(backgroundColor)
                    val textColor = if (isColorDark(backgroundColor)) Color.WHITE else Color.BLACK
                    fullName.setTextColor(textColor)
                    slogan?.setTextColor(textColor)
                    sloganMars?.setTextColor(textColor)
                    true
                }
                handler.sendEmptyMessage(0)
            }.start()
        }

        private fun isColorDark(color: Int): Boolean {
            val darkness =
                1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(
                    color
                )) / 255
            return darkness >= 0.5
        }

        /*private fun setBackgroundColors(imageResource: Int) {
    val image = BitmapFactory.decodeResource(context.resources, imageResource)
    val palette = Palette.from(image).generate { palette ->
        val dominantSwatch = palette?.dominantSwatch
        val defColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        creatureCard.setBackgroundColor(dominantSwatch?.rgb ?: defColor)
        nicknameHolder.setBackgroundColor(dominantSwatch?.rgb ?: defColor)
        nickName.setTextColor(dominantSwatch?.titleTextColor ?: Color.WHITE)
    }
}*/

        /* private fun setBackgroundColors(imageresource: Int) {
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
                } */

    }

    enum class ViewType {
        JUPITER, MARS, OTHER
    }

    var jupiterSpanSize = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.JUPITER.ordinal -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.creature_card_view_jupiter, parent, false)
                ViewHolder(view)
            }

            ViewType.MARS.ordinal -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.creature_card_view_mars, parent, false)
                ViewHolder(view)
            }

            ViewType.OTHER.ordinal -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.creature_card_view, parent, false)
                ViewHolder(view)
            }

            else -> throw IllegalArgumentException("View type of item in creature card adapter is invalid")
        }

    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (creatures[position].planet) {
            Constants.JUPITER -> ViewType.JUPITER.ordinal
            Constants.MARS -> ViewType.MARS.ordinal
            else -> ViewType.OTHER.ordinal
        }
    }

    fun spanSizeAtPosition(position: Int): Int {
        return if (creatures[position].planet == Constants.JUPITER) jupiterSpanSize
        else 1
    }
//    in-case this adapter is to be used for favorites activity too
//    fun updateFavorites(favoriteCreatures: List<Creature>) {
//        creatures.clear()
//        creatures.addAll(favoriteCreatures)
//        notifyDataSetChanged()
//    }

    override fun onItemMove(from: Int, to: Int): Boolean {
        if (from < to) {
            for (i in from until to) {
                Collections.swap(creatures, i, i + 1)
            }
        } else {
            for (i in from downTo to + 1) {
                Collections.swap(creatures, i, i - 1)
            }
        }
        notifyItemMoved(from, to)
        return true
    }

    override fun onItemSwipe(viewHolder: RecyclerView.ViewHolder, position: Int) {
        /*meh, no use here, swipe is disabled and this will never be called, only here because it is sharing
        touchHelperListener with favorite adapter one*/
    }
}


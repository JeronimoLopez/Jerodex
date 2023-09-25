package com.android.jerodex

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.jerodex.database.PokemonInformation
import com.android.jerodex.databinding.ListItemPokedexBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

private const val TAG = "PokedexListAdapter"

class PokemonViewHolder(
    private val binding: ListItemPokedexBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(pokemonInformation: PokemonInformation) {
        binding.pokedexImageView.load(pokemonInformation.sprites)
    }
}

class PokedexListAdapter(
    private var pokemonInformation: List<PokemonInformation>
) : RecyclerView.Adapter<PokemonViewHolder>() {

    private val displayedItems: MutableList<PokemonInformation> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PokemonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPokedexBinding.inflate(inflater, parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val item = pokemonInformation[position]
        if (!displayedItems.contains(item)) {
            Log.d(TAG, "${item.name} is being updated")
            holder.bind(item)
            val imageView = holder.itemView.findViewById<ImageView>(R.id.pokedex_image_view)

            Glide.with(holder.itemView.context)
                .asBitmap()
                .load(item.sprites)
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Palette.from(resource).generate { palette ->
                            val dominantColor = palette?.dominantSwatch?.rgb ?: Color.WHITE
                            val gradientDrawable = GradientDrawable()
                            gradientDrawable.orientation = GradientDrawable.Orientation.TOP_BOTTOM
                            gradientDrawable.colors = intArrayOf(
                                dominantColor,
                                Color.WHITE
                            )
                            gradientDrawable.cornerRadius =
                                holder.itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corner_radius)
                                    .toFloat()
                            imageView.background = gradientDrawable
                        }
                        Palette.from(resource).generate { palette ->
                            val dominantColor = palette?.dominantSwatch?.rgb ?: Color.WHITE}
                    }
                })
            displayedItems.add(item)
        }
    }

    override fun getItemCount(): Int = pokemonInformation.size

    fun updateData(newData: List<PokemonInformation>) {
        displayedItems.clear()
        pokemonInformation = newData
        notifyDataSetChanged()
    }
}

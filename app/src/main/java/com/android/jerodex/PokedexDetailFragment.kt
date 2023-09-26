package com.android.jerodex

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.android.jerodex.database.PokemonInformation
import com.android.jerodex.databinding.PokedexDetailFragmentBinding
import kotlinx.coroutines.launch

class PokedexDetailFragment : Fragment() {
    private var _binding: PokedexDetailFragmentBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: PokedexDetailFragmentArgs by navArgs()
    val pokedexDetailViewModel: PokedexDetailViewModel by viewModels {
        PokedexDetailViewModelFactory(args.name)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PokedexDetailFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                pokedexDetailViewModel.pokemon.collect() { pokemon ->
                    pokemon?.let { updateUi(it) }

                }

            }
        }
    }

    private fun updateUi(pokemonInformation: PokemonInformation) {
        binding.apply {
            pokemonName.text = pokemonInformation.name.uppercase()
            pokemonSprite.load(pokemonInformation.sprites)
            pokemonType1.load(getDrawable(pokemonInformation.types.first()))
            if(pokemonInformation.types.size > 1) {
                pokemonType2.load(getDrawable(pokemonInformation.types.last()))
                pokemonType2.visibility = View.VISIBLE
            }
            else{
                pokemonType2.visibility = View.GONE
            }
            nextButton.setOnClickListener{
                val (flag, text) = pokedexDetailViewModel.changePokemon((pokemonInformation.id)+1)
                if (flag) Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            }
            previousButton.setOnClickListener{
                val (flag, text) = pokedexDetailViewModel.changePokemon((pokemonInformation.id)-1)
                if (flag) Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDrawable(type:String):Int?{
        val typeToDrawableMap = mapOf(
            "fire" to R.drawable.firetype,
            "water" to R.drawable.watertype,
            "grass" to R.drawable.grasstype,
            "electric" to R.drawable.electrictype,
            "ice" to R.drawable.icetype,
            "psychic" to R.drawable.psychictype,
            "fighting" to R.drawable.fightingtype,
            "ground" to R.drawable.groundtype,
            "rock" to R.drawable.rocktype,
            "flying" to R.drawable.flyingtype,
            "bug" to R.drawable.bugtype,
            "poison" to R.drawable.poisontype,
            "normal" to R.drawable.normaltype,
            "ghost" to R.drawable.ghosttype,
            "dragon" to R.drawable.dragontype,
            "dark" to R.drawable.darktype,
            "steel" to R.drawable.steeltype,
            "fairy" to R.drawable.fairytype
        )
        val drawableResourceId = typeToDrawableMap[type]

        return typeToDrawableMap[type]
    }

    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}

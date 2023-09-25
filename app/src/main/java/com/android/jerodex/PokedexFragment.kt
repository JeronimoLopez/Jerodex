package com.android.jerodex

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.jerodex.database.PokemonInformation
import com.android.jerodex.databinding.FragmentPokedexBinding
import kotlinx.coroutines.launch

private const val TAG = "PokedexFragment"

class PokedexFragment : Fragment() {

    private var _binding: FragmentPokedexBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }
    private val pokedexViewModel: PokedexViewModel by viewModels()


    //SIRVE?
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pokedex, container, false)

        // Set the background color programmatically
        view.setBackgroundColor(resources.getColor(R.color.purple))

        _binding =
            FragmentPokedexBinding.inflate(inflater, container, false)
        binding.pokedexGrid.layoutManager = GridLayoutManager(context, 2)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.pokedex_grid)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.layoutManager = layoutManager

        val adapter = PokedexListAdapter(emptyList())
        recyclerView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                pokedexViewModel.pokemon.collect { pokemon ->
                    val displayedItems = pokedexViewModel.getDisplayedItems()
                    val newItems = pokemon.filterNot { displayedItems.contains(it) }
                    pokedexViewModel.updateDisplayedItems(newItems)
                    adapter.updateData(pokemon)
                }
            }
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                val threshold = 6
                if (lastVisibleItemPosition + threshold >= totalItemCount) {
                    pokedexViewModel.viewModelScope.launch {
                        pokedexViewModel.loadMoreData(totalItemCount, 20, true)
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
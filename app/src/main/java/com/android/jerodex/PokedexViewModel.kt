package com.android.jerodex

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.jerodex.database.PokemonInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PokedexViewModel : ViewModel() {

    private val pokemonRepository = PokemonRepository.get()

    private val _pokemon: MutableStateFlow<List<PokemonInformation>> = MutableStateFlow(emptyList())
    val pokemon: StateFlow<List<PokemonInformation>>
        get() = _pokemon.asStateFlow()

    private val pokedexRepository = PokedexRepository()
    private val displayedItems: MutableList<PokemonInformation> = mutableListOf()
    private val requestMutex = Mutex()
    private var isRequestInProgress = false

    init {
        viewModelScope.launch {
            val pokemonList: List<PokemonInformation>? =
                pokemonRepository.getPokemonList().firstOrNull()
            val isDatabaseEmpty: Boolean = pokemonList.isNullOrEmpty()
            if (isDatabaseEmpty) {
                loadMoreData(0, 21, false)
            } else {
                pokemonRepository.getPokemonList().collect() {
                    _pokemon.value = it
                }
            }

        }
    }

    suspend fun loadMoreData(offset: Int, limit: Int, boolean: Boolean) {
        //TODO Add max list count
        requestMutex.withLock {
            if (!isRequestInProgress || boolean) {
                isRequestInProgress = true
                try {
                    val pokedexList = pokedexRepository.getPokedexList(offset, limit).results
                    for (item in pokedexList) {
                        if (pokemonRepository.getPokemon(item.name)?.name == null) {
                            val detailsResponse = pokedexRepository.getPokemonByUrl(item.url)
                            val details = PokemonInformation(
                                name = item.name,
                                id = detailsResponse.id,
                                sprites = detailsResponse.sprites.frontDefault,
                                types = detailsResponse.types.map { it.type.name },
                                url = item.url
                            )
                            pokemonRepository.addPokedex(details)
                        }
                    }

                } catch (e: Exception) {
                    Log.e("ErrorTag", "Exception retrofit", e)
                } finally {
                    val updatedData: List<PokemonInformation> =
                        pokemonRepository.getPokemonList().first()
                    _pokemon.value = updatedData
                    isRequestInProgress = false
                }
            }
        }
    }

    fun getDisplayedItems(): List<PokemonInformation> {
        return displayedItems
    }
    fun updateDisplayedItems(newItems: List<PokemonInformation>) {
        displayedItems.addAll(newItems)
    }
}

/*
suspend fun loadMoreData(offset: Int, limit: Int, boolean: Boolean) {
        if (!isRequestInProgress || boolean) {
            isRequestInProgress = true
            try {
                val pokedexList = pokedexRepository.getPokedexList(offset, limit).results
                for (item in pokedexList) {
                    if (pokemonRepository.getPokemon(item.name)?.name == null) {
                        val detailsResponse = pokedexRepository.getPokemonByUrl(item.url)
                        val details = PokemonInformation(
                            name = item.name,
                            id = detailsResponse.id,
                            sprites = detailsResponse.sprites.frontDefault,
                            types = detailsResponse.types.map { it.type.name },
                            url = item.url )
                        pokemonRepository.addPokedex(details)
                    }
                }
                val updatedData: List<PokemonInformation> =
                    pokemonRepository.getPokemonList().first()
                _pokemon.value = updatedData
            } catch (e: Exception) {
                Log.e("ErrorTag", "Exception retrofit", e)

            } finally {
                isRequestInProgress = false
            }
        }
    }
 */
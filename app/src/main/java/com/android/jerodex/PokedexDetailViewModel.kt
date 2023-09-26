package com.android.jerodex

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.jerodex.database.PokemonInformation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PokedexDetailViewModel(name: String) : ViewModel() {

    private val pokemonRepository = PokemonRepository.get()

    private val _pokemon: MutableStateFlow<PokemonInformation?> = MutableStateFlow(null)
    val pokemon: StateFlow<PokemonInformation?> = _pokemon.asStateFlow()
    var maxId:Int = 0




    init {
        viewModelScope.launch {
            maxId = pokemonRepository.getPokemonList().first().last().id
            Log.d("MaxId", "$maxId")
            _pokemon.value = pokemonRepository.getPokemon(name)
        }
    }

    fun changePokemon(id:Int):Pair<Boolean, String>{
        val newId = when {
            id > maxId -> maxId
            id <= 0 -> 1
            else -> id
        }
        var flag = false
        var text = ""
        if(id!=newId){
            flag = true
            text = if (id > maxId) {
                "You haven't encountered the next Pokémon yet"
            } else {
                "There is no previous entry"
            }
        }
        viewModelScope.launch {
            _pokemon.value = pokemonRepository.getPokemonId(newId)
        }

        return Pair(flag, text)
    }




}


class PokedexDetailViewModelFactory(
    private val name: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokedexDetailViewModel(name) as T
    }
}
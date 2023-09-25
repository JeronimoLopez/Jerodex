package com.android.jerodex

import android.app.Application

class PokedexApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        PokemonRepository.initialize(this)
    }
}
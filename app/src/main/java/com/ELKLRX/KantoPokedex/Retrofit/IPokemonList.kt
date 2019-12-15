package com.ELKLRX.KantoPokedex.Retrofit

import com.ELKLRX.KantoPokedex.Model.Pokedex
import retrofit2.http.GET
import io.reactivex.Observable

interface IPokemonList {
    @get:GET("pokedex.json")
    val listPokemon:Observable<Pokedex>
}
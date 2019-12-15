package com.ELKLRX.KantoPokedex

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.ELKLRX.KantoPokedex.Common.Common
import com.ELKLRX.KantoPokedex.Common.Common.KEY_ENABLE_HOME
import com.ELKLRX.KantoPokedex.Common.Common.KEY_NUM_EVOLUTION
import com.ELKLRX.KantoPokedex.Common.Common.KEY_POKEMON_TYPE
import com.ELKLRX.KantoPokedex.Model.Pokemon
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Create Broadcast handle
    private val showDetail = object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent!!.action!!.toString() == Common.KEY_ENABLE_HOME) {
                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                supportActionBar!!.setDisplayShowHomeEnabled(true)

                //replace Fragement
                val detailFragment:PokemonDetail = PokemonDetail.getInstance()
                val num:String = intent.getStringExtra("num")
                val bundle = Bundle()
                bundle.putString("num",num)
                detailFragment.arguments = bundle

                val fragmentTransaction:FragmentTransaction = supportFragmentManager.beginTransaction()

                fragmentTransaction.replace(R.id.list_pokemon_fragment, detailFragment)
                fragmentTransaction.addToBackStack("detail")
                fragmentTransaction.commit()

                //Set Pokemon Name for Toolbar
                val pokemon:Pokemon? = Common.findPokemonByNum(num)
                toolbar.title = pokemon!!.name
            }
        }

    }

    private val showPokemonType = object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent!!.action!!.toString() == Common.KEY_POKEMON_TYPE) {

                //replace Fragement
                val pokemonType = PokemonType.getInstance()
                val type:String = intent.getStringExtra("type")
                val bundle = Bundle()
                bundle.putString("type",type)
                pokemonType!!.arguments = bundle

                supportFragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE)

                val fragmentTransaction:FragmentTransaction = supportFragmentManager.beginTransaction()

                fragmentTransaction.replace(R.id.list_pokemon_fragment, pokemonType)
                fragmentTransaction.addToBackStack("type")
                fragmentTransaction.commit()

                //Set Pokemon Name for Toolbar
                //val pokemon:Pokemon? = Common.findPokemonByNum(num)
                toolbar.title = "POKEMON TYPE "+type.toUpperCase()

            }
        }

    }

    private val showEvolution = object:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent!!.action!!.toString() == KEY_NUM_EVOLUTION) {

                //replace Fragement
                val detailFragment:PokemonDetail = PokemonDetail.getInstance()
                val bundle = Bundle()
                val num = intent.getStringExtra("num")
                bundle.putString("num", num)
                detailFragment.arguments = bundle

                val fragmentTransaction:FragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.remove(detailFragment) //Remove Current
                fragmentTransaction.replace(R.id.list_pokemon_fragment, detailFragment)
                fragmentTransaction.addToBackStack("detail")
                fragmentTransaction.commit()

                //Set Pokemon Name for Toolbar
                val pokemon:Pokemon? = Common.findPokemonByNum(num)
                toolbar.title = pokemon!!.name
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle("KANTO POKEDEX")
        setSupportActionBar(toolbar)

        //Register Broadcast

        LocalBroadcastManager.getInstance(this)
            .registerReceiver(showDetail, IntentFilter(KEY_ENABLE_HOME))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(showEvolution, IntentFilter(KEY_NUM_EVOLUTION))
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(showPokemonType, IntentFilter(KEY_POKEMON_TYPE))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                toolbar.title = "KANTO POKEDEX"

                //Clear all fragment in stack with name "detail"
                supportFragmentManager.popBackStack(
                    "detail",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )

                supportFragmentManager.popBackStack(
                    "type",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )

                //replace fragment

                val pokemonList = PokemonList.getInstance()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.remove(pokemonList)
                fragmentTransaction.replace(R.id.list_pokemon_fragment,pokemonList)
                fragmentTransaction.commit()



                supportActionBar!!.setDisplayShowHomeEnabled(false)
                supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            }
        }
        return true
    }

}

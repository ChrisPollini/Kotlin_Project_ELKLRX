package com.ELKLRX.KantoPokedex.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ELKLRX.KantoPokedex.Common.Common
import com.ELKLRX.KantoPokedex.Interface.IItemClickListener
import com.ELKLRX.KantoPokedex.Model.Pokemon
import com.ELKLRX.KantoPokedex.R


class PokemonListAdapter(internal var context: Context,
                         internal var pokemonList:List<Pokemon>):RecyclerView.Adapter<PokemonListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.pokemon_list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(context).load(pokemonList[position].img).into(holder.imgPokemon)
        holder.txtPokemon.text = pokemonList[position].name

        holder.setItemClickListener(object:IItemClickListener {
            override fun onClick(view: View, position: Int) {
                LocalBroadcastManager.getInstance(context)
                    .sendBroadcast(Intent(Common.KEY_ENABLE_HOME).putExtra("num",pokemonList[position].num))
            }

        })
    }

    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        internal var imgPokemon:ImageView
        internal var txtPokemon:TextView

        internal var itemClickListener:IItemClickListener?=null

        fun setItemClickListener(iItemClickListener:IItemClickListener) {
            this.itemClickListener = iItemClickListener
        }


        init {
            imgPokemon = itemView.findViewById(R.id.pokemon_image) as ImageView
            txtPokemon = itemView.findViewById(R.id.pokemon_name) as TextView

            itemView.setOnClickListener {
                view -> itemClickListener!!.onClick(view, adapterPosition)
            }
        }
    }
}
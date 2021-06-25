package com.example.consumir_api_pokemon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.consumir_api_pokemon.models.Pokemon;

import java.util.ArrayList;

public class ListPokemonAdapter extends RecyclerView.Adapter<ListPokemonAdapter.ViewHolder> {

    private ArrayList<Pokemon> listPokemon;
    private Context context;
    private final String URLBASE = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/";

    public ListPokemonAdapter(Context context) {
        this.listPokemon = new ArrayList<>();
        this.context = context;
    }
    @Override
    public ListPokemonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon,
                parent,
                false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pokemon pokemon = listPokemon.get(position);
        holder.tvName.setText(pokemon.getName());

        Glide.with(context)
                .load(URLBASE + pokemon.getNumber()+".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgPokemon);
    }

    @Override
    public int getItemCount() {
        return listPokemon.size();
    }

    public void addPokemon(ArrayList<Pokemon> listPokemon){
        this.listPokemon.addAll(listPokemon);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPokemon;
        private TextView tvName;

        public ViewHolder(View itemView) {
            super(itemView);

            imgPokemon = itemView.findViewById(R.id.imgPokemon);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}


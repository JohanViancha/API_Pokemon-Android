package com.example.consumir_api_pokemon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.consumir_api_pokemon.PokeApi.PokemonServices;
import com.example.consumir_api_pokemon.models.Pokemon;
import com.example.consumir_api_pokemon.models.PokemonResponse;

import java.security.Policy;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private RecyclerView recyclerView;
    private ListPokemonAdapter listPokemonAdapter;
    private final String URLBASE = "https://pokeapi.co/api/v2/";
    private int offset;
    private boolean loadmore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        listPokemonAdapter = new ListPokemonAdapter(this);
        recyclerView.setAdapter(listPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0){
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if(loadmore){
                        if((visibleItemCount+pastVisibleItems) >= totalItemCount)
                            loadmore = false;
                            offset +=20;
                            getData(offset);
                    }
                }
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(URLBASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        offset = 0;
        loadmore = true;
        getData(offset);
    }

    private void getData(int offset){

        PokemonServices services =retrofit.create(PokemonServices.class);
        Call<PokemonResponse> pokemonResponseCall = services.getListPokemon(20,offset);

        pokemonResponseCall.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                loadmore = true;
                if(response.isSuccessful()){
                    PokemonResponse pokemonResponse = response.body();
                    ArrayList<Pokemon> listPokemon = (ArrayList<Pokemon>) pokemonResponse.getResults();
                    listPokemonAdapter.addPokemon(listPokemon);

                }else{
                    Log.e("Error", " Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                Log.e("Error", " Error: " +t.getMessage());
            }
        });

    }
}
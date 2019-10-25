package com.example.flicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.flicks.adapter.RecyclerViewAdapter;
import com.example.flicks.api.ApiModule;
import com.example.flicks.api.ApiService;
import com.example.flicks.entity.NowPlaying;
import com.example.flicks.mapper.DataMapper;
import com.example.flicks.model.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    protected  List<Movie> movieList;
    protected RecyclerViewAdapter adapter;
    protected DataMapper mapper;

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rv);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        if(movieList == null){
            movieList = new ArrayList<>();
            setupList();
            getNowPlaying();
        }
        else adapter.setData(movieList);

    }
    private void getNowPlaying() {

        ApiModule.getInstance().getNowPlaying(ApiService.API_KEY)
                .enqueue(new Callback<NowPlaying>() {
                    @Override
                    public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                        if(response.body() != null){
                            movieList = mapper.transform(response.body());
                            adapter.setData(movieList);
                        }
                    }

                    @Override
                    public void onFailure(Call<NowPlaying> call, Throwable t) {
                    }
                });

    }

    private void setupList() {
        mapper = new DataMapper();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new RecyclerViewAdapter(MainActivity.this,movieList);
        adapter.setData(movieList==null ? new ArrayList<Movie>(): movieList);
        recyclerView.setAdapter(adapter);

        adapter.setListener(new RecyclerViewAdapter.IClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("movie",movie);
                Intent detail =new Intent(MainActivity.this, DetailActivity.class);
                detail.putExtras(bundle);
                startActivity(detail);            }
        });

        this.recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearData();
                getNowPlaying();
                swipeRefreshLayout.setRefreshing(false);

            }
        });

    }
}

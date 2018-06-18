package com.example.usphuong.flicks;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.usphuong.flicks.adapter.RecyclerViewAdapter;
import com.example.usphuong.flicks.api.ApiModule;
import com.example.usphuong.flicks.api.ApiService;
import com.example.usphuong.flicks.model.Movie;
import com.example.usphuong.flicks.entity.NowPlaying;
import com.example.usphuong.flicks.mapper.DataMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{

    protected  List<Movie> movieList;
    protected RecyclerViewAdapter adapter;
    protected DataMapper mapper;

    @BindView(R.id.rv) RecyclerView recyclerView;
    @BindView(R.id.swipeRefresh) SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setupList();
    }
}

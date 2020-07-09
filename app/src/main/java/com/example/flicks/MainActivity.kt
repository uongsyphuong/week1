package com.example.flicks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.flicks.adapter.RecyclerViewAdapter
import com.example.flicks.api.ApiModule
import com.example.flicks.api.ApiService
import com.example.flicks.entity.NowPlaying
import com.example.flicks.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    //
    private var movieList: MutableList<Movie>? = null
    private var adapter: RecyclerViewAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv)
        swipeRefreshLayout = findViewById(R.id.swipeRefresh)
        movieList = ArrayList()
        setupList()
        getNowPlaying()
    }

    private fun setupList() {
        // khai bao recycler view
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = mLayoutManager
        adapter = RecyclerViewAdapter(this@MainActivity, movieList
                ?: ArrayList()) { movie -> // truyền phim vào trong màn hình sau
            val bundle = Bundle()
            bundle.putParcelable("movie", movie)
            val detail = Intent(this@MainActivity, DetailActivity::class.java)
            detail.putExtras(bundle)
            startActivity(detail)
        }

        //set adapter cho recycler view
        recyclerView!!.adapter = adapter

        //
        swipeRefreshLayout!!.setOnRefreshListener {
            adapter!!.clearData()
            getNowPlaying()
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    // goi api để lấy danh sách phim
    private fun getNowPlaying() {

        // goi api để lấy danh sách phim
        ApiModule.instance?.getNowPlaying(ApiService.API_KEY)
                ?.enqueue(object : Callback<NowPlaying?> {
                    override fun onResponse(call: Call<NowPlaying?>, response: Response<NowPlaying?>) {
                        if (response.body() != null) {
                            adapter!!.setData(response.body()?.results as MutableList<Movie>)
                        }
                    }

                    override fun onFailure(call: Call<NowPlaying?>, t: Throwable) {
                        Log.d("Leo", "fail")
                    }
                })
    }
}
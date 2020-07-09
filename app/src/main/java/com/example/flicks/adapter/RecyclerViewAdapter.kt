package com.example.flicks.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.flicks.R
import com.example.flicks.adapter.RecyclerViewAdapter.MyViewHolder
import com.example.flicks.model.Movie

class RecyclerViewAdapter(private val mContext: Context, private var mData: MutableList<Movie>?, private val onClickItem: (Movie?) -> Unit) : RecyclerView.Adapter<MyViewHolder>() {
    fun setData(data: MutableList<Movie>) {
        mData = data
        notifyDataSetChanged()
    }

    fun clearData() {
        mData?.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View
        val inflater = LayoutInflater.from(mContext)
        view = inflater.inflate(R.layout.movie_row_item, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tvName.text = mData?.get(position)?.title
        holder.tvOverview.text = mData?.get(position)?.overview
        Glide.with(mContext).load(mData?.get(position)?.posterUrl()).apply(RequestOptions()
                .fitCenter())
                .into(holder.imgPoster)
    }

    override fun getItemCount(): Int {
        return mData?.size?:0
    }
    
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var tvName: TextView = itemView.findViewById(R.id.rowname)
        var tvOverview: TextView = itemView.findViewById(R.id.overview)
        var imgPoster: ImageView = itemView.findViewById(R.id.poster)
        override fun onClick(v: View) {
            onClickItem(mData?.get(adapterPosition))
        }

        init {
            //
            itemView.setOnClickListener(this)
        }
    }
}
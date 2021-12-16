package com.example.btkfotopaylasim.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.btkfotopaylasim.R
import com.example.btkfotopaylasim.databinding.CardPostBinding
import com.example.btkfotopaylasim.model.Post
import com.example.btkfotopaylasim.util.downloadAPI
import com.example.btkfotopaylasim.util.plcProgresbar

class PostAdapter(var mContext:Context,var postlar:ArrayList<Post>):RecyclerView.Adapter<PostAdapter.PostHolder>() {
    inner class PostHolder(var cardPostBinding: CardPostBinding):RecyclerView.ViewHolder(cardPostBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val tasarim=DataBindingUtil.inflate<CardPostBinding>(LayoutInflater.from(mContext), R.layout.card_post,parent,false)
        return PostHolder(tasarim)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val post=postlar[position]
        holder.cardPostBinding.post=post
        holder.cardPostBinding.imageView2.downloadAPI(post.url, plcProgresbar(mContext))
    }

    override fun getItemCount(): Int {
        return postlar.size
    }

    fun updateList(list:List<Post>){
        postlar.clear()
        postlar.addAll(list)
        notifyDataSetChanged()
    }
}
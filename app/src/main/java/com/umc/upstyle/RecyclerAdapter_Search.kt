package com.umc.upstyle

import Item_search
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerAdapter_Search(private val itemSearchList: List<Item_search>) :
    RecyclerView.Adapter<RecyclerAdapter_Search.ViewHolder>() {

    // ViewHolder 클래스
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.item_title) // 제목 TextView
        val image: ImageView = itemView.findViewById(R.id.item_image) // 이미지 ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemSearchList[position]

        // 텍스트 설정
        holder.title.text = item.description

        // Glide를 사용해 이미지 로드
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.image)
    }

    override fun getItemCount(): Int = itemSearchList.size
}

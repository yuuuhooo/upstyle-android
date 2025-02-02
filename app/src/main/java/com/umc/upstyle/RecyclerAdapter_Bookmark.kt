package com.umc.upstyle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerAdapter_Bookmark(private var items: List<Item_bookmark>) :
    RecyclerView.Adapter<RecyclerAdapter_Bookmark.BookmarkViewHolder>() {

    class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_bookmark, parent, false)
        return BookmarkViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val item = items[position]

        // 아이템 정보 설정
        holder.itemTitle.text = item.description

        // Glide로 이미지 로드
        Glide.with(holder.itemView.context)
            .load(item.imageUrl)
            .into(holder.itemImage)
    }

    override fun getItemCount(): Int = items.size

    // 북마크된 아이템 목록 업데이트
    fun updateList(newItems: List<Item_bookmark>) {
        items = newItems
        notifyDataSetChanged()
    }
}

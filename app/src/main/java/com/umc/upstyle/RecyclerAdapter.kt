package com.umc.upstyle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.upstyle.databinding.ItemLayoutBinding

class RecyclerAdapter(
    private val itemList: List<Item>,
    private val itemClickListener: OnItemClickListener // 클릭 리스너를 어댑터의 생성자로 전달
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // 클릭 리스너 인터페이스 정의
    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }

    // ViewHolder 클래스
    class ViewHolder(val binding: ItemLayoutBinding, val itemClickListener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            // 아이템 클릭 시 리스너 호출
            binding.root.setOnClickListener {
                val item = binding.item // 데이터를 ViewHolder에서 가져옴
                itemClickListener.onItemClick(item) // 클릭 리스너 호출
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, itemClickListener) // 클릭 리스너를 ViewHolder로 전달
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Glide를 사용해 이미지 로드
        Glide.with(holder.binding.root.context)
            .load(item.imageUrl) // 이미지 URL
            .centerCrop() // 이미지 자르기
            .into(holder.binding.itemImage) // ImageView에 설정

        // 텍스트 바인딩
        holder.binding.item = item // 바인딩된 item 객체에 값을 설정 (item_title의 텍스트가 자동으로 업데이트됨)
    }

    override fun getItemCount(): Int = itemList.size

    // 뷰 재활용 시 이미지 클리어
    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.binding.root.context).clear(holder.binding.itemImage) // 이미지 클리어
    }
}

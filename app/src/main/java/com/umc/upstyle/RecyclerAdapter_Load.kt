package com.umc.upstyle

import Item_load
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerAdapter_Load(
    private val itemList: List<Item_load>,
    private val onItemClick: (Item_load, Any?) -> Unit // 클릭 리스너 추가
) : RecyclerView.Adapter<RecyclerAdapter_Load.ViewHolder>() {

    private var selectedPosition: Int = RecyclerView.NO_POSITION // 선택된 아이템 위치

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageButton: AppCompatImageButton = itemView.findViewById(R.id.item_image) // 아이템 이미지 버튼
        val itemTitle: TextView = itemView.findViewById(R.id.item_title) // 아이템 제목
        val icUnselected: ImageButton = itemView.findViewById(R.id.ic_unselected) // 선택 아이콘
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_item_load, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]  // 리스트에서 position으로 아이템 가져옴

        val context = holder.imageButton.context

        // 제목 설정
        holder.itemTitle.text = item.description

        // Glide로 이미지 로드
        Glide.with(context)
            .load(item.imageUrl)
            .into(holder.imageButton)

        // 현재 아이템이 선택되었는지 확인 후 UI 변경
        if (position == selectedPosition) {
            holder.icUnselected.setImageResource(R.drawable.ic_selected) // 선택된 상태
        } else {
            holder.icUnselected.setImageResource(R.drawable.ic_unselected) // 선택되지 않은 상태
        }

        // 이미지를 클릭하면 선택되도록 설정
        holder.imageButton.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = position

            // LoadItemFragment에서 아이템 선택 시
            onItemClick(item, position)


            // 기존 선택된 아이템을 해제하고 새 아이템을 선택
            notifyItemChanged(previousSelected)  // 기존 선택된 아이템 갱신
            notifyItemChanged(selectedPosition) // 새로 선택된 아이템 갱신
        }
    }

    override fun getItemCount(): Int = itemList.size

    fun setSelectedPosition(position: Any?) {
        val previousSelected = selectedPosition
        selectedPosition = position as Int

        // UI 갱신
        notifyItemChanged(previousSelected) //이전 선택 해제
        notifyItemChanged(selectedPosition) //다음 선택
    }
}

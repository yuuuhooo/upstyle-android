package com.umc.upstyle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.upstyle.databinding.ItemVoteBinding
import com.umc.upstyle.model.VoteItem

class VoteItemAdapter(
    private val voteItems: MutableList<VoteItem>,
    private val onItemClick: (Int) -> Unit,
    private val onAddClick: () -> Unit
) : RecyclerView.Adapter<VoteItemAdapter.VoteItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoteItemViewHolder {
        val binding = ItemVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoteItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoteItemViewHolder, position: Int) {
        if (position < voteItems.size) {
            holder.bind(voteItems[position])  // 기존 아이템 바인딩
        } else {
            holder.bindAddButton()  // 추가 버튼 바인딩
        }
    }

    override fun getItemCount(): Int {
        return if (voteItems.size >= 4) voteItems.size else voteItems.size + 1
    }


    inner class VoteItemViewHolder(private val binding: ItemVoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(voteItem: VoteItem) {
            // ✅ Glide를 사용하여 이미지 로드
            Glide.with(binding.root.context)
                .load(voteItem.imageUrl) // `imageUrl`이 String일 경우
                .into(binding.ivVoteItemImage)

            binding.etVoteItemText.setText(voteItem.name) // ✅ EditText 값 설정
            binding.root.setOnClickListener { onItemClick(adapterPosition) }
        }

        fun bindAddButton() {
            if (voteItems.size >= 4) {
                binding.root.visibility = View.GONE  // 추가 버튼 숨기기
            } else {
                binding.root.visibility = View.VISIBLE
                binding.icPicture.setImageResource(com.umc.upstyle.R.drawable.ic_plus)
                binding.etVoteItemText.alpha = 0.3f
                binding.barFirst.alpha = 0.3f
                binding.barSecond.alpha = 0.3f
                binding.ivVoteItemImage.alpha = 0.3f
                binding.ivPencil.visibility = View.GONE
                binding.root.setOnClickListener { onAddClick() }
            }
        }

    }
}

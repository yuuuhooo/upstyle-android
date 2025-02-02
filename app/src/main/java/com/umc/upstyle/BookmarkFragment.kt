package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.upstyle.databinding.FragmentBookmarkBinding

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RecyclerAdapter_Bookmark

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        adapter = RecyclerAdapter_Bookmark(emptyList())
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        // 버튼 ID 리스트
        val buttonIds = listOf(
            R.id.btn_all, R.id.btn_outer, R.id.btn_top, R.id.btn_bottom,
            R.id.btn_shoes, R.id.btn_bag, R.id.btn_other
        )

        // 기본 선택 버튼 (ALL)
        val selectedButton = binding.root.findViewById<ConstraintLayout>(R.id.btn_all)
        selectedButton.setBackgroundResource(R.drawable.bg_circle_yellow) // 기본 선택 배경 적용

        // 각 버튼 클릭 이벤트 설정
        buttonIds.forEach { id ->
            val button = binding.root.findViewById<ConstraintLayout>(id)
            button.setOnClickListener {
                selectButton(button)
                filterBookmarks(id)
            }
        }

        // 초기 상태 불러오기
        filterBookmarks(R.id.btn_all)
    }

    private fun selectButton(button: ConstraintLayout) {
        // 모든 버튼을 기본 색으로 변경
        val buttonIds = listOf(
            R.id.btn_all, R.id.btn_outer, R.id.btn_top, R.id.btn_bottom,
            R.id.btn_shoes, R.id.btn_bag, R.id.btn_other
        )

        buttonIds.forEach { id ->
            binding.root.findViewById<ConstraintLayout>(id)
                .setBackgroundResource(R.drawable.bg_circle_gray)
        }

        // 현재 선택된 버튼을 노란색으로 변경
        button.setBackgroundResource(R.drawable.bg_circle_yellow)
    }

    private fun filterBookmarks(selectedCategoryId: Int) {
        val outerBookmarked = loadBookmarkState("bookmark_outer")
        val topBookmarked = loadBookmarkState("bookmark_top")
        val bottomBookmarked = loadBookmarkState("bookmark_bottom")
        val bagBookmarked = loadBookmarkState("bookmark_bag")
        val shoesBookmarked = loadBookmarkState("bookmark_shoes")
        val otherBookmarked = loadBookmarkState("bookmark_other")

        val bookmarkedItems = mutableListOf<Item_bookmark>()

        when (selectedCategoryId) {
            R.id.btn_all -> { // 모든 북마크된 아이템을 보여줌
                if (outerBookmarked) bookmarkedItems.add(Item_bookmark("", "https://www.ocokorea.com//upload/images/product/148/148607/Product_1693647123947.jpg"))
                if (topBookmarked) bookmarkedItems.add(Item_bookmark("", "https://example.com/top.jpg"))
                if (bottomBookmarked) bookmarkedItems.add(Item_bookmark("", "https://example.com/bottom.jpg"))
                if (bagBookmarked) bookmarkedItems.add(Item_bookmark("", "https://example.com/bag.jpg"))
                if (shoesBookmarked) bookmarkedItems.add(Item_bookmark("", "https://example.com/shoes.jpg"))
                if (otherBookmarked) bookmarkedItems.add(Item_bookmark("", "https://example.com/other.jpg"))
            }
            R.id.btn_outer -> if (outerBookmarked) bookmarkedItems.add(Item_bookmark("루스한 오버핏 블랙", "https://www.ocokorea.com//upload/images/product/148/148607/Product_1693647123947.jpg"))
            R.id.btn_top -> if (topBookmarked) bookmarkedItems.add(Item_bookmark("오버핏 레귤러 블랙", "https://example.com/top.jpg"))
            R.id.btn_bottom -> if (bottomBookmarked) bookmarkedItems.add(Item_bookmark("일단 아무 텍스트", "https://example.com/bottom.jpg"))
            R.id.btn_bag -> if (bagBookmarked) bookmarkedItems.add(Item_bookmark("숄더백", "https://example.com/bag.jpg"))
            R.id.btn_shoes -> if (shoesBookmarked) bookmarkedItems.add(Item_bookmark("부츠/워커", "https://example.com/shoes.jpg"))
            R.id.btn_other -> if (otherBookmarked) bookmarkedItems.add(Item_bookmark("기타", "https://example.com/other.jpg"))
        }

        // RecyclerView 업데이트
        adapter.updateList(bookmarkedItems)
    }

    private fun loadBookmarkState(key: String): Boolean {
        val preferences = requireActivity().getSharedPreferences("BookmarkPrefs", android.content.Context.MODE_PRIVATE)
        return preferences.getBoolean(key, false) // 기본값: false (북마크 해제 상태)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.umc.upstyle

import android.os.Bundle
import android.provider.MediaStore.Video.VideoColumns.CATEGORY
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.upstyle.databinding.FragmentBookmarkBinding
import java.util.Locale.Category

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

        // RecyclerView 설정
        adapter = RecyclerAdapter_Bookmark(emptyList())
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = adapter

        //아이템 간격 추가 (아이템끼리 5dp, 화면 양끝 12.5dp)
        val spacingInPixels = (5 * resources.displayMetrics.density).toInt() // 5dp → px 변환
        val edgeSpacingInPixels = (12.5 * resources.displayMetrics.density).toInt() // 12.5dp → px 변환
        binding.recyclerView.addItemDecoration(LayoutSpace(2, spacingInPixels, edgeSpacingInPixels))

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
        val preferences = requireActivity().getSharedPreferences("BookmarkPrefs", android.content.Context.MODE_PRIVATE)

        val bookmarkedItems = mutableListOf<Item_bookmark>()

        val uniqueItems = mutableSetOf<String>() // 중복 제거를 위해
        val allKeys = preferences.all.keys.filter { it.endsWith("_name") }

        for (key in allKeys) {
            val itemKey = key.removeSuffix("_name") // _name 제거하여 실제 북마크 키 획득
            if (preferences.getBoolean(itemKey, false)) {
                val name = preferences.getString("${itemKey}_name", null)
                val image = preferences.getString("${itemKey}_image", null)
                if (!name.isNullOrEmpty() && !image.isNullOrEmpty()) {
                    val category = itemKey.split("_")[1] // 예: "bookmark_outer_아이템이름" → "outer"
                    bookmarkedItems.add(Item_bookmark(name, image, category))
                }
            }
        }

        // 선택된 카테고리에 따라 필터링
        val filteredItems = when (selectedCategoryId) {
            R.id.btn_all -> {
                // 중복 제거하여 리스트 생성 (이미지만 표시, name을 null로 설정)
                bookmarkedItems.filter { uniqueItems.add(it.image) }
                    .map { Item_bookmark("", it.image, it.category) }
            }
            R.id.btn_outer -> bookmarkedItems.filter { it.category == "outer" }
            R.id.btn_top -> bookmarkedItems.filter { it.category == "top" }
            R.id.btn_bottom -> bookmarkedItems.filter { it.category == "bottom" }
            R.id.btn_bag -> bookmarkedItems.filter { it.category == "bag" }
            R.id.btn_shoes -> bookmarkedItems.filter { it.category == "shoes" }
            R.id.btn_other -> bookmarkedItems.filter { it.category == "other" }
            else -> bookmarkedItems
        }

        // RecyclerView 업데이트
        adapter.updateList(filteredItems)
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

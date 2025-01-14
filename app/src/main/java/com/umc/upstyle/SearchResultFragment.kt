package com.umc.upstyle

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.upstyle.databinding.FragmentSearchResultBinding
import java.io.File

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!! // Non-nullable로 사용하기 위한 접근자

    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Arguments로 전달된 category 값 가져오기
        category = arguments?.getString("CATEGORY")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        val items = loadItemsFromPreferences()
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // RecyclerAdapter에 아이템 클릭 리스너 추가
        binding.recyclerView.adapter = RecyclerAdapter(items, object : RecyclerAdapter.OnItemClickListener {
            override fun onItemClick(item: Item) {
                // 클릭된 아이템에 따라 다른 프래그먼트로 이동
                val fragment = OthersOotdFragment.newInstance(item)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null) // 백 스택에 추가해서 뒤로 가기 가능
                    .commit()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.recyclerView.adapter?.notifyDataSetChanged() // 리사이클러뷰 어댑터 데이터 갱신
    }

    private fun loadItemsFromPreferences(): List<Item> {
        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        // 해당 카테고리에 맞는 텍스트와 이미지 경로 가져오기
        val description = preferences.getString(category, "$category 정보 없음")
        val savedImagePath = preferences.getString("SAVED_IMAGE_PATH", null)

        // 기본 샘플 데이터 -> ui확인용
        val items = mutableListOf(
            Item("루스한 오버핏 블랙", "https://www.ocokorea.com//upload/images/product/148/148607/Product_1693647123947.jpg"),
            Item("오버핏 레귤러 블랙", "https://sitem.ssgcdn.com/70/26/15/item/1000363152670_i1_750.jpg"),
            Item("일단 아무 텍스트", "https://m.likeygirl.kr/web/product/big/20231204_000027_LK.jpg")
        )

        // 저장된 데이터 추가
        if (!savedImagePath.isNullOrEmpty() && !description.isNullOrEmpty()) {
            val fileUri = Uri.fromFile(File(savedImagePath))
            items.add(0, Item(description, fileUri.toString()))
        }

        return items
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지: 뷰가 파괴되면 바인딩 해제
        _binding = null
    }

    companion object {
        fun newInstance(category: String): SearchResultFragment {
            val fragment = SearchResultFragment()
            val args = Bundle()
            args.putString("CATEGORY", category) // 카테고리 전달
            fragment.arguments = args
            return fragment
        }
    }
}

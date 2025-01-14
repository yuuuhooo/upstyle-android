package com.umc.upstyle

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class SearchOuterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 레이아웃을 inflate
        val view = inflater.inflate(R.layout.fragment_search_outer, container, false)

        // RecyclerView 초기화
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)

        // 저장된 사진 불러오기
        val items = loadItemsFromPreferences()

        // RecyclerView 설정
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2) // 한 줄에 2개
        recyclerView.adapter = RecyclerAdapter(items)

        return view
    }


    // SharedPreferences에서 데이터 불러오기
    private fun loadItemsFromPreferences(): List<Item> {
        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        // 저장된 OUTER 텍스트와 이미지 경로 가져오기
        val outerDescription = preferences.getString("OUTER", "OUTER 정보 없음")
        val savedImagePath = preferences.getString("SAVED_IMAGE_PATH", null)

        // 기본 샘플 데이터
        val items = mutableListOf(
            Item("루스한 오버핏 블랙", "https://www.ocokorea.com//upload/images/product/148/148607/Product_1693647123947.jpg"),
            Item("오버핏 레귤러 블랙", "https://sitem.ssgcdn.com/70/26/15/item/1000363152670_i1_750.jpg"),
            Item("일단 아무 텍스트", "https://m.likeygirl.kr/web/product/big/20231204_000027_LK.jpg")

        )

        // 저장된 OUTER 데이터 추가
        if (!savedImagePath.isNullOrEmpty() && !outerDescription.isNullOrEmpty()) {
            val fileUri = Uri.fromFile(File(savedImagePath))
            items.add(0, Item(outerDescription, fileUri.toString()))
        }

        return items
    }

}

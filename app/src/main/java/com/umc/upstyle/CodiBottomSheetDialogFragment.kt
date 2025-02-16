package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.upstyle.data.model.ClothIdResponse
import com.umc.upstyle.data.model.ClothRequestDTO
import com.umc.upstyle.data.model.ClothRequestDesDTO
import com.umc.upstyle.databinding.FragmentCodiBottomSheetDialogBinding

class CodiBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentCodiBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var clothList: MutableList<ClothRequestDesDTO>
    private lateinit var clothIDList: MutableList<ClothIdResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCodiBottomSheetDialogBinding.inflate(inflater, container, false)

        // Bundle에서 clothList와 clothIDList를 받음
        arguments?.let {
            clothList = it.getParcelableArrayList<ClothRequestDesDTO>("CLOTH_LIST") ?: mutableListOf()
            clothIDList = it.getParcelableArrayList<ClothIdResponse>("CLOTH_ID_LIST") ?: mutableListOf()
        }

        // UI 업데이트
        updateUI(clothList)

        // POST 요청을 위한 데이터 처리
        handleApiRequest(clothIDList)

        return binding.root
    }

    // UI 업데이트
    private fun updateUI(clothList: MutableList<ClothRequestDesDTO>) {
        // clothList를 순회하며 clothKindId에 따라 TextView 업데이트
        for (cloth in clothList) {
            when (cloth.clothKindId) {
                1 -> binding.tvOuter.text = cloth.description // OUTER (1)
                2 -> binding.tvTop.text = cloth.description   // TOP (2)
                3 -> binding.tvBottom.text = cloth.description
                4 -> binding.tvShoes.text = cloth.description
                5 -> binding.tvBag.text = cloth.description
                6 -> binding.tvOther.text = cloth.description
            }
        }
    }


    // API 요청 처리
    private fun handleApiRequest(clothIDList: MutableList<ClothIdResponse>) {
        // 여기서 clothIDList를 사용하여 API 요청을 보낼 수 있음
        // 예를 들어 Retrofit을 사용하여 POST 요청을 보낼 수 있음
    }

    companion object {
        const val TAG = "CodiBottomModalSheet"

        fun newInstance(clothList: MutableList<ClothRequestDesDTO>, clothIDList: MutableList<ClothIdResponse>): CodiBottomSheetFragment {
            return CodiBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList("CLOTH_LIST", ArrayList(clothList))
                    putParcelableArrayList("CLOTH_ID_LIST", ArrayList(clothIDList))
                }
            }
        }

    }
}

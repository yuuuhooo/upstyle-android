package com.umc.upstyle

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.umc.upstyle.databinding.FragmentSearchBinding

// 필요한 UI 요소와 동작을 여기에 추가할 수 있습니다
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!! // Non-nullable로 사용하기 위한 접근자

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // 뷰 바인딩 초기화
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // UI 작업 및 이벤트 처리
//        binding.myButton.setOnClickListener {
//            // 버튼 클릭 이벤트 처리
//        }

        binding.btnGoToOuter.setOnClickListener {

//            val navController = findNavController()
//            navController.navigate(R.id.action_searchFragment_to_closetResultFragment)


            val fragment = SearchOuterFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // 뒤로가기 시 이전 프래그먼트로 돌아가도록 추가
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 메모리 누수 방지: 뷰가 파괴되면 바인딩 해제
        _binding = null
    }
}

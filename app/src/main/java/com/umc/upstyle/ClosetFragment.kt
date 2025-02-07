package com.umc.upstyle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.upstyle.data.network.ApiService
import com.umc.upstyle.databinding.ActivityClosetBinding
import com.umc.upstyle.data.model.ClosetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClosetFragment : Fragment() {
    private var _binding: ActivityClosetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityClosetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = RetrofitClient.createService(ApiService::class.java)

        apiService.getUserCloset(userId = 1L).enqueue(object : Callback<ClosetResponse> {
            override fun onResponse(call: Call<ClosetResponse>, response: Response<ClosetResponse>) {
                if (response.isSuccessful) {
                    val userName = response.body()?.result?.userName
                    binding.tvUsername.text = "${userName}"


                } else {
                    binding.tvUsername.text = "오류"

                }
            }

            override fun onFailure(call: Call<ClosetResponse>, t: Throwable) {
                binding.tvUsername.text = "API 실패"

            }
        })

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.mainFragment) // 이전 Fragment로 이동
        }

        binding.btnGoOuter.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "OUTER")
            findNavController().navigate(action)
        }

        binding.btnGoTop.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "TOP")
            findNavController().navigate(action)
        }

        binding.btnGoBottom.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "BOTTOM")
            findNavController().navigate(action)
        }

        binding.btnGoShoes.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "SHOES")
            findNavController().navigate(action)
        }

        binding.btnGoBag.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "BAG")
            findNavController().navigate(action)
        }

        binding.btnGoOther.setOnClickListener {
            val action = ClosetFragmentDirections.actionClosetFragmentToClosetItemFragment(category = "OTHER")
            findNavController().navigate(action)
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ViewBinding 해제
    }
}

package com.umc.upstyle

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.umc.upstyle.databinding.FragmentOotdDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OotdDetailFragment : Fragment() {
    private var _binding: FragmentOotdDetailBinding? = null
    private val binding get() = _binding!!
    private val apiService = RetrofitClient.createService(OotdApiService::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOotdDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        // 전달된 OOTD ID 받기
        val ootdId = arguments?.getInt("SELECTED_OOTD_ID")

        if (ootdId != null) {
            fetchOotdData(ootdId) // 서버에서 해당 OOTD 데이터 요청
        }
    }

    private fun fetchOotdData(ootdId: Int) {
        apiService.getOOTDById(ootdId).enqueue(object : Callback<ApiResponse<OOTD>> {
            override fun onResponse(call: Call<ApiResponse<OOTD>>, response: Response<ApiResponse<OOTD>>) {
                if (response.isSuccessful) {
                    response.body()?.result?.let { ootd ->
                        displayOotdData(ootd) // 데이터를 화면에 표시
                    }
                } else {
                    Log.e("API_ERROR", "Failed to fetch data for OOTD ID: $ootdId")
                }
            }

            override fun onFailure(call: Call<ApiResponse<OOTD>>, t: Throwable) {
                Log.e("API_ERROR", "Request failed", t)
            }
        })
    }

    private fun displayOotdData(ootd: OOTD) {
        binding.date.text = formatDateKey(ootd.date)

//        Glide.with(this).load(ootd.imageUrl).into(binding.photoImageView)

        // OOTD 이미지 적용
        val imageUrl = ootd.imageUrl
        if (imageUrl != null) {
            binding.photoImageView.visibility = View.VISIBLE
            loadImage(binding.photoImageView, imageUrl.toString())
            Log.e("PIC", ootd.imageUrl)
        } else {
            binding.photoImageView.visibility = View.GONE
        }

        val textViewList = listOf(
            binding.outerText,
            binding.topText,
            binding.bottomText,
            binding.shoesText,
            binding.bagText,
            binding.otherText
        )

        if (ootd.clothList.isNotEmpty()) {
            for (i in ootd.clothList.indices) {
                if (i < textViewList.size) {
                    val cloth = ootd.clothList[i]
                    textViewList[i].text =
                        "${cloth.kindName} ${cloth.categoryName} ${cloth.fitName} ${cloth.colorName}"
                }
            }
        }

        for (i in ootd.clothList.size until textViewList.size) {
            textViewList[i].text = "옷에 대한 정보가 없습니다"
        }
    }

    private fun formatDateKey(dateString: String): String {
        val parts = dateString.split("-")
        return parts[1] + parts[2]
    }

    // Glide를 이용한 이미지 로드
    private fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

package com.umc.upstyle

import RequestAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.upstyle.data.model.RequestResponse
import com.umc.upstyle.data.network.ApiService
import com.umc.upstyle.data.network.RetrofitClient
import com.umc.upstyle.databinding.FragmentRequestBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// 코디요청 목록 보여주는 프래그먼트 !!!!
// RequestRepositoryd에서 데이터 로드함
class RequestFragment : Fragment() {
    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!
    private var listener: RequestFragmentListener? = null

    // ChatFragment에서 리스너 설정
    fun setRequestFragmentListener(listener: RequestFragmentListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 서버에서 코디 요청 목록 불러오기
        loadCodiRequestsFromServer()
    }

    private fun loadCodiRequestsFromServer() {
        val apiService = RetrofitClient.createService(ApiService::class.java)

        apiService.getCodiRequests().enqueue(object : Callback<RequestResponse> {
            override fun onResponse(call: Call<RequestResponse>, response: Response<RequestResponse>) {
                if (response.isSuccessful && response.body()?.isSuccess == true) {
                    val serverResponseList = response.body()?.result?.codiReqPreviewList ?: emptyList()

                    val requestList = serverResponseList.map {
                        Request(
                            id = it.id,
                            title = it.title,
                            commentCount = it.responseCount
                        )
                    }


                    val adapter = RequestAdapter(requestList) { request ->
                        val action = RequestFragmentDirections
                            .actionRequestFragmentToRequestDetailFragment(request.id, request.title, request.commentCount)
                        findNavController().navigate(action)
                    }

                    binding.recyclerView.adapter = adapter


                    binding.recyclerView.adapter = adapter
                } else {
                    Log.e("RequestFragment", "서버 응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<RequestResponse>, t: Throwable) {
                Log.e("RequestFragment", "네트워크 오류 발생: ${t.message}")
            }
        })
    }


    override fun onResume() {
        super.onResume()
        binding.recyclerView.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// 이벤트 전달을 위한 인터페이스
interface RequestFragmentListener {
    fun onRequestSelected(reqId: Int, reqTitle: String, commentCount: Int)
}
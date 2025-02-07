package com.umc.upstyle

import com.umc.upstyle.data.network.Item_load
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.umc.upstyle.data.viewmodel.RequestViewModel
import com.umc.upstyle.databinding.FragmentCreateRequestBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateRequestFragment : Fragment() {
    private var _binding: FragmentCreateRequestBinding? = null
    private val binding get() = _binding!!

    private var photoUri: Uri? = null // ✅ lateinit 제거 및 nullable 변경

    private lateinit var viewModel: RequestViewModel
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateRequestBinding.inflate(inflater, container, false)

        editTextTitle = binding.etTitle
        editTextContent = binding.etContent


        // ViewModel 가져오기
        viewModel = ViewModelProvider(requireActivity()).get(RequestViewModel::class.java)

        // ViewModel에 저장된 데이터가 있으면 복원
        editTextTitle.setText(viewModel.requestTitle)
        editTextContent.setText(viewModel.requestContent)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp() // 이전 Fragment로 이동
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.get<String>("SELECTED_ITEM")?.let { description ->
            findNavController().currentBackStackEntry?.savedStateHandle?.get<String>("SELECTED_ITEM_IMAGE_URL")
                ?.let { imageUrl ->
                    findNavController().currentBackStackEntry?.savedStateHandle?.get<String>("CATEGORY")
                        ?.let { category ->
                            // 이제 description과 imageUrl을 사용해서 필요한 작업을 처리
                            val item = Item_load(description, imageUrl) // 아이템 객체 생성
                            // 이미지 로드 처리

                            binding.imageContainer.visibility = View.VISIBLE
                            binding.imgSelected.visibility = View.VISIBLE
                            binding.btnRemoveImage.visibility = View.VISIBLE // x 버튼 표시
                            binding.btnImageUpload.visibility = View.INVISIBLE
                            saveImageUri(Uri.parse(item.imageUrl)) // ✅ photoUri가 null이 아닐 때 저장

                            Glide.with(requireContext())
                                .load(item.imageUrl)
                                .into(binding.imgSelected)
                        }
                }
        }

        // 사진 등록 버튼 이벤트
        binding.btnImageUpload.setOnClickListener { showPhotoOptions() }

        // 사진 삭제 버튼 이벤트
        binding.btnRemoveImage.setOnClickListener {
            // 사진 초기화
            binding.imgSelected.setImageURI(null)  // 이미지 초기화
            binding.imgSelected.visibility = View.GONE  // 이미지 숨기기
            binding.btnRemoveImage.visibility = View.GONE  // X 버튼 숨기기
            binding.imageContainer.visibility = View.INVISIBLE  // imageContainer 숨기기
            binding.btnImageUpload.visibility = View.VISIBLE  // 사진 업로드 버튼 보이기

            // 사진 URI도 초기화
            photoUri = null
            Toast.makeText(requireContext(), "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // ✅ TakePictureLauncher - photoUri가 null이 아니면만 처리
    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && photoUri != null) {
            binding.imageContainer.visibility = View.VISIBLE
            binding.imgSelected.visibility = View.VISIBLE
            binding.imgSelected.setImageURI(photoUri) // 촬영한 사진 표시
            binding.btnRemoveImage.visibility = View.VISIBLE // x 버튼 표시
            binding.btnImageUpload.visibility = View.INVISIBLE
            saveImageUri(photoUri!!) // ✅ photoUri가 null이 아닐 때 저장
            Toast.makeText(requireContext(), "사진 촬영 성공!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "사진 촬영 실패", Toast.LENGTH_SHORT).show()
        }
    }

    // ✅ 이미지 선택 시 photoUri를 사용하지 않고 직접 적용
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val savedPath = saveImageToInternalStorage(it)
            if (savedPath != null) {
                binding.imageContainer.visibility = View.VISIBLE
                binding.imgSelected.visibility = View.VISIBLE
                binding.imgSelected.setImageURI(it) // ✅ 직접 선택한 URI 사용
                binding.btnRemoveImage.visibility = View.VISIBLE // x 버튼 표시
                binding.btnImageUpload.visibility = View.INVISIBLE
                saveImagePath(savedPath)
                Toast.makeText(requireContext(), "사진 선택 완료!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "이미지 저장 실패", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(requireContext(), "사진 선택 취소", Toast.LENGTH_SHORT).show()
    }

    private fun showPhotoOptions() {
        val votePopup = VotePopupDialog(
            onTakePhoto = { takePhoto() },
            onChoosePhoto = { selectImageFromGallery() },
            onLoadItem = { findNavController().navigate(R.id.loadCategoryFragment) },
            onCancel = { /* 취소 버튼 동작 */ }
        )
        votePopup.show(parentFragmentManager, "VotePopupDialog")
    }

    private fun takePhoto() {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val photoFile = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)

            // ✅ photoUri를 null 체크 후 초기화
            photoUri = FileProvider.getUriForFile(
                requireContext(),
                "${requireContext().packageName}.fileprovider",
                photoFile
            )

            takePictureLauncher.launch(photoUri)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "사진 촬영 준비 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveImageUri(uri: Uri) {
        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)
        preferences.edit().putString("SAVED_IMAGE_PATH", uri.toString()).apply()
        Toast.makeText(requireContext(), "이미지 경로가 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun selectImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val fileName = "selected_image_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().filesDir, fileName)
            val outputStream = file.outputStream()

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveImagePath(path: String) {
        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)
        preferences.edit().putString("SAVED_IMAGE_PATH", path).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

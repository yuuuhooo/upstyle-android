package com.umc.upstyle

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.umc.upstyle.databinding.ActivityTodayOotdBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TodayOotdFragment : Fragment(R.layout.activity_today_ootd) {

    private var _binding: ActivityTodayOotdBinding? = null
    private val binding get() = _binding!!

    private var calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ActivityTodayOotdBinding.bind(view)

        // 날짜 표시
        val month = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH는 0부터 시작하므로 1을 더함
        val day = calendar.get(Calendar.DATE)

        binding.date.text = "${month.toString().padStart(2, '0')}${day.toString().padStart(2, '0')}"

        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

        // 이전 Fragment나 Activity에서 전달된 데이터 처리
        handleReceivedData(preferences)

        // 저장된 데이터 복원
        updateUIWithPreferences(preferences)

        // 카테고리 버튼 이벤트 설정
        setupCategoryButtons(preferences)

        // 사진 등록 버튼 이벤트
        binding.photoUploadFrame.setOnClickListener { showPhotoOptions() }

        // 저장 버튼 이벤트
        binding.saveButton.setOnClickListener { saveData(preferences) }

        // 뒤로가기 버튼 클릭 이벤트 설정
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
    }

    // 이전 Fragment나 Activity에서 전달된 데이터 처리
    private fun handleReceivedData(preferences: SharedPreferences) {
        val selectedCategory = arguments?.getString("CATEGORY")
        val selectedSubCategory = arguments?.getString("SUB_CATEGORY")
        val selectedFit = arguments?.getString("FIT")
        val selectedSize = arguments?.getString("SIZE")
        val selectedColor = arguments?.getString("COLOR")

        if (!selectedCategory.isNullOrEmpty()) {
            val categoryText = when (selectedCategory) {
                "OUTER", "TOP", "BOTTOM" -> "$selectedSubCategory $selectedFit $selectedColor"
                "SHOES", "OTHER" -> "$selectedSubCategory $selectedColor"
                "BAG" -> "$selectedSubCategory $selectedSize $selectedColor"
                else -> ""
            }

            // 데이터를 SharedPreferences에 저장
            preferences.edit {
                putString(selectedCategory, categoryText)
            }

            // UI 업데이트
            when (selectedCategory) {
                "OUTER" -> binding.outerText.text = categoryText
                "TOP" -> binding.topText.text = categoryText
                "BOTTOM" -> binding.bottomText.text = categoryText
                "SHOES" -> binding.shoesText.text = categoryText
                "BAG" -> binding.bagText.text = categoryText
                "OTHER" -> binding.otherText.text = categoryText
            }
        }
    }

    // 카테고리 버튼 초기화
    private fun setupCategoryButtons(preferences: SharedPreferences) {
        val buttons = listOf(
            binding.outerButton to "OUTER",
            binding.topButton to "TOP",
            binding.bottomButton to "BOTTOM",
            binding.shoesButton to "SHOES",
            binding.bagButton to "BAG",
            binding.otherButton to "OTHER"
        )

        buttons.forEach { (button, category) ->
            button.setOnClickListener { navigateToCategory(category) }
        }
    }

    // UI 업데이트 함수
    private fun updateUIWithPreferences(preferences: SharedPreferences) {
        binding.outerText.text = preferences.getString("OUTER", "없음")
        binding.topText.text = preferences.getString("TOP", "없음")
        binding.bottomText.text = preferences.getString("BOTTOM", "없음")
        binding.shoesText.text = preferences.getString("SHOES", "없음")
        binding.bagText.text = preferences.getString("BAG", "없음")
        binding.otherText.text = preferences.getString("OTHER", "없음")

        val savedPath = preferences.getString("SAVED_IMAGE_PATH", null)
        if (!savedPath.isNullOrEmpty()) {
            val file = File(savedPath)
            if (file.exists()) {

                // 이미지가 있을 경우 이미지를 표시하고 "사진등록" 글자 숨기기
                binding.photoImageView.visibility = View.VISIBLE
                binding.uploadText.visibility = View.GONE
                binding.photoImageView.setImageURI(Uri.fromFile(file))
            } else {
                // 이미지 파일이 없을 경우 "사진등록" 글자 표시
                binding.photoImageView.visibility = View.GONE
                binding.uploadText.visibility = View.VISIBLE
            }
        } else {
            // 저장된 경로가 없을 경우 "사진등록" 글자 표시
            binding.photoImageView.visibility = View.GONE
            binding.uploadText.visibility = View.VISIBLE
        }
    }


    // 데이터 저장 함수
    private fun saveData(preferences: SharedPreferences) {
        val editor = preferences.edit()

        editor.putString("OUTER", binding.outerText.text.toString().trim())
        editor.putString("TOP", binding.topText.text.toString().trim())
        editor.putString("BOTTOM", binding.bottomText.text.toString().trim())
        editor.putString("SHOES", binding.shoesText.text.toString().trim())
        editor.putString("BAG", binding.bagText.text.toString().trim())
        editor.putString("OTHER", binding.otherText.text.toString().trim())

        editor.apply()
        Toast.makeText(requireContext(), "카테고리가 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }

    // 카테고리 이동 함수
    private fun navigateToCategory(category: String) {
        val action = TodayOotdFragmentDirections.actionTodayOotdFragmentToCategoryFragment(category)
        findNavController().navigate(action)
    }

    // 사진 관련 코드 시작
    private lateinit var photoUri: Uri // 사진 촬영 URI

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.photoImageView.visibility = View.VISIBLE
            binding.photoImageView.setImageURI(photoUri) // 촬영한 사진 표시
            saveImageUri(photoUri) // URI 저장
            Toast.makeText(requireContext(), "사진 촬영 성공!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "사진 촬영 실패", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val savedPath = saveImageToInternalStorage(it)
            if (savedPath != null) {
                binding.photoImageView.visibility = View.VISIBLE
                binding.photoImageView.setImageURI(Uri.parse(savedPath))
                saveImagePath(savedPath)
                Toast.makeText(requireContext(), "사진 선택 완료!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "이미지 저장 실패", Toast.LENGTH_SHORT).show()
            }
        } ?: Toast.makeText(requireContext(), "사진 선택 취소", Toast.LENGTH_SHORT).show()
    }

    private fun showPhotoOptions() {
        val photoPopup = PhotoPopupDialog(
            onTakePhoto = { takePhoto() },
            onChoosePhoto = { selectImageFromGallery() },
            onCancel = { /* 취소 버튼 동작 */ }
        )
        photoPopup.show(parentFragmentManager, "PhotoPopupDialog")
    }

    private fun takePhoto() {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val photoFile = File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)

            // photoUri 초기화
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

    private fun saveImageUri(photoUri: Uri?) {
        if (photoUri != null) {
            val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)
            preferences.edit().putString("SAVED_IMAGE_PATH", photoUri.toString()).apply()
            Toast.makeText(requireContext(), "이미지 경로가 저장되었습니다.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "이미지 경로 저장 실패: URI가 null입니다.", Toast.LENGTH_SHORT).show()
        }
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


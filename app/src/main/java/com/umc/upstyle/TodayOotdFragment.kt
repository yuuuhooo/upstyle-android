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
import androidx.fragment.app.Fragment
import com.umc.upstyle.databinding.ActivityTodayOotdBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TodayOotdFragment : Fragment(R.layout.activity_today_ootd) {

    private var _binding: ActivityTodayOotdBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ActivityTodayOotdBinding.bind(view)

        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)

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
            if (parentFragmentManager.backStackEntryCount > 0) {
                parentFragmentManager.popBackStack() // 이전 프래그먼트로 이동
            } else {
                requireActivity().finish() // 더 이상 스택이 없으면 액티비티 종료
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
                binding.photoImageView.visibility = View.VISIBLE
                binding.photoImageView.setImageURI(Uri.fromFile(file))
            }
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
        val fragment = CategoryFragment().apply {
            arguments = Bundle().apply { putString("CATEGORY", category) }
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
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

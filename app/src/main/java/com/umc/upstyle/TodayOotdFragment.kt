package com.umc.upstyle

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.FirebaseStorage
import com.umc.upstyle.data.model.ClothRequestDTO
import com.umc.upstyle.data.model.OOTDRequest
import com.umc.upstyle.data.network.OOTDService
import com.umc.upstyle.data.viewmodel.ClothViewModel
import com.umc.upstyle.databinding.ActivityTodayOotdBinding
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class TodayOotdFragment : Fragment(R.layout.activity_today_ootd) {

    private var _binding: ActivityTodayOotdBinding? = null
    private val binding get() = _binding!!

    // ViewModel을 프래그먼트 간 공유하기 위해 activityViewModels 사용
    private val clothViewModel: ClothViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ActivityTodayOotdBinding.bind(view)

        val preferences = requireActivity().getSharedPreferences("AppData", Context.MODE_PRIVATE)


        // 이전 Fragment나 Activity에서 전달된 데이터 처리
        handleReceivedData()

        // 카테고리 UI 업데이트
        clothViewModel.categoryData.observe(viewLifecycleOwner) { categoryData ->
            binding.outerText.text = categoryData["OUTER"]
            binding.topText.text = categoryData["TOP"]
            binding.bottomText.text = categoryData["BOTTOM"]
            binding.shoesText.text = categoryData["SHOES"]
            binding.bagText.text = categoryData["BAG"]
            binding.otherText.text = categoryData["OTHER"]
        }

        // 날짜
        val dateFormat = SimpleDateFormat("MMdd", Locale.getDefault())
        val todayDate = dateFormat.format(Date())

        binding.date.text = todayDate


        // 저장된 데이터 복원
        updateUIWithPreferences(preferences)

        // 카테고리 버튼 이벤트 설정
        setupCategoryButtons()

        // 사진 등록 버튼 이벤트
        binding.photoUploadFrame.setOnClickListener { showPhotoOptions() }

        // 저장 버튼 이벤트
        binding.saveButton.setOnClickListener {
            val dateServer = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val ootdRequest = OOTDRequest(
                userId = 1,
                date = dateServer,
                ootdImages = "",
                clothViewModel.clothList.value ?: emptyList()
            )

            val imageUri = Uri.parse(clothViewModel.categoryData.value?.get("SAVED_IMAGE_PATH"))
            if (imageUri != null) {
                uploadOOTD(ootdRequest, imageUri)
            } else {
                Toast.makeText(requireContext(), "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 뒤로가기 버튼 클릭 이벤트 설정
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private fun uploadOOTD(request: OOTDRequest, imageUri: Uri?) {
        lifecycleScope.launch {
            try {
                // Firebase에 이미지를 업로드하고 URL을 받기 위한 비동기 작업
                val uploadedImageUrls = mutableListOf<String>()

                if (imageUri != null) {
                    uploadImageToFirebase(imageUri) { imageUrl ->
                        if (imageUrl != null) {
                            uploadedImageUrls.add(imageUrl)
                            Log.d("Firebase", "이미지 업로드 성공: $imageUrl")
                        } else {
                            Log.e("Firebase", "이미지 업로드 실패")
                        }
                    }
                }

                // Firebase 업로드가 성공적으로 완료되면, 업로드된 이미지 URL을 OOTDRequest에 추가하여 서버로 전송
                if (uploadedImageUrls.isNotEmpty()) {
                    val updatedRequest = request.copy(ootdImages = uploadedImageUrls.first()) // 첫 번째 이미지만 사용
                    sendToServer(updatedRequest)
                } else {
                    Log.e("Upload", "이미지 업로드에 실패했습니다.")
                }

            } catch (e: Exception) {
                Log.e("Retrofit", "에러 발생: ${e.message}")
            }
        }
    }

    // Firebase에 이미지를 업로드하는 함수
    private fun uploadImageToFirebase(uri: Uri, callback: (String?) -> Unit) {
        try {
            val storageRef = FirebaseStorage.getInstance().reference
            val fileName = "images/${UUID.randomUUID()}.jpg" // 랜덤 파일명 생성
            val imageRef = storageRef.child(fileName)

            val uploadTask = imageRef.putFile(uri)

            uploadTask.addOnSuccessListener {
                // 업로드 성공 후 이미지 URL 가져오기
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    callback(downloadUri.toString()) // URL 반환
                }.addOnFailureListener {
                    callback(null)
                }
            }.addOnFailureListener {
                callback(null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }


    // 서버로 OOTDRequest를 전송하는 함수
    private fun sendToServer(updatedRequest: OOTDRequest) {
        val ootdService = RetrofitClient.createService(OOTDService::class.java)
        lifecycleScope.launch {
            try {
                val response = ootdService.uploadOOTD(updatedRequest)
                if (response.isSuccessful) {
                    Log.d("Retrofit", "업로드 성공!")
                } else {
                    Log.e("Retrofit", "업로드 실패: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("Retrofit", "서버 요청 에러: ${e.message}")
            }
        }
    }



    // 이전 Fragment나 Activity에서 전달된 데이터 처리
    private fun handleReceivedData() {
        val selectedCategory = arguments?.getString("CATEGORY")
        val selectedSubCategory = arguments?.getString("SUB_CATEGORY")
        val selectedFit = arguments?.getString("FIT")
        val selectedSize = arguments?.getString("SIZE")
        val selectedColor = arguments?.getString("COLOR")
        val selectedEtc = arguments?.getString("ETC")


        val clothId = arguments?.getInt("CLOTH_ID") ?:0
        val kindId = arguments?.getInt("KIND_ID") ?:0
        val categoryId = arguments?.getInt("CATEGORY_ID") ?:0
        val fitId = arguments?.getInt("FIT_ID") ?:0
        val colorId = arguments?.getInt("COLOR_ID") ?:0
        val addInfo = arguments?.getString("ADD_INFO") ?:""


        // 새로 받아온 아이템 정보가 있다면
        if(kindId != 0) {
            // DTO 생성
            val clothRequestDTO = ClothRequestDTO(
                clothId = clothId,
                clothKindId = kindId,
                clothCategoryId = categoryId,
                fitCategoryId = fitId,
                colorCategoryId = colorId,
                additionalInfo = addInfo
            )

            clothViewModel.addClothRequest(clothRequestDTO)
        }

        if (!selectedCategory.isNullOrEmpty()) {
            val categoryText = when (selectedCategory) {
                "OUTER", "TOP", "BOTTOM" -> "$selectedSubCategory $selectedFit $selectedColor $selectedEtc"
                "SHOES", "OTHER" -> "$selectedSubCategory $selectedColor $selectedEtc"
                "BAG" -> "$selectedSubCategory $selectedSize $selectedColor $selectedEtc"
                else -> ""
            }


            // UI 업데이트 되도록
            clothViewModel.updateCategory(selectedCategory, categoryText)
        }
    }

    private fun setupCategoryButtons() {
        val buttons = listOf(
            binding.outerButton to "OUTER",
            binding.topButton to "TOP",
            binding.bottomButton to "BOTTOM",
            binding.shoesButton to "SHOES",
            binding.bagButton to "BAG",
            binding.otherButton to "OTHER"
        )

        buttons.forEach { (button, category) ->
            button.setOnClickListener {
                showLoadItemPopupDialog(category)
            }
        }
    }

    private fun showLoadItemPopupDialog(category: String) {
        val dialog = LoadItemPopupDialog(
            requireContext(),
            onCreateNewClicked = {
                // "새로 생성" 클릭 시 카테고리 생성 화면으로 이동
                navigateToCategoryFragment(category)
            },
            onLoadPreviousClicked = {
                // "불러오기" 클릭 시 LoadItemFragment로 이동
                navigateToLoadItemFragment(category)
            }
        )
        dialog.show()
    }


    //categoryFragment로 이동
    private fun navigateToCategoryFragment(category: String) {

        val bundle = Bundle().apply {
            putString("CATEGORY", category)
        }
        Log.d("TodayOotdFragment", "Navigating to CategoryFragment with category: $category") // 디버깅 로그 추가

        findNavController().navigate(R.id.categoryFragment, bundle)

//        val action = TodayOotdFragmentDirections.actionTodayOotdFragmentToCategoryFragment(category)
//        findNavController().navigate(action)

//        // Safe Args로 전달된 값을 Fragment에서도 arguments로 다시 설정
//        val fragment = CategoryFragment().apply {
//            arguments = Bundle().apply { putString("CATEGORY", category) }
//        }

    }

    // LoadItemFragment로 이동
    private fun navigateToLoadItemFragment(category: String) {
        val action = TodayOotdFragmentDirections.actionTodayOotdFragmentToLoadItemFragment(category)
        findNavController().navigate(action)
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
                binding.uploadText.visibility = View.GONE // "사진 등록" 텍스트 숨김
                binding.photoImageView.setImageURI(Uri.fromFile(file))
                binding.uploadText.visibility = View.GONE // "사진 등록" 텍스트 숨김
            }
        }
    }

    //사진+카테고리 중 1 선택시 저장버튼 활성화
    private fun updateSaveButtonVisibility(preferences: SharedPreferences) {
        val savedPath = preferences.getString("SAVED_IMAGE_PATH", null)
        val categories = listOf(
            preferences.getString("OUTER", "없음"),
            preferences.getString("TOP", "없음"),
            preferences.getString("BOTTOM", "없음"),
            preferences.getString("SHOES", "없음"),
            preferences.getString("BAG", "없음"),
            preferences.getString("OTHER", "없음")
        )

        // 사진이 등록되었거나 카테고리 중 하나 이상이 입력되었는지 확인
        val isSaveEnabled =
            (!savedPath.isNullOrEmpty() && File(savedPath).exists()) || categories.any { it != "없음" }

        binding.saveButton.visibility = if (isSaveEnabled) View.VISIBLE else View.GONE
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


//    // 카테고리 이동 함수
//    private fun navigateToCategory(category: String) {
//        val fragment = CategoryFragment().apply {
//            arguments = Bundle().apply { putString("CATEGORY", category) }
//        }
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null)
//            .commit()
//    }
//    private fun navigateToClosetItemFragment(category: String) {
//        val action =
//            TodayOotdFragmentDirections.actionTodayOotdFragmentToClosetItemFragment(category)
//        findNavController().navigate(action)
//
//    }


    // 사진 관련 코드 시작
    private lateinit var photoUri: Uri // 사진 촬영 URI

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            binding.photoImageView.visibility = View.VISIBLE
            binding.photoImageView.setImageURI(photoUri) // 촬영한 사진 표시
            binding.uploadText.visibility = View.GONE // "사진 등록" 텍스트 숨김
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
                binding.uploadText.visibility = View.GONE // "사진 등록" 텍스트 숨김
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
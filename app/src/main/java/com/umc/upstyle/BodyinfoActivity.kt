package com.umc.upstyle

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.upstyle.databinding.ActivityBodyinfoBinding

class BodyinfoActivity : AppCompatActivity() {

    // ViewBinding 객체 선언
    private lateinit var binding: ActivityBodyinfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityBodyinfoBinding.inflate(layoutInflater)

        // setContentView()로 ViewBinding의 root 레이아웃을 설정
        setContentView(binding.root)



        // 성별 버튼 클릭 시 상태 변경
        binding.btnBodyinfoMale.setOnClickListener {
            binding.btnBodyinfoMale.isSelected = true
            binding.btnBodyinfoFemale.isSelected = false
        }

        binding.btnBodyinfoFemale.setOnClickListener {
            binding.btnBodyinfoFemale.isSelected = true
            binding.btnBodyinfoMale.isSelected = false
        }


        //시작하기 버튼 이벤트 처리
        binding.btBodyinfoStart.setOnClickListener {
            // 버튼 클릭 이벤트 처리
            // Intent 생성
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) // TestActivity로 이동
        }

    }
}

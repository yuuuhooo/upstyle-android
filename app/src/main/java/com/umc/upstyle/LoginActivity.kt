package com.umc.upstyle

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.umc.upstyle.databinding.ActivityBodyinfoBinding
import com.umc.upstyle.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // ViewBinding 초기화
        binding = ActivityLoginBinding.inflate(layoutInflater)

        // setContentView()로 ViewBinding의 root 레이아웃을 설정
        setContentView(binding.root)


        binding.tvNonMember.setOnClickListener {
            // 버튼 클릭 이벤트 처리
            // Intent 생성
            val intent = Intent(this, BodyinfoActivity::class.java)
            startActivity(intent) // 현재는 TestActivity로 이동. 실제로는 마이홈으로 가야 됨.
        }
    }
}
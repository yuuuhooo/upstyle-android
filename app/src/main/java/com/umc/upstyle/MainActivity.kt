package com.umc.upstyle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.databinding.DataBindingUtil
import com.umc.upstyle.databinding.ActivityBodyinfoBinding
import com.umc.upstyle.databinding.ActivityMainBinding
import com.umc.upstyle.ui.theme.UPSTYLETheme

class MainActivity : AppCompatActivity() {

    // ViewBinding 객체 선언
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityMainBinding.inflate(layoutInflater)

        // setContentView()로 ViewBinding root 레이아웃을 설정
        setContentView(binding.root)

    }
}
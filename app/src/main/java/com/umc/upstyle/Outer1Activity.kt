package com.umc.upstyle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class Outer1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outer_1)  // XML 레이아웃 파일 이름
    }
}

package com.umc.upstyle

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Kakao Sdk 초기화
        KakaoSdk.init(this, "7b558a1bbfd23f2f42f6694d0575c8e3")
    }
}
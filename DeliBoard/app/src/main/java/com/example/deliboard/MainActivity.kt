package com.example.deliboard

import FCM.MessagingService
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hideSystemUI()

        if (!isLogined()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            setContentView(R.layout.activity_main)
        }


        // 켤때 FCM 토큰 가져오는거
        MessagingService().getFirebaseToken()

        val rightTextView = findViewById<TextView>(R.id.rightTextView)
//        val backButton = findViewById<ImageView>(R.id.backButton)

        //내비 감지 -> 메인프래그먼트가 아니면 뒤로가기 버튼 보이게
//        val navController = findNavController(R.id.fragmentContainerView)
//        navController.addOnDestinationChangedListener{_, destnation, _ ->
//            val currentFragmentId = destnation.id
//
//            if (currentFragmentId == androidx.constraintlayout.widget.R.id.home) {
//                backButton.visibility = View.GONE
//                rightTextView.visibility = View.VISIBLE
//            } else {
//                rightTextView.visibility = View.GONE
//                backButton.visibility = View.VISIBLE
//                showBackButton()
//            }
//        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            // Immersive Mode 재활성화 (사용자가 화면에 다시 포커스를 줄 때)
            hideSystemUI()
        }
    }

    private fun isLogined() : Boolean {
        val sharedPreferences = getSharedPreferences("StoreInfo", Context.MODE_PRIVATE)
        return sharedPreferences.contains("storeId") && sharedPreferences.contains("roomNum")
    }

    private fun showBackButton() {

    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11 이상에서는 아래 코드로 Immersive Mode를 설정할 수 있습니다.
            window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        } else {
            // Android 11 미만 버전에서는 아래 코드로 Immersive Mode를 설정할 수 있습니다.
            window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
        }
    }
}
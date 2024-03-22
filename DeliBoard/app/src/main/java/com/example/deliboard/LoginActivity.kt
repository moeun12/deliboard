package com.example.deliboard

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton = findViewById<ConstraintLayout>(R.id.loginButton)
        val storeNumInput = findViewById<TextInputEditText>(R.id.storeNumField)
        val roomNumInput = findViewById<TextInputEditText>(R.id.roomNumField)

        loginButton.setOnClickListener {
            val storeId = storeNumInput.text.toString()
            val roomNum = roomNumInput.text.toString()

            StoreNumCheck(storeId, roomNum)
        }
    }

    private fun StoreNumCheck(storeId: String, roomNum: String) {
        val client = BaseApi.getInstance().create(LoginApi::class.java)

        val requestBody = mapOf(
            "storeId" to storeId,
            "number" to roomNum
        )

        client.sendStoreInfo(requestBody).enqueue(object : Callback<Void> {
            override fun onResponse(call : Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("Login", "success")
                    saveStoreInfo(storeId, roomNum)
                } else {
                    Log.d("Login", "failed")
                    showFailure(this@LoginActivity)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Login", "$t")
                showFailure(this@LoginActivity)
            }
        })
    }

    private fun saveStoreInfo(storeId: String, roomNum: String) {
        val sharedPreferences = getSharedPreferences("StoreInfo", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("storeId", storeId)
        editor.putString("roomNum", roomNum)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showFailure(context: Context) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("인증 실패")
        builder.setMessage("매장 번호, 테이블번호가 유효하지 않습니다.")

        builder.setPositiveButton("확인") {dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }
}
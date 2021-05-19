package com.example.databook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class cadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
    }

    fun InicarLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun InicarHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
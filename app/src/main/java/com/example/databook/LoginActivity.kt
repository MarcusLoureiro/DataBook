package com.example.databook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cadastro.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tv_cadastre_se.setOnClickListener {
            InicarCadastro()
        }
    }

    fun InicarCadastro(){
        val intent = Intent(this, cadastroActivity::class.java)
        startActivity(intent)
        finish()
    }
}
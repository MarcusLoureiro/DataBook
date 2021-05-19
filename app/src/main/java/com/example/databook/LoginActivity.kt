package com.example.databook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_cadastro.tv_cadastre_se
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tv_cadastre_se.setOnClickListener {
            InicarCadastro()
        }
        btn_login.setOnClickListener {
            InicarHome()
        }
    }

    fun InicarCadastro(){
        val intent = Intent(this, cadastroActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun InicarHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
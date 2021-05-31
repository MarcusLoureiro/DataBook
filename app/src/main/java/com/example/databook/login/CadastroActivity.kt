package com.example.databook.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.databook.R
import com.example.databook.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_cadastro.arrow_back

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        arrow_back.setOnClickListener {
            inicarHome()
            finish()
        }

        tv_login.setOnClickListener {
            inicarLogin()
        }

        btn_cadastrar.setOnClickListener {
            if (testarCampos()) {
                val email = edEmailCadastro.text.toString()
                val password = edSenhaCadastro.text.toString()
                if (password == edSenhaConfirmeCadastro.text.toString() && email == edEmailCadastro.text.toString()) {
                    registerFirebase(email, password)
                } else {
                    showMsg("Algo deu errado. Tente novamente.")
                }
            }
        }
    }
    private fun registerFirebase(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showMsg("usuário registrado com sucesso")
                    inicarHome()
                } else {
                    showMsg(task.exception?.message.toString())
                }
            }
    }

    private fun inicarLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun inicarHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    private fun testarCampos(): Boolean {
        var teste = true
        if (edEmailCadastro.text.isNullOrBlank()) {
            teste = false
            showMsg("Email inválido")
        }
        if (edSenhaCadastro.text.isNullOrBlank()) {
            teste = false
            showMsg("Senha inválida")
        }
        if (edConfirmeEmailCadastro.text.isNullOrBlank()) {
            teste = false
            showMsg("Email não confere")
        }
        if (edSenhaConfirmeCadastro.text.isNullOrBlank()) {
            teste = false
            showMsg("Senha não confere")
        }
        return teste
    }

}




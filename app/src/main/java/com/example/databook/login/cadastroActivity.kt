package com.example.databook.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.databook.R
import com.example.databook.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import kotlinx.android.synthetic.main.activity_cadastro.*
import kotlinx.android.synthetic.main.activity_cadastro.arrow_back
import kotlinx.android.synthetic.main.activity_login.*

class cadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        arrow_back.setOnClickListener {
            InicarHome()
            finish()
        }

        tv_login.setOnClickListener {
            InicarLogin()
        }

        btn_cadastrar.setOnClickListener {
            if (testarCampos()) {
                var email = edEmailCadastro.text.toString()
                var password = edSenhaCadastro.text.toString()
                if (password == edSenhaConfirmeCadastro.text.toString() && email == edEmailCadastro.text.toString()) {
                    registerFirebase(email, password)
                } else {
                    showMsg("Algo deu errado. Tente novamente.")
                }
            }
        }
    }
    fun registerFirebase(email: String, password: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result?.user!!
                    showMsg("usuário registrado com sucesso")
                    var idUser = firebaseUser.uid
                    var email = firebaseUser.email
                    InicarHome()
                } else {
                    showMsg(task.exception?.message.toString())
                }
            }
    }

    fun InicarLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun InicarHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun showMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun testarCampos(): Boolean {
        var teste = true
        if (edEmailCadastro.text.isNullOrBlank() == true) {
            teste = false
            showMsg("Email inválido")
        }
        if (edSenhaCadastro.text.isNullOrBlank() == true) {
            teste = false
            showMsg("Senha inválida")
        }
        if (edConfirmeEmailCadastro.text.isNullOrBlank() == true) {
            teste = false
            showMsg("Email não confere")
        }
        if (edSenhaConfirmeCadastro.text.isNullOrBlank() == true) {
            teste = false
            showMsg("Senha não confere")
        }
        return teste
    }

}




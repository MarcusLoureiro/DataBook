package com.example.databook.login

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.databook.R
import com.example.databook.database.perfil.PerfilEntity
import com.example.databook.database.perfil.PerfisViewModel
import com.example.databook.home.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_cadastro.tv_cadastre_se
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var viewModelPerfil: PerfisViewModel


    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelPerfil = ViewModelProvider(this).get(PerfisViewModel::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        if (user != null) {
            println("id idfer" + user.providerData[1].providerId)
        }
        updateUI(user)
        arrow_back.setOnClickListener {
            finish()
        }
        btn_login.setOnClickListener {
            signIn()
        }
        tv_cadastre_se.setOnClickListener {
            inicarCadastro()
        }
    }


    private fun inicarCadastro() {
        val intent = Intent(this, CadastroActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun inicarHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun testarCampos(): Boolean {
        var teste = true
        if (edEmail.text.isNullOrBlank()) {
            teste = false
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
        }
        if (edSenha.text.isNullOrBlank()) {
            teste = false
            Toast.makeText(this, "Senha inválida", Toast.LENGTH_SHORT).show()
        }
        return teste
    }


    private fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            val icon = BitmapFactory.decodeResource(resources, R.drawable.avatar)
            viewModelPerfil.addPerfil(PerfilEntity(
                account.uid,
                icon,
                "",
                account.email.toString(),
                0,
                0,
                ""))
            Toast.makeText(this, "olá, ${account.email}", Toast.LENGTH_LONG).show()
            inicarHome()
        } else {
            Toast.makeText(this, "U Didnt signed in", Toast.LENGTH_LONG).show()
        }
    }


    private fun signIn() {
        if (testarCampos()) {
            val email = edEmail.text.toString()
            val password = edSenha.text.toString()
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = mAuth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }

                }
        }
    }
}



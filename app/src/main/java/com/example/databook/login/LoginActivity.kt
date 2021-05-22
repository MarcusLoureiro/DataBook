package com.example.databook.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.databook.R
import com.example.databook.entities.Constants
import com.example.databook.home.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_cadastro.tv_cadastre_se
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 100
    }

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
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
            InicarCadastro()
        }
        tv_cadastre_se.setOnClickListener {
            InicarCadastro()
        }
    }


    fun InicarCadastro() {
        val intent = Intent(this, cadastroActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun InicarHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun testarCampos(): Boolean {
        var teste = true
        if (edEmail.text.isNullOrBlank() == true) {
            teste = false
            Toast.makeText(this, "Email inválido", Toast.LENGTH_SHORT).show()
        }
        if (edSenha.text.isNullOrBlank() == true) {
            teste = false
            Toast.makeText(this, "Senha inválida", Toast.LENGTH_SHORT).show()
        }
        return teste
    }


    fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "olá, ${account.email}", Toast.LENGTH_LONG).show()
            InicarHome()
        } else {
            Toast.makeText(this, "U Didnt signed in", Toast.LENGTH_LONG).show()
        }
    }


    fun signIn() {
        if (testarCampos() == true) {
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



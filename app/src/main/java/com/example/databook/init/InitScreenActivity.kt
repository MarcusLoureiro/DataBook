package com.example.databook.init

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.databook.R
import com.example.databook.database.perfil.PerfisViewModel
import com.example.databook.entities.Constants
import com.example.databook.home.MainActivity
import com.example.databook.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_init_screen.*

@Suppress("DEPRECATION", "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class InitScreenActivity : AppCompatActivity() {
    private lateinit var viewModelPerfil: PerfisViewModel

    companion object {
        const val RC_SIGN_IN = 100
    }

    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_screen)
        viewModelPerfil = ViewModelProvider(this).get(PerfisViewModel::class.java)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("389033630908-10ehndc75t0s3o2ftvpmkq0baccaep14.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
        mAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = mAuth.currentUser
        if (user != null) {
            println("id idfer" + user.providerData[1].providerId)
        }
        updateUI(user)
        btn_login_google.setOnClickListener {
            if (user == null) {
                signinFirebaseGoogle()
            }
        }
        btn_entrar.setOnClickListener {
            inicarLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
            try {
                val account = task.getResult(ApiException::class.java)
                println("firebaseAuthWithGoogle:" + account.id)
                println("alo")
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                Toast.makeText(this, "Login falhou.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun inicarHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Constants.loginGoogle = true
                    // Sign in success, update UI with the signed-in user's information
                    println("signInWithCredential:success")
                    val user = mAuth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    println("signInWithCredential:failure" + task.exception)
                    updateUI(null)
                }

            }
    }

    private fun updateUI(account: FirebaseUser?) {
        if (account != null) {
            Toast.makeText(this, "Olá, ${account.displayName}", Toast.LENGTH_LONG).show()
            inicarHome()
        } else {
            Toast.makeText(this, "Faça Login.", Toast.LENGTH_LONG).show()
        }
    }


    private fun signinFirebaseGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account.id ?: ""
            Log.i("Google ID", googleId)

            val googleFirstName = account.givenName ?: ""
            Log.i("Google First Name", googleFirstName)
            Constants.nomeGoogle = googleFirstName

            val googleLastName = account.familyName ?: ""
            Log.i("Google Last Name", googleLastName)

            val googleEmail = account.email ?: ""
            Log.i("Google Email", googleEmail)
            Constants.emailGoogle = googleEmail

            val googleProfilePicURL = account.photoUrl.toString()
            Log.i("Google Profile Pic URL", googleProfilePicURL)

            val googleIdToken = account.idToken ?: ""
            Log.i("Google ID Token", googleIdToken)

        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

    private fun inicarLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}




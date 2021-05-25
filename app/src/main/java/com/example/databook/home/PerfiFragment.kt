package com.example.databook.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.databook.R
import com.example.databook.init.SplashScreenActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_livro_selecionado.*
import kotlinx.android.synthetic.main.fragment_perfil.view.*

class PerfiFragment:Fragment(){

    var Perfil: Boolean? = null
    val mAuth = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            Perfil = arguments?.getBoolean(perfil)
        }
    }

    companion object {
        private val perfil = "perfil"
        fun newInstance(Perfil: Boolean): PerfiFragment {
            val fragment = PerfiFragment()
            val args = Bundle()
            args.putBoolean(perfil, Perfil)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_perfil, container, false)
        Picasso.get().load(mAuth?.photoUrl).into(view.iv_perfil)
        view.tv_perfil_name.setText(mAuth?.displayName)
        view.iv_exit_app.setOnClickListener {
            signOut()
            InicarSplash()
        }
        return view
    }

    fun InicarSplash() {
        val intent = Intent(activity, SplashScreenActivity::class.java)
        startActivity(intent)
        activity?.finish()
        Toast.makeText(activity, "Usuário Desconectado", Toast.LENGTH_SHORT).show()
    }

    fun signOut(){
        // Sai do Firebase
        FirebaseAuth.getInstance().signOut()
        // Sai do google
        var googleSignInClient: GoogleSignInClient
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("389033630908-10ehndc75t0s3o2ftvpmkq0baccaep14.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        googleSignInClient.signOut()
    }

}



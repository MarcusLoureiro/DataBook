package com.example.databook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class HomeFragment:Fragment(){
    var Favoritos: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            Favoritos = arguments?.getBoolean(favoritos)
        }


    }

    companion object {
        private val favoritos = "favoritos"

        fun newInstance(Favoritos: Boolean): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putBoolean(favoritos, Favoritos)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_favoritos, container, false)
    }
}



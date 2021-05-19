package com.example.databook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class PerfiFragment:Fragment(){

    var Perfil: Boolean? = null

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }
}



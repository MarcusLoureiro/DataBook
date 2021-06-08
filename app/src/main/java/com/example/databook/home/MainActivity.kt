package com.example.databook.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.example.databook.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTabs()
    }
    private fun setTabs() {
        val adapter = ViewPagerHomeAdapter(supportFragmentManager)
        val homePerfil = PerfiFragment.newInstance(true)
        val homeFavoritos = HomeFragment.newInstance(true)
        val homeHome = HomeFragment.newInstance(false)
        adapter.addFragment(homeHome, "Busca")
        adapter.addFragment(homeFavoritos, "Favoritos")
        adapter.addFragment(homePerfil, "Perfil")
        viewPager_HomePage.adapter = adapter
        tabLayout_HomePage.setupWithViewPager(viewPager_HomePage)
        //Definição dos ícones de cada tab
        tabLayout_HomePage.getTabAt(0)!!.setIcon(R.drawable.ic_lupa)
        tabLayout_HomePage.getTabAt(1)!!.setIcon(R.drawable.ic_favorito_select)
        tabLayout_HomePage.getTabAt(2)!!.setIcon(R.drawable.ic_baseline_person_24)
        // Seta o item principal
        viewPager_HomePage.currentItem = 1
        
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
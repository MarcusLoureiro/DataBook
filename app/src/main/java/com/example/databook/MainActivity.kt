package com.example.databook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun setTabs() {

        val adapter = ViewPagerHomeAdapter(supportFragmentManager)
        val homeMediaFilme = HomeMediaFragment.newInstance(true)
        val homeMediaSerie = HomeMediaFragment.newInstance(false)
        adapter.addFragment(homeMediaSerie, "Séries")
        adapter.addFragment(HomeFragment(), "Home")
        adapter.addFragment(homeMediaFilme, "Filmes")

        viewPager_HomePage.adapter = adapter
        tabLayout_HomePage.setupWithViewPager(viewPager_HomePage)


        //Definição dos ícones de cada tab
        tabLayout_HomePage.getTabAt(0)!!.setIcon(R.drawable.ic_series_roxo)
        tabLayout_HomePage.getTabAt(1)!!.setIcon(R.drawable.ic_home_roxo)
        tabLayout_HomePage.getTabAt(2)!!.setIcon(R.drawable.ic_claquete_flaticon)
        viewPager_HomePage.setCurrentItem(1)
    }
}
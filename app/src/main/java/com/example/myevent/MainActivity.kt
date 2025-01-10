package com.example.myevent

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.nav_upcoming -> {
                    loadFragment(FragmentUpcoming())
                    true
                }
                R.id.nav_finished -> {
                    loadFragment(FragmentFinished())
                    true
                }
                R.id.nav_favorite -> {
                    loadFragment(FragmentFavorite())
                    true
                }
                R.id.nav_settings -> {
                    loadFragment(SettingFragment())
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.nav_home
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

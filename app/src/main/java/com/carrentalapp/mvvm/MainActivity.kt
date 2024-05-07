package com.carrentalapp.mvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.carrentalapp.mvvm.databinding.ActivityMainBinding
import com.carrentalapp.mvvm.ui.favourite.FavouriteFragment
import com.carrentalapp.mvvm.ui.home.HomeFragment
import com.carrentalapp.mvvm.ui.notification.NotificationFragment
import com.carrentalapp.mvvm.ui.person.PersonFragment


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> replaceFragment(HomeFragment())
                R.id.favoriteFragment -> replaceFragment(FavouriteFragment())
                R.id.notificationFragment -> replaceFragment(NotificationFragment())
                R.id.personFragment -> replaceFragment(PersonFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view_tag, fragment)
            .commit()
    }
}
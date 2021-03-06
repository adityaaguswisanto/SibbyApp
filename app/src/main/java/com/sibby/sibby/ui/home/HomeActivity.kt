package com.sibby.sibby.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sibby.sibby.R
import com.sibby.sibby.data.helper.gone
import com.sibby.sibby.data.helper.launchActivity
import com.sibby.sibby.data.helper.visible
import com.sibby.sibby.data.store.UserStore
import com.sibby.sibby.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    @Inject
    lateinit var userStore: UserStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navigationHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navigationHost.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> bottomNavigationView.visible()
                R.id.newsFragment -> bottomNavigationView.visible()
                R.id.babiesFragment -> bottomNavigationView.visible()
                R.id.settingsFragment -> bottomNavigationView.visible()
                else -> bottomNavigationView.gone()
            }
        }

        bottomNavigationView.setupWithNavController(navController)

    }

    fun performLogout() = lifecycleScope.launch {
        userStore.clear()
        launchActivity(AuthActivity::class.java)
    }
}
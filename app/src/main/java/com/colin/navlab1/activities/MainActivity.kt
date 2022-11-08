package com.colin.navlab1.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider


import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.colin.navlab1.R
import com.colin.navlab1.databinding.ActivityMainBinding
import com.colin.navlab1.mvvm.NewsViewModel
import com.colin.navlab1.mvvm.NewsViewModelFactory
import com.colin.navlab1.repositories.NewsRepository
import com.colin.navlab1.services.NewsService
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    lateinit var userName: String

    private lateinit var intentService: Intent

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var newsViewModelFactory: NewsViewModelFactory
    private lateinit var viewModel: NewsViewModel

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ext_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_logout) {
            stopService(intentService)
            Firebase.auth.signOut()
            val int = Intent(this, MainActivity2::class.java)
            startActivity(int)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userName = intent.extras?.get("name").toString()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.appBarMain.toolbar)

        navController = Navigation.findNavController(this, R.id.nav_host)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home,
            R.id.navigation_fav,
            R.id.navigation_settings
        ), binding.drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.drawerMenuText).text = userName
        binding.appBarMain.activityMain.bottomNav.setupWithNavController(navController)
        binding.navView.setupWithNavController(navController)
        newsViewModelFactory = NewsViewModelFactory(NewsRepository, this)
        viewModel = ViewModelProvider(this, newsViewModelFactory)[NewsViewModel::class.java]
        intentService = Intent(this, NewsService::class.java)
        ContextCompat.startForegroundService(this, intentService)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
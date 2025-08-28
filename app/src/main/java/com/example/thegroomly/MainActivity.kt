package com.example.thegroomly

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.thegroomly.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Toolbar
        setSupportActionBar(binding.appBarMain.toolbar)

        // NavController
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // U AppBarConfiguration ubaci sve top-level destinacije (home, booking, settings, profile)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_booking,
                R.id.nav_settings,
                R.id.nav_profile    // <-- Moj profil
            ),
            binding.drawerLayout
        )

        // Poveži ActionBar s Navigacijom
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Standardno hookanje side-drawer-a
        binding.navView.setupWithNavController(navController)

        // Dodaj custom listener za Odjavu, ostale stavke prepusti NavigationUI-ju
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    // Ako je kliknuto na “Odjava”
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()  // ne dozvoli povratak pritiskom Back
                    true
                }
                else -> {
                    // Svi ostali id-evi (home, booking, settings, profile)
                    androidx.navigation.ui.NavigationUI
                        .onNavDestinationSelected(menuItem, navController)
                    binding.drawerLayout.closeDrawers()
                    true
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

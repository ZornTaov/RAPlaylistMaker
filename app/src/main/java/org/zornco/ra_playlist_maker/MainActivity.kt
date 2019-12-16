package org.zornco.ra_playlist_maker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import org.zornco.ra_playlist_maker.common.DataHolder
import org.zornco.ra_playlist_maker.common.IOnBackPressed
import org.zornco.ra_playlist_maker.common.OnItemClickListener
import org.zornco.ra_playlist_maker.common.SettingsActivity
import org.zornco.ra_playlist_maker.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity(),
    OnItemClickListener,
    NavigationView.OnNavigationItemSelectedListener
{
    lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private val STORAGE_PERMISSION_CODE = 101
    private val INTERNET_PERMISSION_CODE = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        this.setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_CODE)

        checkPermission(
            Manifest.permission.INTERNET,
            INTERNET_PERMISSION_CODE)

        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        binding.navView.setNavigationItemSelectedListener(this)
        val paths = getExternalFilesDirs(null)
        paths.forEach{
            //this is disgusting, but it works...
            //paths back from /storage/emulated/0/Android/data/org.zornco.ra_playlist.maker/files to get the root of the drive.
            DataHolder.storageRoots.add(it.parentFile.parentFile.parentFile.parent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {

            // Requesting the permission
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(permission),
                requestCode
            )
        } /*else {
            Toast.makeText(
                this@FileBrowserActivity,
                "Permission already granted",
                Toast.LENGTH_SHORT
            )
                .show()
        }*/
    }

    override fun onClick(obj: Any) {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as? NavHostFragment
        val currentFragment = fragment?.childFragmentManager?.fragments?.first() as? OnItemClickListener
        currentFragment?.onClick(obj)
    }

    override fun onLongClick(obj: Any) {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as? NavHostFragment
        val currentFragment = fragment?.childFragmentManager?.fragments?.first() as? OnItemClickListener
        currentFragment?.onLongClick(obj)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        val fragment = this.supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.first()
        val currentFragment = fragment as? IOnBackPressed
        if (currentFragment != null) {
            if (!currentFragment.onBackPressed())
            {
                this.finish()
            }
        }
        else
        {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.settings -> {
                val inten = Intent(this, SettingsActivity::class.java)
                startActivity(inten)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

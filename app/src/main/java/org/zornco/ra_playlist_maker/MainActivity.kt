package org.zornco.ra_playlist_maker

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import org.zornco.ra_playlist_maker.file_browser.IOnBackPressed
import org.zornco.ra_playlist_maker.common.OnItemClickListener
import org.zornco.ra_playlist_maker.databinding.ActivityMainBinding
import java.io.*

class MainActivity : AppCompatActivity(),
    OnItemClickListener
{

    lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private val STORAGE_PERMISSION_CODE = 101
    private val INTERNET_PERMISSION_CODE = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_CODE)
        checkPermission(
            Manifest.permission.INTERNET,
            INTERNET_PERMISSION_CODE)
        //binding.textView.text = Build.SUPPORTED_ABIS[0]
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)
    }

    fun InputStream.toFile(path: String) {
        File(path).outputStream().use { this.copyTo(it) }
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
        val currentFragment = fragment?.childFragmentManager?.fragments?.get(0) as? OnItemClickListener
        currentFragment?.onClick(obj)
    }

    override fun onLongClick(obj: Any) {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as? NavHostFragment
        val currentFragment = fragment?.childFragmentManager?.fragments?.get(0) as? OnItemClickListener
        currentFragment?.onLongClick(obj)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val fragment = this.supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as? NavHostFragment
        val currentFragment = fragment?.childFragmentManager?.fragments?.get(0) as? IOnBackPressed
        currentFragment?.onBackPressed()?.takeIf { !it }?.let{
            this.finish()
        }
    }

}

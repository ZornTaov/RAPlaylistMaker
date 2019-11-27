package org.zornco.ra_playlist_maker.file_browser

import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.zornco.ra_playlist_maker.common.FileModel
import org.zornco.ra_playlist_maker.common.FileType
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.*
import org.zornco.ra_playlist_maker.Libretro.JsonClasses
import android.os.Build;
import androidx.navigation.fragment.NavHostFragment
import org.zornco.ra_playlist_maker.Libretro.PlaylistLoader
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.BackStackManager
import org.zornco.ra_playlist_maker.common.BreadcrumbRecyclerAdapter
import org.zornco.ra_playlist_maker.databinding.FileBrowserBinding

class FileBrowserActivity : AppCompatActivity(){
    private lateinit var binding : FileBrowserBinding
    private lateinit var drawerLayout: DrawerLayout
    private val backStackManager = BackStackManager<FileModel>()
    private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter<FileModel>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.file_browser
        )
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        //drawerLayout = binding.drawerLayout
        //val navController = this.findNavController(R.id.myNavHostFragment)
        //NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        //NavigationUI.setupWithNavController(binding.navView, navController)

        if (savedInstanceState == null) {
            val filesListFragment =
                FilesListFragment.build {
                    path = Environment.getExternalStorageDirectory().absolutePath
                }

            supportFragmentManager.beginTransaction()
                .add(R.id.container, filesListFragment)
                .addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()
        }
        initViews()
        initBackStack()
        val gson = Gson()
        val systemList: List<JsonClasses.RASystem> = gson.fromJson(PlaylistLoader.loadJSONFromAsset(this, "systems.json"), Array<JsonClasses.RASystem>::class.java).toList()
        val count = systemList.count()
        val build = Build.SUPPORTED_ABIS[0]

        /*val json:JSONArray = PlaylistLoader.load(this)
        val text = findViewById<TextView>(R.id.textview)
        text.text = json.length().toString()*/
    }

    private fun initViews()
    {
        setSupportActionBar(binding.toolbar)

        binding.breadcrumbRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBreadcrumbRecyclerAdapter =
            BreadcrumbRecyclerAdapter()
        binding.breadcrumbRecyclerView.adapter = mBreadcrumbRecyclerAdapter
        mBreadcrumbRecyclerAdapter.onItemClickListener = {
            supportFragmentManager.popBackStack(it.path, 2)
            backStackManager.popFromStackTill(it)
        }
    }

    private fun initBackStack()
    {
        backStackManager.onStackChangeListener = {
            updateAdapterData(it)
        }
        backStackManager.addToStack(fileModel = FileModel(Environment.getExternalStorageDirectory().absolutePath, FileType.FOLDER, "/", 0.0))
    }

    private fun updateAdapterData(files: List<FileModel>) {
        mBreadcrumbRecyclerAdapter.updateData(files)
        if (files.isNotEmpty())
        {
            binding.breadcrumbRecyclerView.smoothScrollToPosition(files.size - 1)
        }
    }


    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.filesRecyclerView) as? NavHostFragment
        val currentFragment = fragment?.childFragmentManager?.fragments?.get(0) as? IOnBackPressed
        currentFragment?.onBackPressed()?.takeIf { !it }?.let{ super.onBackPressed() }

    }


}

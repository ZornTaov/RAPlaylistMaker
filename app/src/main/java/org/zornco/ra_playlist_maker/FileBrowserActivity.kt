package org.zornco.ra_playlist_maker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.zornco.ra_playlist_maker.common.FileModel
import org.zornco.ra_playlist_maker.common.FileType
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import org.zornco.ra_playlist_maker.common.FileUtils.Companion.launchFileIntent
import com.google.gson.*
import org.zornco.ra_playlist_maker.Libretro.JsonClasses
import android.os.Build;
import org.zornco.ra_playlist_maker.Libretro.Playlist
import org.zornco.ra_playlist_maker.databinding.FileBrowserBinding

class FileBrowserActivity : AppCompatActivity(), FilesListFragment.OnItemClickListener{
    private lateinit var binding : FileBrowserBinding
    private lateinit var drawerLayout: DrawerLayout
    private val backStackManager = BackStackManager()
    private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.file_browser)
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        //drawerLayout = binding.drawerLayout
        //val navController = this.findNavController(R.id.myNavHostFragment)
       // NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        //NavigationUI.setupWithNavController(binding.navView, navController)

        if (savedInstanceState == null) {
            val filesListFragment = FilesListFragment.build {
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
        val systemList: List<JsonClasses.RASystem> = gson.fromJson(Playlist.loadJSONFromAsset(this, "systems.json"), Array<JsonClasses.RASystem>::class.java).toList()
        val count = systemList.count()
        val build = Build.SUPPORTED_ABIS[0]

        /*val json:JSONArray = Playlist.load(this)
        val text = findViewById<TextView>(R.id.textview)
        text.text = json.length().toString()*/
    }

    private fun initViews()
    {
        setSupportActionBar(binding.toolbar)

        binding.breadcrumbRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBreadcrumbRecyclerAdapter = BreadcrumbRecyclerAdapter()
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


    override fun onClick(fileModel: FileModel) {
        if (fileModel.fileType == FileType.FOLDER)
        {
            addFileFragment(fileModel)
        }
        else
        {
            launchFileIntent(fileModel)
        }
    }

    override fun onLongClick(fileModel: FileModel) {

    }

    private fun addFileFragment(fileModel: FileModel)
    {
        val filesListFragment = FilesListFragment.build { path = fileModel.path }
        backStackManager.addToStack(fileModel)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, filesListFragment)
        fragmentTransaction.addToBackStack(fileModel.path)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backStackManager.popFromStack()
        if (supportFragmentManager.backStackEntryCount == 0)
        {
            finish()
        }
    }


}

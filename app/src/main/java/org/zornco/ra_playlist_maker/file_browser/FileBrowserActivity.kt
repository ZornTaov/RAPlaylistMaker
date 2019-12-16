package org.zornco.ra_playlist_maker.file_browser

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import org.zornco.ra_playlist_maker.libretro.JsonClasses
import android.util.Log
import org.zornco.ra_playlist_maker.playlist.EntryEditorActivity
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.*
import org.zornco.ra_playlist_maker.databinding.ActivityFileBrowserBinding

class FileBrowserActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding : ActivityFileBrowserBinding
    private val backStackManager = BackStackManager<FileModel>()
    private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter<FileModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_file_browser
        )
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        if (savedInstanceState == null) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val useAllExt = sharedPreferences.getBoolean("use_all_ext", false)
            DataHolder.currentStoragePath = sharedPreferences.getString("list_preference_1", DataHolder.storageRoots[0])!!
            val filesListFragment =
                FilesListFragment.build {
                    path = DataHolder.currentStoragePath
                    extensions = (if (useAllExt || DataHolder.currentSystem!!.systemExt.isEmpty())
                                    DataHolder.currentSystem!!.allExt else DataHolder.currentSystem!!.systemExt
                                 ).toTypedArray()
                }

            this.supportFragmentManager.beginTransaction()
                .add(R.id.container, filesListFragment)
                .addToBackStack(DataHolder.currentStoragePath)
                .commit()
            initViews()
            initBackStack()
        } else
        {
            val filemodel: FileModel? = savedInstanceState.getParcelable("Top")
            if (filemodel != null) {
                addFileFragment(filemodel)
            }
            initViews()
            initBackStack(filemodel)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("saveInst", backStackManager.top.name)
        outState.putParcelable("Top",backStackManager.top)
    }

    private fun initViews()
    {
        (this as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        binding.breadcrumbRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBreadcrumbRecyclerAdapter = BreadcrumbRecyclerAdapter()
        binding.breadcrumbRecyclerView.adapter = mBreadcrumbRecyclerAdapter
        mBreadcrumbRecyclerAdapter.onItemClickListener = {
            this.supportFragmentManager.popBackStack(it.path, 2)
            backStackManager.popFromStackTill(it)
        }
    }


    private fun initBackStack(fileModel: FileModel? = null)
    {
        backStackManager.onStackChangeListener = {
            updateAdapterData(it)
        }
        backStackManager.addToStack(fileModel ?: FileModel(DataHolder.currentStoragePath, FileType.FOLDER, "/", 0.0))
    }

    private fun updateAdapterData(files: List<FileModel>) {
        mBreadcrumbRecyclerAdapter.updateData(files)
        if (files.isNotEmpty())
        {
            binding.breadcrumbRecyclerView.smoothScrollToPosition(files.size - 1)
        }
    }

    override fun onClick(obj: Any) {
        val fileModel = obj as FileModel
        if (fileModel.fileType == FileType.FOLDER)
        {
            addFileFragment(fileModel)
        }
        else
        {
            val playlistModel: JsonClasses.RAPlaylistEntry = JsonClasses.RAPlaylistEntry(path = fileModel.path, label = fileModel.name)
            DataHolder.currentEntry = playlistModel
            val inten = Intent(this, EntryEditorActivity::class.java)
            startActivityForResult(inten,101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }

    override fun onLongClick(obj: Any) {

    }

    private fun addFileFragment(fileModel: FileModel)
    {
        val filesListFragment =
            FilesListFragment.build {
                path = fileModel.path
                extensions = DataHolder.currentSystem!!.allExt.toTypedArray()
            }
        backStackManager.addToStack(fileModel)
        this.supportFragmentManager.beginTransaction()
            .replace(R.id.container, filesListFragment)
            .addToBackStack(fileModel.path)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backStackManager.popFromStack()
        if (this.supportFragmentManager.backStackEntryCount == 0)
        {
            finish()
        }

    }
}

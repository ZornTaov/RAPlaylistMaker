package org.zornco.ra_playlist_maker.file_browser


import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.BackStackManager
import org.zornco.ra_playlist_maker.common.BreadcrumbRecyclerAdapter
import org.zornco.ra_playlist_maker.common.FileModel
import org.zornco.ra_playlist_maker.common.FileType
import org.zornco.ra_playlist_maker.common.FileUtils.Companion.launchFileIntent
import org.zornco.ra_playlist_maker.databinding.FragmentFileBrowserBinding

/**
 * A simple [Fragment] subclass.
 */
class FileBrowserFragment : Fragment(), IOnBackPressed,
    FilesListFragment.OnItemClickListener {
    private lateinit var binding : FragmentFileBrowserBinding
    private val backStackManager = BackStackManager<FileModel>()
    private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter<FileModel>

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View?
    {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_file_browser, container, false)
        if (savedInstanceState == null) {
            val filesListFragment =
                FilesListFragment.build {
                    path = Environment.getExternalStorageDirectory().absolutePath
                }

            this.activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.container, filesListFragment)
                .addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()
        }
        initViews()
        initBackStack()
        return binding.root
    }

    private fun initViews()
    {
        (this.activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)

        binding.breadcrumbRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        mBreadcrumbRecyclerAdapter =
            BreadcrumbRecyclerAdapter()
        binding.breadcrumbRecyclerView.adapter = mBreadcrumbRecyclerAdapter
        mBreadcrumbRecyclerAdapter.onItemClickListener = {
            this.activity?.supportFragmentManager?.popBackStack(it.path, 2)
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
        Log.d("TAG", "${fileModel.path}")
    }

    override fun onLongClick(fileModel: FileModel) {

    }

    private fun addFileFragment(fileModel: FileModel)
    {
        val filesListFragment =
            FilesListFragment.build {
                path = fileModel.path
            }
        backStackManager.addToStack(fileModel)
        val fragmentTransaction = this.activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, filesListFragment)
        fragmentTransaction.addToBackStack(fileModel.path)
        fragmentTransaction.commit()
    }
    override fun onBackPressed(): Boolean {
        backStackManager.popFromStack()
        if (this.activity?.supportFragmentManager?.backStackEntryCount == 0)
        {
            return false
        }
        Log.d("TAG", "q path")
        return true
    }
}

interface IOnBackPressed {
    fun onBackPressed(): Boolean

}

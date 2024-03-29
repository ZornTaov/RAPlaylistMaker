package org.zornco.ra_playlist_maker.file_browser

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_files_list.*
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.FileUtils.Companion.getFileModelsFromFiles
import org.zornco.ra_playlist_maker.common.FileUtils.Companion.getFilesFromPath
import org.zornco.ra_playlist_maker.common.OnItemClickListener
import java.lang.Exception

class FilesListFragment : Fragment() {
    private lateinit var mFilesAdapter: FilesRecyclerAdapter
    private lateinit var PATH: String
    private lateinit var EXTENSIONS:List<String>
    private lateinit var mCallback: OnItemClickListener


    companion object {
        private const val ARG_PATH: String = "org.zornco.ra_playlist_maker.fileslist.path"
        private const val ARG_EXTENSIONS: String = "org.zornco.ra_playlist_maker.fileslist.extensions"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var path: String = ""
        var extensions: Array<String> = arrayOf()
        fun build(): FilesListFragment {
            val fragment = FilesListFragment()
            val args = Bundle()
            args.putString(ARG_PATH, path)
            args.putStringArray(ARG_EXTENSIONS, extensions)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            mCallback = context as OnItemClickListener
        }catch (e: Exception)
        {
            throw  Exception("$context should implement FilesListFragment.OnItemClickListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filePath = arguments?.getString(ARG_PATH)
        if (filePath == null) {
            Toast.makeText(context, "Path should not be null!", Toast.LENGTH_SHORT).show()
            return
        }
        PATH = filePath
        EXTENSIONS = arguments?.getStringArray(ARG_EXTENSIONS)!!.toList()

        initViews()
    }

    private fun initViews() {
        filesRecyclerView.layoutManager = LinearLayoutManager(context)
        mFilesAdapter = FilesRecyclerAdapter()
        filesRecyclerView.adapter = mFilesAdapter
        updateDate()
        mFilesAdapter.onItemClickListener = {
            mCallback.onClick(it)
        }

        mFilesAdapter.onItemLongClickListener = {
            mCallback.onLongClick(it)
        }
    }

    private fun updateDate() {
        val files = getFileModelsFromFiles(getFilesFromPath(PATH))

        if (files.isEmpty()) {
            emptyFolderLayout.visibility = View.VISIBLE
        } else {
            emptyFolderLayout.visibility = View.INVISIBLE
        }
        mFilesAdapter.updateData(files, EXTENSIONS)
    }
}
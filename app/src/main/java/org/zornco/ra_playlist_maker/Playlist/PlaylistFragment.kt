package org.zornco.ra_playlist_maker.playlist


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
import org.zornco.ra_playlist_maker.libretro.JsonClasses

import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.databinding.FragmentPlaylistBinding
import org.zornco.ra_playlist_maker.common.BackStackManager
import org.zornco.ra_playlist_maker.common.BreadcrumbRecyclerAdapter
import org.zornco.ra_playlist_maker.common.OnItemClickListener

class PlaylistFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentPlaylistBinding
    //private val backStackManager = BackStackManager<JsonClasses.RAPlaylistEntry>()
    //private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter<JsonClasses.RAPlaylistEntry>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_playlist, container, false)
        val args = PlaylistFragmentArgs.fromBundle(arguments!!).system
        if (savedInstanceState == null) {
            val filesListFragment =
                PlaylistListFragment.build {
                    path = args
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
        (this.activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        //binding.breadcrumbRecyclerView.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        //mBreadcrumbRecyclerAdapter = BreadcrumbRecyclerAdapter()
        //binding.breadcrumbRecyclerView.adapter = mBreadcrumbRecyclerAdapter
//        mBreadcrumbRecyclerAdapter.onItemClickListener = {
//            this.activity?.supportFragmentManager?.popBackStack(it.label, 2)
//            backStackManager.popFromStackTill(it)
//        }
    }

    private fun initBackStack()
    {
//        backStackManager.onStackChangeListener = {
//            updateAdapterData(it)
//        }
//        backStackManager.addToStack(fileModel = JsonClasses.RAPlaylistEntry("Playlist"))
    }

    private fun updateAdapterData(files: List<JsonClasses.RAPlaylistEntry>) {
        //mBreadcrumbRecyclerAdapter.updateData(files)
        //if (files.isNotEmpty())
        //{
        //    binding.breadcrumbRecyclerView.smoothScrollToPosition(files.size - 1)
        //}
    }
    override fun onClick(obj: Any) {
        val playlistModel = obj as JsonClasses.RAPlaylistEntry
        Log.d("TAG", "${playlistModel.label}")

    }

    override fun onLongClick(obj: Any) {

    }

    private fun addPlaylistragment(playlistModel: JsonClasses.RAPlaylistEntry)
    {
        val systemsListFragment =
            PlaylistListFragment.build {
                path = playlistModel.label
            }
        val fragmentTransaction = this.activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, systemsListFragment)
        fragmentTransaction.addToBackStack(playlistModel.label)
        fragmentTransaction.commit()
    }


}

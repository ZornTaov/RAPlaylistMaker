package org.zornco.ra_playlist_maker.playlist


import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import org.zornco.ra_playlist_maker.libretro.JsonClasses

import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.databinding.FragmentPlaylistBinding
import org.zornco.ra_playlist_maker.common.BackStackManager
import org.zornco.ra_playlist_maker.common.BreadcrumbRecyclerAdapter
import org.zornco.ra_playlist_maker.common.OnItemClickListener
import org.zornco.ra_playlist_maker.libretro.PlaylistLoader
import java.io.File

class PlaylistFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentPlaylistBinding
    //private val backStackManager = BackStackManager<JsonClasses.RAPlaylistEntry>()
    //private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter<JsonClasses.RAPlaylistEntry>
    val args: PlaylistFragmentArgs by navArgs()

    lateinit var playlist:JsonClasses.RAPlaylist

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist, container, false)
        binding.fab.setOnClickListener { onFabClick() }
        if (savedInstanceState == null) {
            val PATH = "/storage/emulated/0/RetroArch/playlists/${args.system.system[0]}.lpl"
            var newplaylist:JsonClasses.RAPlaylist
            try {
                newplaylist = PlaylistLoader.loadPlaylist(PATH)
            }
            catch (e:Exception)
            {
                //playlist does not exist?
                Log.d("PlLiFra", "Making New Playlist for $PATH")
                Toast.makeText(this.context, "Making new Playlist for ${args.system.system[0]}", Toast.LENGTH_SHORT).show()
                val newList = File(PATH)
                val gson = GsonBuilder().setPrettyPrinting().create()
                newplaylist = JsonClasses.RAPlaylist()
                newList.writeText(gson.toJson( newplaylist ))
                newList.writer().close()
            }
            val playlistListFragment =
                PlaylistListFragment.build {
                    playlist = newplaylist
                }
            this.activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.container, playlistListFragment)
                .addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()
            playlist = newplaylist
        }
        if (args.entry != null)
        {

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
        val ac = PlaylistFragmentDirections.actionPlaylistFragmentToEntryEditorFragment()
        ac.playlistEntry = playlistModel
        this.findNavController().navigate(ac)
    }

    override fun onLongClick(obj: Any) {

    }

    fun onFabClick() {
        val ac = PlaylistFragmentDirections.actionPlaylistFragmentToFileBrowserFragment(args.system)
        this.findNavController().navigate(ac)
    }

    private fun addPlaylistragment(playlistModel: JsonClasses.RAPlaylistEntry)
    {
        val systemsListFragment =
            PlaylistListFragment.build {
                playlist = playlist
            }
        val fragmentTransaction = this.activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, systemsListFragment)
        fragmentTransaction.addToBackStack(playlistModel.label)
        fragmentTransaction.commit()
    }


}

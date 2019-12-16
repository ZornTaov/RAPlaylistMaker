package org.zornco.ra_playlist_maker.playlist


import android.content.Intent
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
import org.zornco.ra_playlist_maker.common.*
import org.zornco.ra_playlist_maker.databinding.FragmentPlaylistBinding
import org.zornco.ra_playlist_maker.file_browser.FileBrowserActivity
import org.zornco.ra_playlist_maker.libretro.PlaylistLoader
import java.io.File

class PlaylistFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentPlaylistBinding
    //private val backStackManager = BackStackManager<JsonClasses.RAPlaylistEntry>()
    //private lateinit var mBreadcrumbRecyclerAdapter: BreadcrumbRecyclerAdapter<JsonClasses.RAPlaylistEntry>
    private lateinit var playlistListFragment: PlaylistListFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist, container, false)
        binding.fab.setOnClickListener { onFabClick() }
        if (savedInstanceState == null) {
            val PATH = "/storage/emulated/0/RetroArch/playlists/${DataHolder.currentSystem!!.system[0]}.lpl"
            var newplaylist:JsonClasses.RAPlaylist
            try {
                newplaylist = PlaylistLoader.loadPlaylist(PATH)
            }
            catch (e:Exception)
            {
                //playlist does not exist?
                Log.d("PlLiFra", "Making New Playlist for $PATH")
                Toast.makeText(this.context, "Making new Playlist for ${DataHolder.currentSystem!!.system[0]}", Toast.LENGTH_SHORT).show()
                val newList = File(PATH)
                val gson = GsonBuilder().setPrettyPrinting().create()
                newplaylist = JsonClasses.RAPlaylist()
                newList.writeText(gson.toJson( newplaylist ))
                newList.writer().close()
            }
            newplaylist.PATH = PATH
            playlistListFragment =
                PlaylistListFragment.build {
                    playlist = newplaylist
                }
            this.activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.container, playlistListFragment)
                //.addToBackStack(Environment.getExternalStorageDirectory().absolutePath)
                .commit()
            DataHolder.currentPlaylist = newplaylist
        }
        if (DataHolder.currentEntry != null)
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

    override fun onClick(obj: Any) {
        val playlistModel = obj as JsonClasses.RAPlaylistEntry
        Log.d("TAG", "${playlistModel.label}")
        DataHolder.currentState = if(DataHolder.currentPlaylist!!.items.isEmpty() )PlaylistState.FIRST else PlaylistState.EDIT
        DataHolder.currentEntry = playlistModel
        val inten = Intent(this.activity, EntryEditorActivity::class.java)
        startActivityForResult(inten,101)
    }

    override fun onLongClick(obj: Any) {
        DataHolder.currentPlaylist!!.items.removeAt(DataHolder.playlistIndex)
        updatePlaylist()
    }

    fun onFabClick() {
//        val ac = PlaylistFragmentDirections.actionPlaylistFragmentToFileBrowserActivity()
//        this.findNavController().navigate(ac)
        DataHolder.currentState = PlaylistState.ADD
        val inten = Intent(this.activity, FileBrowserActivity::class.java)
        startActivityForResult(inten,101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updatePlaylist()
    }
    fun updatePlaylist(){
        val broadcastIntent = Intent()
        broadcastIntent.action = PlaylistListFragment.BROADCAST_EVENT
        broadcastIntent.putExtra(ListChangeBroadcastReceiver.EXTRA_PATH, playlistListFragment.playlistName)
        this.activity?.sendBroadcast(broadcastIntent)
    }
}

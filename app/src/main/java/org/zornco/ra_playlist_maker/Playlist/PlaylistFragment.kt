package org.zornco.ra_playlist_maker.playlist

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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
    private lateinit var playlistListFragment: PlaylistListFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_playlist, container, false)
        binding.fab.setOnClickListener { onFabClick() }
        if (savedInstanceState == null) {
            val path = "/storage/emulated/0/RetroArch/playlists/${DataHolder.currentSystem!!.system[0]}.lpl"
            var newplaylist:JsonClasses.RAPlaylist
            try {
                newplaylist = PlaylistLoader.loadPlaylist(path)
            }
            catch (e:Exception)
            {
                //playlist does not exist?
                Log.d("PlLiFra", "Making New Playlist for $path")
                Toast.makeText(this.context, "Making new Playlist for ${DataHolder.currentSystem!!.system[0]}", Toast.LENGTH_SHORT).show()
                val newList = File(path)
                newList.parentFile.mkdirs()
                val gson = GsonBuilder().setPrettyPrinting().create()
                newplaylist = JsonClasses.RAPlaylist()
                newList.writeText(gson.toJson( newplaylist ))
                newList.writer().close()
            }
            newplaylist.PATH = path
            playlistListFragment =
                PlaylistListFragment.build {
                    playlist = newplaylist
                }
            this.activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.container, playlistListFragment)
                .commit()
            DataHolder.currentPlaylist = newplaylist
        }
        initViews()
        return binding.root
    }

    private fun initViews()
    {
        (this.activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
    }

    override fun onClick(obj: Any) {
        val playlistModel = obj as JsonClasses.RAPlaylistEntry
        DataHolder.currentState = if(DataHolder.currentPlaylist!!.items.isEmpty() )PlaylistState.FIRST else PlaylistState.EDIT
        DataHolder.currentEntry = playlistModel
        val inten = Intent(this.activity, EntryEditorActivity::class.java)
        startActivityForResult(inten,101)
    }

    override fun onLongClick(obj: Any) {
        val dialogBuilder = AlertDialog.Builder(this.activity)

        // set message of alert dialog
        dialogBuilder.setMessage("Do you want to delete this entry?")
            .setCancelable(false)
            .setPositiveButton("Delete") { _, _ ->
                DataHolder.currentPlaylist!!.items.removeAt(DataHolder.playlistIndex)
                updatePlaylist()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun onFabClick() {
        DataHolder.currentState = PlaylistState.ADD
        val inten = Intent(this.activity, FileBrowserActivity::class.java)
        startActivityForResult(inten,101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updatePlaylist()
    }

    private fun updatePlaylist(){
        val broadcastIntent = Intent()
        broadcastIntent.action = PlaylistListFragment.BROADCAST_EVENT
        broadcastIntent.putExtra(ListChangeBroadcastReceiver.EXTRA_PATH, playlistListFragment.playlistName)
        this.activity?.sendBroadcast(broadcastIntent)
    }
}

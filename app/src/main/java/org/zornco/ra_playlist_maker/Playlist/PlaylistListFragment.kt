package org.zornco.ra_playlist_maker.systems


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_playlist_list.*
import org.zornco.ra_playlist_maker.Libretro.JsonClasses
import org.zornco.ra_playlist_maker.Libretro.PlaylistLoader
import org.zornco.ra_playlist_maker.MainActivity
import org.zornco.ra_playlist_maker.R
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class PlaylistListFragment : Fragment() {

    private lateinit var mFilesAdapter: PlaylistRecyclerAdapter
    private lateinit var mCallback: OnItemClickListener

    interface OnItemClickListener {
        fun onClick(playlistModel: JsonClasses.RAPlaylistEntry)

        fun onLongClick(playlistModel: JsonClasses.RAPlaylistEntry)
    }

    companion object {
        private const val ARG_PATH: String = "org.zornco.ra_playlist_maker.systems.path"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        var path: String = ""

        fun build(): PlaylistListFragment {
            val fragment = PlaylistListFragment()
            val args = Bundle()
            args.putString(ARG_PATH, path)
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
            throw  Exception("$context should implement PlaylistListFragment.OnItemClickListener")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playlist_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        playlistRecyclerView.layoutManager = LinearLayoutManager(context)
        mFilesAdapter = PlaylistRecyclerAdapter()
        playlistRecyclerView.adapter = mFilesAdapter
        updateDate()
        mFilesAdapter.onItemClickListener = {
            mCallback.onClick(it)
        }
        mFilesAdapter.onItemLongClickListener = {
            mCallback.onLongClick(it)
        }
    }

    private fun updateDate() {
        val files = PlaylistLoader.loadPlaylist(this.activity as MainActivity, "/storage/emulated/0/RetroArch/playlists/Nintendo - Super Nintendo Entertainment System.lpl")

        if (files.isEmpty()) {
            emptyPlaylistLayout.visibility = View.VISIBLE
        } else {
            emptyPlaylistLayout.visibility = View.INVISIBLE
        }

        mFilesAdapter.updateData(files)
    }

    private fun getEntriesFromPlaylist(playlist : MutableList<JsonClasses.RAPlaylistEntry>): List<JsonClasses.RAPlaylistEntry> {
        return playlist.map {
            JsonClasses.RAPlaylistEntry(
                it.path,
                it.label,
                it.core_path,
                it.core_name,
                it.crc32,
                it.db_name
            )
        }
    }


}

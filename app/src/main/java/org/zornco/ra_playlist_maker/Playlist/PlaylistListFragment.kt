package org.zornco.ra_playlist_maker.playlist


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_playlist_list.*
import org.zornco.ra_playlist_maker.libretro.JsonClasses
import org.zornco.ra_playlist_maker.libretro.PlaylistLoader
import org.zornco.ra_playlist_maker.MainActivity
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.OnItemClickListener
import java.io.File
import java.io.FileNotFoundException
import kotlin.Exception
import com.google.gson.GsonBuilder



class PlaylistListFragment : Fragment() {

    private lateinit var mFilesAdapter: PlaylistRecyclerAdapter
    private lateinit var mCallback: OnItemClickListener
    lateinit var playlist: JsonClasses.RAPlaylist

    companion object {
        private const val ARG_PLAYLIST: String = "org.zornco.ra_playlist_maker.systems.playlist"
        fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    class Builder {
        lateinit var playlist: JsonClasses.RAPlaylist

        fun build(): PlaylistListFragment {
            val fragment = PlaylistListFragment()
            val args = Bundle()

            args.putParcelable(ARG_PLAYLIST, playlist)
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

        playlist = arguments?.getParcelable<JsonClasses.RAPlaylist>(ARG_PLAYLIST)!!

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

        if (playlist.items.isEmpty()) {
            emptyPlaylistLayout.visibility = View.VISIBLE
        } else {
            emptyPlaylistLayout.visibility = View.INVISIBLE
        }

        mFilesAdapter.updateData(playlist.items)
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

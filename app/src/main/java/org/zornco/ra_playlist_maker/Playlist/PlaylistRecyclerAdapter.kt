package org.zornco.ra_playlist_maker.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_recycler_file.view.*
import org.zornco.ra_playlist_maker.libretro.JsonClasses
import org.zornco.ra_playlist_maker.R
import org.zornco.ra_playlist_maker.common.DataHolder

class PlaylistRecyclerAdapter : RecyclerView.Adapter<PlaylistRecyclerAdapter.ViewHolder>() {

    var onItemClickListener: ((JsonClasses.RAPlaylistEntry) -> Unit)? = null
    var onItemLongClickListener: ((JsonClasses.RAPlaylistEntry) -> Unit)? = null

    var playlistMap = listOf<JsonClasses.RAPlaylistEntry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_playlist, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = playlistMap.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    fun updateData(filesList: List<JsonClasses.RAPlaylistEntry>) {
        this.playlistMap = filesList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        override fun onClick(v: View?) {
            DataHolder.playlistIndex = adapterPosition
            onItemClickListener?.invoke(playlistMap[adapterPosition])
        }

        override fun onLongClick(v: View?): Boolean {
            DataHolder.playlistIndex = adapterPosition
            onItemLongClickListener?.invoke(playlistMap[adapterPosition])
            return true
        }

        fun bindView(position: Int) {
            val playlistModel = playlistMap[position]
            itemView.nameTextView.text = playlistModel.label
            itemView.setBackgroundColor(0xEEEEEE)
        }
    }
}
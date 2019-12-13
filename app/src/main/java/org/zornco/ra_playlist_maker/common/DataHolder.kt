package org.zornco.ra_playlist_maker.common

import org.zornco.ra_playlist_maker.libretro.JsonClasses


class DataHolder{
    var currentSystem: JsonClasses.RASystem? = null
    var currentPlaylist: JsonClasses.RAPlaylist? = null
    var currentEntry: JsonClasses.RAPlaylistEntry? = null

    companion object {
        private val holder = DataHolder()
        fun getInstance(): DataHolder { return holder }
    }
}
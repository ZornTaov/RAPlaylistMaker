package org.zornco.ra_playlist_maker.common

import org.zornco.ra_playlist_maker.libretro.JsonClasses

enum class PlaylistState {
    FIRST, ADD, EDIT
}

class DataHolder{
    companion object {
        var currentSystem: JsonClasses.RASystem? = null
        var currentPlaylist: JsonClasses.RAPlaylist? = null
        var playlistIndex: Int = -1
        var currentEntry: JsonClasses.RAPlaylistEntry? = null
        var currentState: PlaylistState = PlaylistState.ADD
        var storageRoots: MutableList<String> = mutableListOf()
        var currentStoragePath: String = ""
    }
}
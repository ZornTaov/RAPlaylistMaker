package org.zornco.ra_playlist_maker.libretro

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


class JsonClasses {
    @Parcelize
    data class RASystem(
        val name: String = "",
        var cores: MutableList<String> = mutableListOf(),
        var system: MutableList<String> = mutableListOf(),
        var allExt: MutableList<String> = mutableListOf(),
        var systemExt: MutableList<String> = mutableListOf()
    ) : Parcelable

    @Parcelize
    data class RAPlaylistEntry(
        var path: String = "",
        var label: String = "",
        var core_path: String = "",
        var core_name: String = "",
        var crc32: String = "",
        var db_name: String = ""
    ) : Parcelable

    @Parcelize
    data class RAPlaylist(
        var version: String = "1.2",
        var default_core_path: String = "",
        var default_core_name: String = "",
        var label_display_mode: Int = 0,
        var right_thumbnail_mode: Int = 0,
        var left_thumbnail_mode: Int = 0,
        var items: MutableList<RAPlaylistEntry> = mutableListOf()
    ) : Parcelable

    data class CoreInfo(val map: Map<String, Any?>) {
        val display_name: String by map
        val supported_extensions: String by map
        val corename: String by map
        val manufacturer: String by map
        val systemname: String by map
        val systemid: String by map
        val database: String by map
        val supports_no_game: String by map
    }
}
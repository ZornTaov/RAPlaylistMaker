package org.zornco.ra_playlist_maker.Libretro

class JsonClasses {
    data class RASystem(
        val name: String,
        var cores: MutableList<String>,
        var system: MutableList<String>,
        var allExt: MutableList<String>,
        var systemExt: MutableList<String>
    )
    data class RAPlaylist(
        var path: String,
        var label: String,
        var core_path: String,
        var core_name: String,
        var crc32: String,
        var db_name: String
    )

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